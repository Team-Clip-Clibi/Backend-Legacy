package boot.api.com.clip.api.matching.service;

import com.clip.ApiApplication;
import com.clip.api.matching.controller.dto.MatchingReviewDto;
import com.clip.api.matching.controller.dto.MatchingType;
import com.clip.api.matching.service.UserMatchingReviewService;
import com.clip.api.payment.feign.TossPaymentFeign;
import com.clip.global.config.feign.FeignConfig;
import com.clip.infra.aws.s3.config.S3Config;
import com.clip.infra.aws.s3.S3FCMService;
import com.clip.infra.aws.s3.S3ImgService;
import com.clip.infra.aws.s3.config.S3PathProperties;
import com.clip.infra.fcm.config.FcmConfig;
import com.clip.matching.entity.Mood;
import com.clip.matching.entity.RandomMatching;
import com.clip.matching.entity.RandomMatchingReview;
import com.clip.matching.repository.OnethingMatchingRepository;
import com.clip.matching.repository.OneThingMatchingReviewRepository;
import com.clip.matching.repository.RandomMatchingRepository;
import com.clip.matching.repository.RandomMatchingReviewRepository;
import com.clip.matching.service.MatchingReviewService;
import com.clip.matching.service.MatchingService;
import com.clip.user.entity.User;
import com.clip.user.repository.UserRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import static org.assertj.core.api.Assertions.assertThat;

@ContextConfiguration(classes = ApiApplication.class)
@SpringBootTest
public class UserMatchingReviewServiceTest {
    @Autowired
    private UserMatchingReviewService userMatchingReviewService;
    @Autowired
    private MatchingService matchingService;
    @Autowired
    private MatchingReviewService matchingReviewService;
    @Autowired
    private OnethingMatchingRepository oneThingMatchingRepository;
    @Autowired
    private RandomMatchingRepository randomMatchingRepository;
    @Autowired
    private OneThingMatchingReviewRepository oneThingMatchingReviewRepository;
    @Autowired
    private RandomMatchingReviewRepository randomMatchingReviewRepository;
    @Autowired
    private UserRepository userRepository;
    @MockitoBean
    private S3ImgService s3ImgService;
    @MockitoBean
    private S3Config s3Config;
    @MockitoBean
    private S3FCMService s3FCMService;
    @MockitoBean
    private FcmConfig fcmConfig;
    @MockitoBean
    private FeignConfig feignConfig;
    @MockitoBean
    private TossPaymentFeign tossPaymentFeign;
    @MockitoBean
    private S3PathProperties s3PathProperties;

    @AfterEach
    void tearDown() {
        oneThingMatchingRepository.deleteAllInBatch();
        randomMatchingRepository.deleteAllInBatch();
        oneThingMatchingReviewRepository.deleteAllInBatch();
        randomMatchingReviewRepository.deleteAllInBatch();
        userRepository.deleteAllInBatch();
    }

    @DisplayName("userId와 matchingId로 매칭 리뷰를 작성할 수 있다.")
    @Test
    void saveMatchingReview() {
        //given
        RandomMatching randomMatching = randomMatchingRepository.save(RandomMatching.builder().build());
        User user = userRepository.save(User.builder().build());
        Mood mood = Mood.NEUTRAL;
        String positivePoints = "Positive";
        String negativePoints = "Negative";

        //when
        userMatchingReviewService.saveMatchingReview(user.getId(), randomMatching.getId(), MatchingType.RANDOM,
                MatchingReviewDto.builder()
                        .mood(mood)
                        .positivePoints(positivePoints)
                        .negativePoints(negativePoints)
                        .reviewContent("Review Content")
                        .isMemberAllAttended(true)
                        .noShowMembers("No Show Members")
                        .build()
        );

        //then
        RandomMatchingReview randomMatchingReview = randomMatchingReviewRepository.findRandomMatchingReview(user.getId(), randomMatching.getId()).orElseThrow();
        assertThat(randomMatchingReview)
                .extracting(RandomMatchingReview::getMood, RandomMatchingReview::getPositivePoints,
                        RandomMatchingReview::getNegativePoints, RandomMatchingReview::getReviewContent,
                        RandomMatchingReview::isMemberAllAttended, RandomMatchingReview::getNoShowMembers)
                .containsExactly(mood, positivePoints, negativePoints, "Review Content", true, "No Show Members");

    }
}
