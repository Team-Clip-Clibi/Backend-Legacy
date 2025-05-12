package data.coredata.com.clip.notification.service;

import com.clip.ApiApplication;
import com.clip.api.payment.feign.TossPaymentFeign;
import com.clip.global.config.feign.FeignConfig;
import com.clip.infra.aws.s3.S3Config;
import com.clip.infra.aws.s3.S3ImgService;
import com.clip.notification.entity.Notification;
import com.clip.notification.entity.NotificationType;
import com.clip.notification.repository.NotificationRepository;
import com.clip.notification.service.NotificationService;
import com.clip.user.entity.User;
import com.clip.user.repository.UserRepository;
import com.clip.user.service.UserService;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import java.util.List;

@ContextConfiguration(classes = ApiApplication.class)
@SpringBootTest
public class NotificationServiceTest {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private NotificationRepository notificationRepository;

    @Autowired
    private UserService userService;
    @Autowired
    private NotificationService notificationService;
    @MockitoBean
    private S3ImgService s3ImgService;
    @MockitoBean
    private S3Config s3Config;
    @MockitoBean
    private FeignConfig feignConfig;
    @MockitoBean
    private TossPaymentFeign tossPaymentFeign;

    @AfterEach
    void tearDown() {
        userRepository.deleteAllInBatch();
        notificationRepository.deleteAllInBatch();
    }

    @DisplayName("읽지 않은 알림 조회 시나리오")
    @TestFactory
    List<DynamicTest> unreadNotificationTest() {

        User user = userService.save(User.builder().build());
        Notification noticeContent = notificationService.save(Notification.builder()
                .user(user)
                .isRead(false)
                .notificationType(NotificationType.NOTICE)
                .content("NOTICE content")
                .build());
        Notification eventContent = notificationService.save(Notification.builder()
                .user(user)
                .isRead(false)
                .notificationType(NotificationType.EVENT)
                .content("EVENT content")
                .build());

        Notification meetingContent = notificationService.save(Notification.builder()
                .user(user)
                .isRead(false)
                .notificationType(NotificationType.MEETING)
                .content("MEETING content")
                .build());
        return List.of(DynamicTest.dynamicTest("첫번째 페이지를 조회한다.(lastId==null)", () -> {

                    List<Notification> unreadNotifications = notificationService.findUnreadNotifications(user.getId(), null);

                    Assertions.assertThat(unreadNotifications)
                            .extracting(Notification::getId)
                            .containsExactly(
                                    meetingContent.getId(),
                                    eventContent.getId(),
                                    noticeContent.getId()
                            );
                }), DynamicTest.dynamicTest("meetingContent 다음 알림부터 조회한다.", () -> {

                    List<Notification> unreadNotifications = notificationService.findUnreadNotifications(user.getId(), meetingContent.getId());

                    Assertions.assertThat(unreadNotifications)
                            .extracting(Notification::getId)
                            .containsExactly(
                                    eventContent.getId(),
                                    noticeContent.getId()
                            );
                })
        );
    }

    @DisplayName("읽은 알림 조회 시나리오")
    @TestFactory
    List<DynamicTest> readNotificationTest() {

        User user = userService.save(User.builder().build());
        Notification noticeContent = notificationService.save(Notification.builder()
                .user(user)
                .isRead(true)
                .notificationType(NotificationType.NOTICE)
                .content("NOTICE content")
                .build());
        Notification eventContent = notificationService.save(Notification.builder()
                .user(user)
                .isRead(true)
                .notificationType(NotificationType.EVENT)
                .content("EVENT content")
                .build());

        Notification meetingContent = notificationService.save(Notification.builder()
                .user(user)
                .isRead(true)
                .notificationType(NotificationType.MEETING)
                .content("MEETING content")
                .build());
        return List.of(DynamicTest.dynamicTest("첫번째 페이지를 조회한다.(lastId==null)", () -> {

                    List<Notification> unreadNotifications = notificationService.findReadNotifications(user.getId(), null);

                    Assertions.assertThat(unreadNotifications)
                            .extracting(Notification::getId)
                            .containsExactly(
                                    meetingContent.getId(),
                                    eventContent.getId(),
                                    noticeContent.getId()
                            );
                }), DynamicTest.dynamicTest("meetingContent 다음 알림부터 조회한다.", () -> {

                    List<Notification> unreadNotifications = notificationService.findReadNotifications(user.getId(), meetingContent.getId());

                    Assertions.assertThat(unreadNotifications)
                            .extracting(Notification::getId)
                            .containsExactly(
                                    eventContent.getId(),
                                    noticeContent.getId()
                            );
                })
        );
    }
}
