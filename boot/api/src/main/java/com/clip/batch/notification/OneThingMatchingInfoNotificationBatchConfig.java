package com.clip.batch.notification;

import com.clip.batch.notification.listener.FcmOnethingWriterListener;
import com.clip.batch.notification.projection.OnethingMatchingProjection;
import com.clip.notification.entity.NotificationType;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.database.JdbcBatchItemWriter;
import org.springframework.batch.item.database.JdbcPagingItemReader;
import org.springframework.batch.item.database.Order;
import org.springframework.batch.item.database.builder.JdbcBatchItemWriterBuilder;
import org.springframework.batch.item.database.builder.JdbcPagingItemReaderBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

/***
 * 원띵 매칭 정보 알림 배치 설정
 *
 * step 1: Notification 저장을 위한 데이터 조회 및 저장(ItemWriter)
 * ItemWriterListener에서 FCM 전송 이벤트 발행
 *
 */
@RequiredArgsConstructor
@Configuration
public class OneThingMatchingInfoNotificationBatchConfig {

    private final JobRepository jobRepository;
    private final PlatformTransactionManager transactionManager;
    private static final int CHUNK_SIZE = 100;
    private static final int PAGE_SIZE = 100;
    private final DataSource dataSource;

    @Bean
    public Job sendOneThingMatchingInfoFcmJob(Step sendFcmOneThingStep) {
        return new JobBuilder("sendOneThingMatchingInfoFcmJob", jobRepository)
                .incrementer(new RunIdIncrementer())
                .start(sendFcmOneThingStep)
                .build();
    }

    @Bean
    public Step sendFcmOneThingStep(
            JdbcPagingItemReader<OnethingMatchingProjection> userOneThingReaderByDate,
            JdbcBatchItemWriter<OnethingMatchingProjection> fcmOneThingWriter,
            FcmOnethingWriterListener fcmOnethingWriterListener) {
        return new StepBuilder("sendFcmOneThingStep", jobRepository)
                .<OnethingMatchingProjection, OnethingMatchingProjection>chunk( CHUNK_SIZE, transactionManager)
                .reader(userOneThingReaderByDate)
                .writer(fcmOneThingWriter)
                .listener(fcmOnethingWriterListener)
                .build();
    }

    @Bean
    @StepScope
    public JdbcPagingItemReader<OnethingMatchingProjection> userOneThingReaderByDate(
            @Value("#{jobParameters['targetDate']}") LocalDate targetDate) {
        return new JdbcPagingItemReaderBuilder<OnethingMatchingProjection>()
                .name("userOneThingReaderByDate")
                .dataSource(dataSource)
                .selectClause("""
                        select um.id, u.id as user_id, u.firebase_token as fcm_token, u.device_type as device_type,
                               om.id as one_thing_matching_id, om.date_time as date_time
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
                                rs.getTimestamp("date_time").toLocalDateTime().getDayOfWeek(),
                                rs.getTimestamp("date_time").toLocalDateTime()
                        )
                )
                .pageSize(PAGE_SIZE)
                .build();
    }

    @Bean
    @StepScope
    public JdbcBatchItemWriter<OnethingMatchingProjection> fcmOneThingWriter(
            @Value("#{jobParameters['messageTemplateType']}") String messageTemplateType
    ) {

        return new JdbcBatchItemWriterBuilder<OnethingMatchingProjection>()
                .dataSource(dataSource)
                .sql("""
                        insert into notification (created_at, updated_at, notification_type, is_read, message_template_type, send_status, user_id)
                        values (?, ?, ?, ?, ?)
                        """)
                .itemPreparedStatementSetter((item, ps) -> {
                    ps.setTimestamp(1, Timestamp.valueOf(LocalDateTime.now()));
                    ps.setTimestamp(2, Timestamp.valueOf(LocalDateTime.now()));
                    ps.setString(3, NotificationType.EVENT.name());
                    ps.setBoolean(4, false);
                    ps.setString(5, messageTemplateType);
                    ps.setString(6, "SENT");
                    ps.setLong(7, item.userId());
                })
                .beanMapped()
                .build();

    }

}
