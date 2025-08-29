package com.clip.batch.notification;

import com.clip.batch.notification.dto.NotificationProjection;
import com.clip.batch.notification.dto.OnethingMatchingNotification;
import com.clip.batch.notification.dto.OnethingMatchingProjection;
import com.clip.infra.fcm.event.FcmNotificationEvent;
import com.clip.infra.fcm.service.MessageParams;
import com.clip.infra.fcm.service.MessageTemplateType;
import com.clip.infra.fcm.service.SendFCMService;
import com.clip.notification.entity.NotificationType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.database.JdbcBatchItemWriter;
import org.springframework.batch.item.database.JdbcPagingItemReader;
import org.springframework.batch.item.database.Order;
import org.springframework.batch.item.database.builder.JdbcBatchItemWriterBuilder;
import org.springframework.batch.item.database.builder.JdbcPagingItemReaderBuilder;
import org.springframework.batch.item.support.CompositeItemWriter;
import org.springframework.batch.item.support.builder.CompositeItemWriterBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

@RequiredArgsConstructor
@Configuration
@Slf4j
public class OneThingMatchingInfoNotificationBatchConfig {

    private final JobRepository jobRepository;
    private final PlatformTransactionManager transactionManager;
    private static final int CHUNK_SIZE = 100;
    private static final int PAGE_SIZE = 100;
    private final DataSource dataSource;
    private final SendFCMService sendFCMService;

    @Bean
    public Job sendOneThingMatchingInfoFcmJob(Step writeNotificationStep, Step sendFcmOneThingStep) {
        return new JobBuilder("sendOneThingMatchingInfoFcmJob", jobRepository)
                .incrementer(new RunIdIncrementer())
                .start(writeNotificationStep)
                .next(sendFcmOneThingStep)
                .build();
    }

    @Bean
    public Step writeNotificationStep(
            JdbcPagingItemReader<OnethingMatchingProjection> userOneThingReaderByDate,
            ItemProcessor<OnethingMatchingProjection, OnethingMatchingNotification> notificationMessageProcessor,
            JdbcBatchItemWriter<OnethingMatchingNotification> fcmOneThingWriter) {
        return new StepBuilder("writeNotificationStep", jobRepository)
                .<OnethingMatchingProjection, OnethingMatchingNotification>chunk(CHUNK_SIZE, transactionManager)
                .reader(userOneThingReaderByDate)
                .processor(notificationMessageProcessor)
                .writer(fcmOneThingWriter)
                .build();
    }

    @Bean
    public Step sendFcmOneThingStep(
            JdbcPagingItemReader<NotificationProjection> notificationReader,
            CompositeItemWriter<NotificationProjection> compositeFcmOneThingWriter
    ) {
        return new StepBuilder("sendFcmOneThingStep", jobRepository)
                .<NotificationProjection, NotificationProjection>chunk(CHUNK_SIZE, transactionManager)
                .reader(notificationReader)
                .writer(compositeFcmOneThingWriter)
                .build();
    }

    @Bean
    @StepScope
    public JdbcPagingItemReader<OnethingMatchingProjection> userOneThingReaderByDate(
            @Value("#{jobParameters['targetDate']}") LocalDate targetDate) {
        log.info("OneThing FCM Reader - targetDate: {}", targetDate);
        return new JdbcPagingItemReaderBuilder<OnethingMatchingProjection>()
                .name("userOneThingReaderByDate")
                .dataSource(dataSource)
                .selectClause("""
                        select um.id, u.id as user_id, u.firebase_token as fcm_token, u.device_type as device_type,
                               om.id as one_thing_matching_id, om.date_time as date_time, om.restaurant_name as place
                        """)
                .fromClause("""
                        from user_one_thing_matching um
                        join one_thing_matching om on um.onething_matching_id = om.id
                        join user u on um.user_id = u.id
                        """)
                .whereClause("""
                        where DATE(om.date_time) = :targetDate
                        and um.matching_status = 'CONFIRMED'
                        and u.firebase_token is not null
                        and u.is_allow_notify = true
                        """)
                .sortKeys(Map.of("um.id", Order.ASCENDING))
                .parameterValues(Collections.singletonMap("targetDate", targetDate))
                .rowMapper(
                        (rs, rowNum) -> new OnethingMatchingProjection(
                                rs.getLong("user_id"),
                                rs.getString("fcm_token"),
                                rs.getString("device_type"),
                                rs.getLong("one_thing_matching_id"),
                                rs.getTimestamp("date_time").toLocalDateTime(),
                                rs.getString("place")
                        )
                )
                .pageSize(PAGE_SIZE)
                .build();
    }

    @Bean
    @StepScope
    public ItemProcessor<OnethingMatchingProjection, OnethingMatchingNotification> notificationMessageProcessor(
            @Value("#{jobParameters['messageTemplateType']}") String messageTemplateType
    ) {
        MessageTemplateType templateType = MessageTemplateType.valueOf(messageTemplateType);

        return item -> {
            MessageParams params = null;
            if (templateType == MessageTemplateType.MATCHING_TODAY) {
                params = new MessageParams.TimeAndPlaceParams(String.valueOf(item.dateTime().getHour()), item.place());
            }
            String message = templateType.generateMessage(params);
            return new OnethingMatchingNotification(
                    item.userId(),
                    item.fcmToken(),
                    item.deviceType(),
                    item.oneThingMatchingId(),
                    item.dateTime(),
                    item.place(),
                    message
            );
        };
    }

    @Bean
    @StepScope
    public JdbcBatchItemWriter<OnethingMatchingNotification> fcmOneThingWriter(
            @Value("#{jobParameters['messageTemplateType']}") String messageTemplateType,
            @Value("#{jobParameters['batchKey']}") String batchKey
    ) {
        MessageTemplateType templateType = MessageTemplateType.valueOf(messageTemplateType);
        log.info("OneThing FCM WriterWriter - MessageTemplateType: {}", templateType);

        return new JdbcBatchItemWriterBuilder<OnethingMatchingNotification>()
                .dataSource(dataSource)
                .sql("""
                        insert into notification (created_at, updated_at, notification_type, is_read, content, send_status, user_id, batch_key, matching_type, matching_id)
                        values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)
                        """)
                .itemPreparedStatementSetter((item, ps) -> {
                    ps.setTimestamp(1, Timestamp.valueOf(LocalDateTime.now()));
                    ps.setTimestamp(2, Timestamp.valueOf(LocalDateTime.now()));
                    ps.setString(3, NotificationType.MEETING.name());
                    ps.setBoolean(4, false);
                    ps.setString(5, item.message());
                    ps.setString(6, "PENDING");
                    ps.setLong(7, item.userId());
                    ps.setString(8, batchKey);
                    ps.setString(9, "ONE_THING");
                    ps.setLong(10, item.oneThingMatchingId());
                })
                .beanMapped()
                .build();

    }

    @Bean
    @StepScope
    public JdbcPagingItemReader<NotificationProjection> notificationReader(
            @Value("#{jobParameters['targetDate']}") LocalDate targetDate,
            @Value("#{jobParameters['batchKey']}") String batchKey) {
        return new JdbcPagingItemReaderBuilder<NotificationProjection>()
                .name("notificationReader")
                .dataSource(dataSource)
                .selectClause("""
                        select n.id as notification_id, u.id as user_id, u.firebase_token as fcm_token, u.device_type as device_type,
                               om.id as one_thing_matching_id, om.date_time as date_time, om.restaurant_name as place
                        """)
                .fromClause("""
                        from notification n
                        join user u on n.user_id = u.id
                        join one_thing_matching om on n.matching_id = om.id
                        """)
                .whereClause("""
                        where DATE(om.date_time) = :targetDate
                        and n.send_status = 'PENDING'
                        and n.batch_key = :batchKey
                        and n.matching_type = 'ONE_THING'
                        """)
                .sortKeys(Map.of("notification_id", Order.ASCENDING))
                .parameterValues(Map.of("targetDate", targetDate, "batchKey", batchKey))
                .rowMapper(
                        (rs, rowNum) -> new NotificationProjection(
                                rs.getLong("notification_id"),
                                rs.getLong("user_id"),
                                rs.getString("fcm_token"),
                                rs.getString("device_type"),
                                rs.getLong("one_thing_matching_id"),
                                rs.getTimestamp("date_time").toLocalDateTime(),
                                rs.getString("place")
                        )
                )
                .pageSize(PAGE_SIZE)
                .build();
    }

    @Bean
    public CompositeItemWriter<NotificationProjection> compositeFcmOneThingWriter(JdbcBatchItemWriter<NotificationProjection> notificationSendStatusWriter,
                                                                                  ItemWriter<NotificationProjection> sendFcmOneThingWriter) {
        return new CompositeItemWriterBuilder<NotificationProjection>().delegates(List.of(notificationSendStatusWriter, sendFcmOneThingWriter)).build();
    }

    @Bean
    @StepScope
    public JdbcBatchItemWriter<NotificationProjection> notificationSendStatusWriter() {
        return new JdbcBatchItemWriterBuilder<NotificationProjection>()
                .dataSource(dataSource)
                .sql("""
                        update notification
                        set send_status = 'SENT', updated_at = ?
                        where id = ?
                        """)
                .itemPreparedStatementSetter((item, ps) -> {
                    ps.setTimestamp(1, Timestamp.valueOf(LocalDateTime.now()));
                    ps.setLong(2, item.notificationId());
                })
                .build();
    }


    @Bean
    @StepScope
    public ItemWriter<NotificationProjection> sendFcmOneThingWriter(
            @Value("#{jobParameters['messageTemplateType']}") String messageTemplateType
    ) {
        log.info("OneThing Send FCM Writer - MessageTemplateType: {}", messageTemplateType);
        return items -> {
            Map<Long, FcmNotificationEvent.UserFcmData> userDataMap = new HashMap<>();
            MessageTemplateType templateType = MessageTemplateType.valueOf(messageTemplateType);

            for (NotificationProjection item : items) {
                MessageParams params;
                if (templateType == MessageTemplateType.MATCHING_TODAY) {
                    params = new MessageParams.TimeAndPlaceParams(String.valueOf(item.dateTime().getHour()), item.place());
                } else {
                    params = new MessageParams.EmptyParams();
                }

                userDataMap.put(item.oneThingMatchingId(), new FcmNotificationEvent.UserFcmData(
                        item.oneThingMatchingId(),
                        item.deviceType(),
                        item.fcmToken(),
                        params // 항상 null이 아닌 params 전달
                ));
            }
            sendFCMService.sendMsg(templateType, "ONE_THING", userDataMap);
        };
    }

}
