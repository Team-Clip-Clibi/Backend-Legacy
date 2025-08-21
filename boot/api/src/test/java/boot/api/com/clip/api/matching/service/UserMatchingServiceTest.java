package boot.api.com.clip.api.matching.service;

import com.clip.ApiApplication;
import com.clip.api.matching.controller.dto.*;
import com.clip.api.matching.service.UserMatchingService;
import com.clip.api.payment.feign.TossPaymentFeign;
import com.clip.batch.actuator.feign.DiscordFeign;
import com.clip.global.config.feign.TossFeignConfig;
import com.clip.global.exception.NoContentAvailableException;
import com.clip.infra.aws.s3.config.S3Config;
import com.clip.infra.aws.s3.S3FCMService;
import com.clip.infra.aws.s3.S3ImgService;
import com.clip.infra.aws.s3.config.S3PathProperties;
import com.clip.infra.fcm.config.FcmConfig;
import com.clip.matching.entity.*;
import com.clip.matching.repository.*;
import com.clip.order.entity.OneThingOrder;
import com.clip.order.entity.OneThingOrderStatus;
import com.clip.order.entity.RandomOrder;
import com.clip.order.entity.RandomOrderStatus;
import com.clip.order.repository.OneThingOrderRepository;
import com.clip.order.repository.RandomOrderRepository;
import com.clip.user.entity.Job;
import com.clip.user.entity.JobCategory;
import com.clip.user.entity.User;
import com.clip.user.repository.UserJobRepository;
import com.clip.user.repository.UserRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@ContextConfiguration(classes = ApiApplication.class)
@SpringBootTest
public class UserMatchingServiceTest {
    @Autowired
    private UserMatchingService userMatchingService;
    @Autowired
    private OnethingMatchingRepository oneThingMatchingRepository;
    @Autowired
    private RandomMatchingRepository randomMatchingRepository;
    @Autowired
    private UserOneThingMatchingRepository userOneThingMatchingRepository;
    @Autowired
    private UserRandomMatchingRepository userRandomMatchingRepository;
    @Autowired
    private UserMatchingRepository userMatchingRepository;

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private OneThingOrderRepository oneThingOrderRepository;
    @Autowired
    private RandomOrderRepository randomOrderRepository;

    @MockitoBean
    private S3ImgService s3ImgService;
    @MockitoBean
    private S3Config s3Config;
    @MockitoBean
    private S3FCMService s3FCMService;
    @MockitoBean
    private FcmConfig fcmConfig;
    @MockitoBean
    private TossFeignConfig tossFeignConfig;
    @MockitoBean
    private TossPaymentFeign tossPaymentFeign;
    @MockitoBean
    private S3PathProperties s3PathProperties;
    @MockitoBean
    private DiscordFeign discordFeign;
    @Autowired
    private UserJobRepository userJobRepository;

    @AfterEach
    void tearDown() {
        userOneThingMatchingRepository.deleteAllInBatch();
        userRandomMatchingRepository.deleteAllInBatch();
        oneThingOrderRepository.deleteAllInBatch();
        randomOrderRepository.deleteAllInBatch();
        oneThingMatchingRepository.deleteAllInBatch();
        randomMatchingRepository.deleteAllInBatch();
        userRepository.deleteAllInBatch();
    }

    @Test
    @DisplayName("참여할 원띵,랜덤 모임이 없이면 NoContentAvailableException 예외를 반환한다.")
    public void notExistAnyMatchingException() {
        // given
        User user = userRepository.save(User.builder().build());

        // when&then
        Assertions.assertThatThrownBy(() -> userMatchingService.getUserMatchingProgressInfo(MatchingType.ONE_THING, 1L, user.getId()))
                .isInstanceOf(NoContentAvailableException.class);
        Assertions.assertThatThrownBy(() -> userMatchingService.getUserMatchingProgressInfo(MatchingType.RANDOM, 1L, user.getId()))
                .isInstanceOf(NoContentAvailableException.class);
    }

    @Test
    @DisplayName("종료 상태로 변경된 원띵 모임은 조회되지 않는다.")
    public void endedStatusOnethingMatchingRetrieve() {
        // given
        User user = userRepository.save(User.builder().build());
        OneThingMatching oneThingMatching = oneThingMatchingRepository.save(OneThingMatching.builder().build());

        UserOneThingMatching userOnethingMatching = userOneThingMatchingRepository.save(UserOneThingMatching.builder()
                .user(user)
                .oneThingMatching(oneThingMatching)
                .matchingStatus(OneThingMatchingStatus.APPLIED)
                .isEnded(true)
                .build());

        // when
        assertThatThrownBy(() -> userMatchingService.getUserMatchingProgressInfo(MatchingType.ONE_THING, oneThingMatching.getId(), user.getId()))
                .isInstanceOf(NoContentAvailableException.class);
    }

    @Test
    @DisplayName("원띵 모임 id, userId로 원띵 매칭 진행 정보를 조회할 수 있다.")
    public void onethingMatchingRetrieve() {
        // given
        User user1 = userRepository.save(User.builder().nickname("nickname1").build());
        User user2 = userRepository.save(User.builder().nickname("nickname2").build());
        User user3 = userRepository.save(User.builder().nickname("nickname3").build());
        User user4 = userRepository.save(User.builder().nickname("nickname4").build());
        OneThingMatching oneThingMatching = oneThingMatchingRepository.save(OneThingMatching.builder().build());

        userOneThingMatchingRepository.save(UserOneThingMatching.builder()
                .user(user1)
                .oneThingMatching(oneThingMatching)
                .onethingTopic("onethingTopic1")
                .tmi("tmi1")
                .matchingStatus(OneThingMatchingStatus.APPLIED)
                .isEnded(false)
                .build());

        userOneThingMatchingRepository.save(UserOneThingMatching.builder()
                .user(user2)
                .oneThingMatching(oneThingMatching)
                .onethingTopic("onethingTopic2")
                .tmi("tmi2")
                .matchingStatus(OneThingMatchingStatus.APPLIED)
                .isEnded(false)
                .build());

        userOneThingMatchingRepository.save(UserOneThingMatching.builder()
                .user(user3)
                .oneThingMatching(oneThingMatching)
                .onethingTopic("onethingTopic3")
                .tmi("tmi3")
                .matchingStatus(OneThingMatchingStatus.APPLIED)
                .isEnded(false)
                .build());

        userOneThingMatchingRepository.save(UserOneThingMatching.builder()
                .user(user4)
                .oneThingMatching(oneThingMatching)
                .onethingTopic("onethingTopic4")
                .tmi("tmi4")
                .matchingStatus(OneThingMatchingStatus.APPLIED)
                .isEnded(false)
                .build());

        // when
        MatchingProgressInfoDto userMatchingProgressInfo = userMatchingService.getUserMatchingProgressInfo(MatchingType.ONE_THING, oneThingMatching.getId(), user1.getId());

        // then
        assertThat(userMatchingProgressInfo.getNicknameList())
                .containsExactlyInAnyOrder("nickname1", "nickname2","nickname3", "nickname4");
        assertThat(userMatchingProgressInfo.getTmiList())
                .containsExactlyInAnyOrder("tmi1", "tmi2", "tmi3","tmi4");
        assertThat(userMatchingProgressInfo.getNicknameOnethingMap())
                .containsExactlyInAnyOrderEntriesOf(Map.of("nickname1", "onethingTopic1",
                        "nickname2", "onethingTopic2",
                        "nickname3", "onethingTopic3",
                        "nickname4", "onethingTopic4"));
    }

    @Test
    @DisplayName("랜덤 모임 id, userId로 랜덤 매칭 진행 정보를 조회할 수 있다.")
    public void randomMatchingRetrieve() {
        // given
        User user1 = userRepository.save(User.builder().nickname("nickname1").build());
        User user2 = userRepository.save(User.builder().nickname("nickname2").build());
        User user3 = userRepository.save(User.builder().nickname("nickname3").build());
        User user4 = userRepository.save(User.builder().nickname("nickname4").build());
        RandomMatching randomMatching = randomMatchingRepository.save(RandomMatching.builder().build());

        userRandomMatchingRepository.save(UserRandomMatching.builder()
                .user(user1)
                .randomMatching(randomMatching)
                .tmi("tmi1")
                .onethingTopic("onethingTopic1")
                .matchingStatus(RandomMatchingStatus.APPLIED)
                .isEnded(false)
                .build());

        userRandomMatchingRepository.save(UserRandomMatching.builder()
                .user(user2)
                .randomMatching(randomMatching)
                .tmi("tmi2")
                .onethingTopic("onethingTopic2")
                .matchingStatus(RandomMatchingStatus.APPLIED)
                .isEnded(false)
                .build());

        userRandomMatchingRepository.save(UserRandomMatching.builder()
                .user(user3)
                .randomMatching(randomMatching)
                .tmi("tmi3")
                .onethingTopic("onethingTopic3")
                .matchingStatus(RandomMatchingStatus.APPLIED)
                .isEnded(false)
                .build());

        userRandomMatchingRepository.save(UserRandomMatching.builder()
                .user(user4)
                .randomMatching(randomMatching)
                .tmi("tmi4")
                .onethingTopic("onethingTopic4")
                .matchingStatus(RandomMatchingStatus.APPLIED)
                .isEnded(false)
                .build());

        // when
        MatchingProgressInfoDto userMatchingProgressInfo = userMatchingService.getUserMatchingProgressInfo(MatchingType.RANDOM, randomMatching.getId(), user1.getId());

        // then
        assertThat(userMatchingProgressInfo.getNicknameList())
                .containsExactlyInAnyOrder("nickname1", "nickname2","nickname3", "nickname4");
        assertThat(userMatchingProgressInfo.getTmiList())
                .containsExactlyInAnyOrder("tmi1", "tmi2", "tmi3","tmi4");
        assertThat(userMatchingProgressInfo.getNicknameOnethingMap())
                .containsExactlyInAnyOrderEntriesOf(Map.of("nickname1", "onethingTopic1",
                        "nickname2", "onethingTopic2",
                        "nickname3", "onethingTopic3",
                        "nickname4", "onethingTopic4"));
    }

    @DisplayName("userId와 matchingId로 진행중인 매칭을 종료 상태로 업데이트 할 수 있다.")
    @Test
    void updateMatchingStatusCheck() {
        //given
        User user = userRepository.save(User.builder().build());
        OneThingMatching oneThingMatching = oneThingMatchingRepository.save(OneThingMatching.builder().build());
        RandomMatching randomMatching = randomMatchingRepository.save(RandomMatching.builder().build());

        UserOneThingMatching userOneThingMatching = userOneThingMatchingRepository.save(
                UserOneThingMatching.builder()
                        .user(user)
                        .oneThingMatching(oneThingMatching)
                        .build()
        );

        UserRandomMatching userRandomMatching = userRandomMatchingRepository.save(
                UserRandomMatching.builder()
                        .user(user)
                        .randomMatching(randomMatching)
                        .build()
        );

        //when
        userMatchingService.updateMatchingStatusChecked(user.getId(),MatchingType.ONE_THING, userOneThingMatching.getId());
        userMatchingService.updateMatchingStatusChecked(user.getId(),MatchingType.RANDOM, userRandomMatching.getId());

        //then
        assertThat(userOneThingMatchingRepository.findById(userOneThingMatching.getId()).get().isEnded()).isTrue();
        assertThat(userRandomMatchingRepository.findById(userRandomMatching.getId()).get().isEnded()).isTrue();

    }

    @DisplayName("userId로 다음 모임 날짜 및 신청 완료 & 매칭 확정 모임 정보 및 안내문 전체 조회 여부를 반환한다.")
    @Test
    public void getMatchingOverview() {
        //given
        User user = userRepository.save(User.builder().build());
        OneThingMatching oneThingMatching = oneThingMatchingRepository.save(OneThingMatching.builder()
                .dateTime(LocalDateTime.now().plusHours(1).truncatedTo(ChronoUnit.SECONDS))
                .build());
        RandomMatching randomMatching = randomMatchingRepository.save(RandomMatching.builder()
                .dateTime(LocalDateTime.now().plusHours(2).truncatedTo(ChronoUnit.SECONDS))
                .build());

        OneThingOrder oneThingOrder = oneThingOrderRepository.save(
                OneThingOrder.builder()
                        .user(user)
                        .status(OneThingOrderStatus.DONE)
//                        .oneThingMatching(oneThingMatching)
                        .build()
        );

        RandomOrder randomOrder = randomOrderRepository.save(
                RandomOrder.builder()
                        .user(user)
                        .status(RandomOrderStatus.DONE)
                        .randomMatching(randomMatching)
                        .build()
        );

        UserOneThingMatching userOneThingMatching = userOneThingMatchingRepository.save(
                UserOneThingMatching.builder()
                        .user(user)
                        .oneThingMatching(oneThingMatching)
                        .matchingStatus(OneThingMatchingStatus.APPLIED)
                        .oneThingOrder(oneThingOrder)
                        .build()
        );

        UserRandomMatching userRandomMatching = userRandomMatchingRepository.save(
                UserRandomMatching.builder()
                        .user(user)
                        .randomMatching(randomMatching)
                        .matchingStatus(RandomMatchingStatus.CONFIRMED)
                        .build()
        );

        //when
        MatchingOverviewDto matchingOverview = userMatchingService.getMatchingOverview(user.getId());

        //then
        assertThat(matchingOverview.getAppliedMatchingCount()).isEqualTo(1);
        assertThat(matchingOverview.getConfirmedMatchingCount()).isEqualTo(1);
        assertThat(matchingOverview.getIsAllNoticeRead()).isFalse();
        assertThat(matchingOverview.getNextMatchingDate()).isEqualTo(oneThingMatching.getDateTime().toLocalDate());
    }

    @DisplayName("userId로 매칭된 모임들을 상태에 따라 조회한다.")
    @Test
    public void getMatchingByStatus() {
        //given
        User user = userRepository.save(User.builder().build());
        String oneThingContent = "oneThingContent";
        OneThingMatching oneThingMatching = oneThingMatchingRepository.save(OneThingMatching.builder()
                .dateTime(LocalDateTime.now().plusHours(1).truncatedTo(ChronoUnit.SECONDS))
                .build());
        RandomMatching randomMatching = randomMatchingRepository.save(RandomMatching.builder()
                .dateTime(LocalDateTime.now().plusHours(2).truncatedTo(ChronoUnit.SECONDS))
                .build());

        UserOneThingMatching userOneThingMatching = userOneThingMatchingRepository.save(
                UserOneThingMatching.builder()
                        .user(user)
                        .oneThingMatching(oneThingMatching)
                        .matchingStatus(OneThingMatchingStatus.APPLIED)
                        .onethingTopic(oneThingContent)
                        .build()
        );

        UserRandomMatching userRandomMatching = userRandomMatchingRepository.save(
                UserRandomMatching.builder()
                        .user(user)
                        .randomMatching(randomMatching)
                        .matchingStatus(RandomMatchingStatus.CONFIRMED)
                        .onethingTopic(oneThingContent)
                        .build()
        );

        //when
        List<MatchingDto> matchings = userMatchingService.getMatchings(null, null, user.getId());
        List<MatchingDto> appliedMatchings = userMatchingService.getMatchings(RandomMatchingStatus.APPLIED, null, user.getId());
        List<MatchingDto> confirmedMatchings = userMatchingService.getMatchings(RandomMatchingStatus.CONFIRMED, null, user.getId());

        //then
        assertThat(matchings).hasSize(2);
        assertThat(appliedMatchings).hasSize(1);
        assertThat(confirmedMatchings).hasSize(1);
    }

    @DisplayName("userId로 다음 페이지의 매칭이 존재하지 않는 경우에 마지막 매칭 시간으로 조회 시 204 No Content를 반환한다.")
    @Test
    public void getMatchingByLastId() {
        //given
        User user = userRepository.save(User.builder().build());
        String oneThingContent = "oneThingContent";

        // 과거의 매칭 생성 (6개월 이전)
        LocalDateTime oldMeetingTime = LocalDateTime.now().plusMonths(7).truncatedTo(ChronoUnit.SECONDS);
        OneThingMatching oneThingMatching = oneThingMatchingRepository.save(OneThingMatching.builder()
                .dateTime(oldMeetingTime)
                .build());

        UserOneThingMatching userOneThingMatching = userOneThingMatchingRepository.save(
                UserOneThingMatching.builder()
                        .user(user)
                        .oneThingMatching(oneThingMatching)
                        .matchingStatus(OneThingMatchingStatus.APPLIED)
                        .onethingTopic(oneThingContent)
                        .build()
        );

        LocalDateTime lastMatchingDateTime = LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS);

        //when & then
        assertThatThrownBy(() -> userMatchingService.getMatchings(null, lastMatchingDateTime, user.getId()))
                .isInstanceOf(NoContentAvailableException.class);
    }

    @DisplayName("userId로 매칭 안내문을 조회한다.")
    @Test
    void getMatchingNotice() {
        //given
        Job job = userJobRepository.save(Job.builder().jobCategory(JobCategory.ART).build());
        User user1 = userRepository.save(User.builder().job(job).dietaryOption("베지테리언에요").build());
        User user2 = userRepository.save(User.builder().job(job).dietaryOption("비건이에요").build());
        User user3 = userRepository.save(User.builder().job(job).dietaryOption("글루텐프리를 지켜요").build());
        User user4 = userRepository.save(User.builder().job(job).dietaryOption("다 잘먹어요").build());
        User user5 = userRepository.save(User.builder().job(job).dietaryOption("다 잘 안먹어").build());
        User user6 = userRepository.save(User.builder().job(job).dietaryOption("베지테리언에요").build());
        User user7 = userRepository.save(User.builder().job(job).dietaryOption("생선 싫어요").build());

        OneThingMatching oneThingMatching = oneThingMatchingRepository.save(OneThingMatching.builder()
                .address("onethingAddress1")
                .restaurantName("onethingRestaurant1")
                .dateTime(LocalDateTime.now().minusMonths(1))
                .build());
        RandomMatching randomMatching = randomMatchingRepository.save(RandomMatching.builder()
                .restaurantName("randomRestaurant1")
                .address("randomAddress1")
                .dateTime(LocalDateTime.now().minusDays(15))
                .build());

        UserOneThingMatching userOneThingMatching1 = userOneThingMatchingRepository.save(
                UserOneThingMatching.builder()
                        .user(user1)
                        .oneThingMatching(oneThingMatching)
                        .matchingStatus(OneThingMatchingStatus.CONFIRMED)
                        .build()
        );

        UserOneThingMatching userOneThingMatching2 = userOneThingMatchingRepository.save(
                UserOneThingMatching.builder()
                        .user(user2)
                        .oneThingMatching(oneThingMatching)
                        .matchingStatus(OneThingMatchingStatus.CONFIRMED)
                        .build()
        );

        UserOneThingMatching userOneThingMatching3 = userOneThingMatchingRepository.save(
                UserOneThingMatching.builder()
                        .user(user3)
                        .oneThingMatching(oneThingMatching)
                        .matchingStatus(OneThingMatchingStatus.CONFIRMED)
                        .build()
        );

        UserOneThingMatching userOneThingMatching4 = userOneThingMatchingRepository.save(
                UserOneThingMatching.builder()
                        .user(user4)
                        .oneThingMatching(oneThingMatching)
                        .matchingStatus(OneThingMatchingStatus.CONFIRMED)
                        .build()
        );



        UserRandomMatching userRandomMatching1 = userRandomMatchingRepository.save(
                UserRandomMatching.builder()
                        .user(user1)
                        .randomMatching(randomMatching)
                        .matchingStatus(RandomMatchingStatus.CONFIRMED)
                        .build()
        );

        UserRandomMatching userRandomMatching2 = userRandomMatchingRepository.save(
                UserRandomMatching.builder()
                        .user(user5)
                        .randomMatching(randomMatching)
                        .matchingStatus(RandomMatchingStatus.CONFIRMED)
                        .build()
        );

        UserRandomMatching userRandomMatching3 = userRandomMatchingRepository.save(
                UserRandomMatching.builder()
                        .user(user6)
                        .randomMatching(randomMatching)
                        .matchingStatus(RandomMatchingStatus.CONFIRMED)
                        .build()
        );

        UserRandomMatching userRandomMatching4 = userRandomMatchingRepository.save(
                UserRandomMatching.builder()
                        .user(user7)
                        .randomMatching(randomMatching)
                        .matchingStatus(RandomMatchingStatus.CONFIRMED)
                        .build()
        );

        //when
        List<MatchingNoticeDto> response = userMatchingService.getMatchingNotice(null, user1.getId());

        //then
        System.out.println("response = " + response);
        assertThat(response).hasSize(2);
    }

}
