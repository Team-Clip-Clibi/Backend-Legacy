package boot.api.com.clip.api.matching.service;

import com.clip.ApiApplication;
import com.clip.api.matching.controller.dto.MatchingProgressStatusDto;
import com.clip.api.matching.controller.dto.MatchingType;
import com.clip.api.matching.service.UserMatchingService;
import com.clip.api.matching.service.exception.NotExistAnyMatchingException;
import com.clip.infra.aws.s3.S3Config;
import com.clip.infra.aws.s3.S3ImgService;
import com.clip.matching.entity.OneThingMatching;
import com.clip.matching.entity.RandomMatching;
import com.clip.matching.entity.UserOneThingMatching;
import com.clip.matching.entity.UserRandomMatching;
import com.clip.matching.repository.*;
import com.clip.user.entity.User;
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

@ContextConfiguration(classes = ApiApplication.class)
@SpringBootTest
public class UserMatchingServiceTest {
    @Autowired
    private UserMatchingService userMatchingService;
    @Autowired
    private OneThingMatchingRepository oneThingMatchingRepository;
    @Autowired
    private RandomMatchingRepository randomMatchingRepository;
    @Autowired
    private UserOneThingMatchingRepository userOneThingMatchingRepository;
    @Autowired
    private UserRandomMatchingRepository userRandomMatchingRepository;

    @Autowired
    private UserRepository userRepository;

    @MockitoBean
    private S3ImgService s3ImgService;
    @MockitoBean
    private S3Config s3Config;

    @AfterEach
    void tearDown() {
        userOneThingMatchingRepository.deleteAllInBatch();
        userRandomMatchingRepository.deleteAllInBatch();
        oneThingMatchingRepository.deleteAllInBatch();
        randomMatchingRepository.deleteAllInBatch();
        userRepository.deleteAllInBatch();
    }

    @Test
    @DisplayName("참여할 원띵,랜덤 모임이 없이면 NotExistAnyMatchingException 예외를 반환한다.")
    public void notExistAnyMatchingException() {
        // given
        User user = userRepository.save(User.builder().build());

        // when&then
        Assertions.assertThatThrownBy(() -> userMatchingService.getUserMatchingStatus(user.getId()))
                .isInstanceOf(NotExistAnyMatchingException.class);
    }

    @Test
    @DisplayName("참여할 원띵, 랜덤이 둘 다 존재하고 진행중인 모임이 없으면 현재와 가장 가까운 모임 시간 정보를 반환한다.")
    public void participateInBothMatches() {
        //given
        LocalDateTime oneThingTime = LocalDateTime.now().plusHours(1);
        LocalDateTime randomTime = LocalDateTime.now().plusHours(2);
        User requester = userRepository.save(User.builder().nickname("requester").build());
        OneThingMatching oneThingMatching = oneThingMatchingRepository.save(OneThingMatching.builder().meetingTime(oneThingTime).build());
        RandomMatching randomMatching = randomMatchingRepository.save(RandomMatching.builder().meetingTime(randomTime).build());

        UserOneThingMatching userOneThingMatching = userOneThingMatchingRepository.save(
                UserOneThingMatching.builder()
                        .user(requester)
                        .myQuizContent("myQuizContent")
                        .myOneThingContent("myOneThingContent")
                        .oneThingMatching(oneThingMatching)
                        .build()
        );

        UserRandomMatching userRandomMatching = userRandomMatchingRepository.save(
                UserRandomMatching.builder()
                        .user(requester)
                        .randomMatching(randomMatching)
                        .build()
        );

        //when
        MatchingProgressStatusDto userMatchingStatus = userMatchingService.getUserMatchingStatus(requester.getId());

        //then
        Assertions.assertThat(userMatchingStatus.getMatchingId()).isEqualTo(userOneThingMatching.getId());
        Assertions.assertThat(userMatchingStatus.getMatchingType()).isEqualTo(MatchingType.ONE_THING);
        Assertions.assertThat(userMatchingStatus.getLatestMatchingDateTime()).isCloseTo(oneThingTime, Assertions.within(1L, ChronoUnit.MILLIS));
    }

    @Test
    @DisplayName("참여할 원띵, 랜덤이 둘 다 존재하고 진행중인 모임이 있으면 진행중인 모임의 정보를 반환한다.")
    public void participatingInBothMatchesAndMeetingsInProgress() {
        //given
        LocalDateTime oneThingTime = LocalDateTime.now();
        LocalDateTime randomTime = LocalDateTime.now().plusHours(2);
        String nickanme = "requester";
        String quizContent = "quizContent";
        String oneThingContent = "oneThingContent";
        User requester = userRepository.save(User.builder().nickname(nickanme).build());
        OneThingMatching oneThingMatching = oneThingMatchingRepository.save(OneThingMatching.builder().meetingTime(oneThingTime).build());
        RandomMatching randomMatching = randomMatchingRepository.save(RandomMatching.builder().meetingTime(randomTime).build());

        userOneThingMatchingRepository.save(
                UserOneThingMatching.builder()
                        .user(requester)
                        .myQuizContent(quizContent)
                        .myOneThingContent(oneThingContent)
                        .oneThingMatching(oneThingMatching)
                        .build()
        );

        userRandomMatchingRepository.save(
                UserRandomMatching.builder()
                        .user(requester)
                        .randomMatching(randomMatching)
                        .build()
        );

        MatchingProgressStatusDto.MatchingProgressInfo progressInfo = MatchingProgressStatusDto.MatchingProgressInfo.builder()
                .nicknameList(List.of(nickanme))
                .oneThingMap(Map.of(nickanme, oneThingContent))
                .quizList(List.of(quizContent))
                .build();

        //when
        MatchingProgressStatusDto userMatchingStatus = userMatchingService.getUserMatchingStatus(requester.getId());

        //then
        Assertions.assertThat(userMatchingStatus.getMatchingProgressInfo()).usingRecursiveComparison().isEqualTo(progressInfo);
    }

    @DisplayName("userId와 matchingId로 진행중인 매칭 정보를 조회 완료한 상태로 업데이트 할 수 있다.")
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
        Assertions.assertThat(userOneThingMatchingRepository.findById(userOneThingMatching.getId()).get().isCheckedMatchingStart()).isTrue();
        Assertions.assertThat(userRandomMatchingRepository.findById(userRandomMatching.getId()).get().isCheckedMatchingStart()).isTrue();

    }
}
