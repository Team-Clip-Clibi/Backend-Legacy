package boot.api.com.clip.api.matching.service;

import com.clip.ApiApplication;
import com.clip.api.matching.controller.dto.RandomMatchingDuplicateCheckDto;
import com.clip.api.matching.controller.dto.RandomMatchingOrderDto;
import com.clip.api.matching.service.RandomMatchingOrderService;
import com.clip.api.payment.feign.TossPaymentFeign;
import com.clip.global.config.feign.TossFeignConfig;
import com.clip.infra.aws.s3.config.S3Config;
import com.clip.infra.aws.s3.S3FCMService;
import com.clip.infra.aws.s3.S3ImgService;
import com.clip.infra.aws.s3.config.S3PathProperties;
import com.clip.infra.fcm.config.FcmConfig;
import com.clip.matching.entity.*;
import com.clip.matching.repository.RandomMatchingCapacityRepository;
import com.clip.matching.repository.RandomMatchingRepository;
import com.clip.matching.repository.UserRandomMatchingRepository;
import com.clip.order.repository.RandomOrderRepository;
import com.clip.price.entity.*;
import com.clip.price.repository.RandomDiscountRepository;
import com.clip.price.repository.RandomPriceRepository;
import com.clip.price.service.RandomDiscountService;
import com.clip.price.service.RandomPriceService;
import com.clip.user.entity.User;
import com.clip.user.repository.UserRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import java.math.BigDecimal;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static org.assertj.core.api.Assertions.assertThat;;

@ContextConfiguration(classes = ApiApplication.class)
@SpringBootTest
public class RandomMatchingOrderServiceTest {
    @MockitoBean
    private S3ImgService s3ImgService;
    @MockitoBean
    private S3Config s3Config;
    @MockitoBean
    private TossFeignConfig tossFeignConfig;
    @MockitoBean
    private TossPaymentFeign tossPaymentFeign;
    @MockitoBean
    private S3FCMService s3FCMService;
    @MockitoBean
    private FcmConfig fcmConfig;
    @MockitoBean
    private S3PathProperties s3PathProperties;

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private RandomMatchingCapacityRepository randomMatchingCapacityRepository;
    @Autowired
    private UserRandomMatchingRepository userRandomMatchingRepository;
    @Autowired
    private RandomOrderRepository randomOrderRepository;
    @Autowired
    private RandomMatchingRepository randomMatchingRepository;
    @Autowired
    private RandomMatchingOrderService randomMatchingOrderService;
    @Autowired
    private RandomPriceService randomPriceService;
    @Autowired
    private RandomDiscountService randomDiscountService;
    @Autowired
    private RandomPriceRepository randomPriceRepository;
    @Autowired
    private RandomDiscountRepository randomDiscountRepository;

    @AfterEach
    void tearDown() {
        randomMatchingCapacityRepository.deleteAllInBatch();
        userRandomMatchingRepository.deleteAllInBatch();
        randomOrderRepository.deleteAllInBatch();
        userRepository.deleteAllInBatch();
        randomMatchingRepository.deleteAllInBatch();
        randomPriceRepository.deleteAllInBatch();
        randomDiscountRepository.deleteAllInBatch();
    }

    @Nested
    @DisplayName("createOrder 메서드 테스트")
    class CreateOrderTests {

        @Test
        @DisplayName("여러 지역에서 다수 사용자가 동시에 매칭을 신청할 때 정상 처리되는 사용자와 정상 처리되지 않는 사용자의 매칭 신청을 확인한다.")
        void shouldHandleConcurrentMatchingRequests() throws InterruptedException {
            LocalDate now = LocalDate.now();
            DayOfWeek currentDay = now.getDayOfWeek();
            LocalDate matchingDate = (currentDay == DayOfWeek.THURSDAY || currentDay == DayOfWeek.FRIDAY)
                    ? now.plusWeeks(1).with(DayOfWeek.FRIDAY)
                    : now.with(TemporalAdjusters.nextOrSame(DayOfWeek.FRIDAY));
            LocalDateTime meetingDateTime = matchingDate.atTime(19, 0);

            // 2개 지역의 랜덤 매칭 생성
            RandomMatchingCapacity gangnamCapacity = randomMatchingCapacityRepository.save(
                    new RandomMatchingCapacity(new RandomMatching(RandomDistrict.GANGNAM, "역삼역", "강남 맛집", meetingDateTime, 20), 20));
            RandomMatchingCapacity hongdaeCapacity = randomMatchingCapacityRepository.save(
                    new RandomMatchingCapacity(new RandomMatching(RandomDistrict.HONGDAE_HAPJEONG, "홍대입구역", "홍대 맛집", meetingDateTime, 20), 20));

            int threadCount = 400;
            ExecutorService executorService = Executors.newFixedThreadPool(threadCount);
            CountDownLatch latch = new CountDownLatch(threadCount);

            RandomPrice basicRandomPrice = randomPriceRepository.save(
                    new RandomPrice(BigDecimal.valueOf(2900), OneThingPriceType.BASIC));
            RandomDiscount baseDiscount = randomDiscountRepository.save(
                    new RandomDiscount(BigDecimal.valueOf(0), DiscountUnit.AMOUNT, DiscountType.BASE));

            List<RandomMatchingOrderDto.Response> successResponses = Collections.synchronizedList(new ArrayList<>());
            List<Exception> exceptions = Collections.synchronizedList(new ArrayList<>());

            // When
            // 40명이 동시에 매칭 신청
            for (int i = 0; i < threadCount; i++) {
                final int index = i;
                executorService.submit(() -> {
                    try {
                        User user = User.builder()
                                .nickname("User" + index)
                                .build();
                        User usr = userRepository.save(user);


                        try {
                            // 사용자별로 선호하는 지역 다르게 설정 (20명씩 강남/홍대)
                            RandomDistrict district = index < 200
                                    ? RandomDistrict.GANGNAM
                                    : RandomDistrict.HONGDAE_HAPJEONG;

                            RandomMatchingOrderDto.Request request = RandomMatchingOrderDto.Request.builder()
                                    .district(district)
                                    .topic("테스트 주제 " + index)
                                    .build();

                            // 서비스 메서드 호출
                            RandomMatchingOrderDto.Response response = randomMatchingOrderService.createOrder(usr.getId(), request);
                            successResponses.add(response);
                        } catch (Exception e) {
                            exceptions.add(e);
                        }

                    } catch (Exception e) {
                        exceptions.add(e);
                    } finally {
                        latch.countDown();
                    }
                });
            }

            latch.await();
            executorService.shutdown();

            // Then
            // 총 처리된 요청은 12개여야 함 (강남 6명 + 홍대 6명)
            assertThat(successResponses).hasSize(40);

            // 실패한 요청은 28개여야 함
            assertThat(exceptions).hasSize(360);

            // 강남 매칭과 홍대 매칭의 가용 인원이 모두 0이 되어야 함
            RandomMatchingCapacity updatedGangnamCapacity = randomMatchingCapacityRepository.findById(gangnamCapacity.getId()).orElseThrow();
            RandomMatchingCapacity updatedHongdaeCapacity = randomMatchingCapacityRepository.findById(hongdaeCapacity.getId()).orElseThrow();

            assertThat(updatedGangnamCapacity.getAvailableCapacity()).isEqualTo(0);
            assertThat(updatedHongdaeCapacity.getAvailableCapacity()).isEqualTo(0);

            // 강남과 홍대 각각 6명씩 매칭되었는지 확인
            int gangnamMatchCount = (int) userRandomMatchingRepository.findAll().stream()
                    .filter(urm -> urm.getRandomMatching().getId().equals(gangnamCapacity.getRandomMatching().getId()))
                    .count();
            int hongdaeMatchCount = (int) userRandomMatchingRepository.findAll().stream()
                    .filter(urm -> urm.getRandomMatching().getId().equals(hongdaeCapacity.getRandomMatching().getId()))
                    .count();

            assertThat(gangnamMatchCount).isEqualTo(20);
            assertThat(hongdaeMatchCount).isEqualTo(20);
        }
    }

    @Nested
    @DisplayName("checkDuplicateMatching 메서드 테스트")
    class CheckDuplicateMatchingTests {

        @Test
        @DisplayName("이미 매칭에 참여한 사용자는 중복으로 확인되어야 한다")
        void shouldReturnTrueWhenUserAlreadyParticipated() {
            // Given
            User user = userRepository.save(User.builder().nickname("TestUser").build());
            LocalDate now = LocalDate.now();
            DayOfWeek currentDay = now.getDayOfWeek();
            LocalDate matchingDate = (currentDay == DayOfWeek.THURSDAY || currentDay == DayOfWeek.FRIDAY)
                    ? now.plusWeeks(1).with(DayOfWeek.FRIDAY)
                    : now.with(TemporalAdjusters.nextOrSame(DayOfWeek.FRIDAY));
            LocalDateTime meetingDateTime = matchingDate.atTime(19, 0);

            RandomMatching randomMatching = new RandomMatching(RandomDistrict.GANGNAM, "역삼역", "강남 맛집", meetingDateTime, 6);
            RandomMatchingCapacity gangnamCapacity = randomMatchingCapacityRepository.save(
                    new RandomMatchingCapacity(randomMatching, 6));

            UserRandomMatching userRandomMatching = UserRandomMatching.builder()
                    .user(user)
                    .randomMatching(randomMatching)
                    .onethingTopic("테스트 주제")
                    .matchingStatus(RandomMatchingStatus.CONFIRMED)
                    .build();

            userRandomMatchingRepository.save(userRandomMatching);

            // When
            RandomMatchingDuplicateCheckDto result = randomMatchingOrderService.checkDuplicateMatching(user.getId());

            // Then
            assertThat(result.getIsDuplicated()).isTrue();
            assertThat(result.getMeetingTime()).isEqualTo(meetingDateTime);
        }

        @Test
        @DisplayName("매칭에 참여하지 않은 사용자는 중복이 아니어야 한다")
        void shouldReturnFalseWhenUserNotParticipated() {
            // Given
            User user = userRepository.save(User.builder().nickname("NewUser").build());
            LocalDate now = LocalDate.now();
            DayOfWeek currentDay = now.getDayOfWeek();
            LocalDate matchingDate = (currentDay == DayOfWeek.THURSDAY || currentDay == DayOfWeek.FRIDAY)
                    ? now.plusWeeks(1).with(DayOfWeek.FRIDAY)
                    : now.with(TemporalAdjusters.nextOrSame(DayOfWeek.FRIDAY));
            LocalDateTime meetingDateTime = matchingDate.atTime(19, 0);

            // When
            RandomMatchingDuplicateCheckDto result = randomMatchingOrderService.checkDuplicateMatching(user.getId());

            // Then
            assertThat(result.getIsDuplicated()).isFalse();
            assertThat(result.getMeetingTime()).isEqualTo(meetingDateTime);
        }
    }
}
