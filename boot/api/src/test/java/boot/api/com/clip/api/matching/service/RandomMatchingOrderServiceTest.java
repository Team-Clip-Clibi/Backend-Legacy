package boot.api.com.clip.api.matching.service;

import com.clip.ApiApplication;
import com.clip.api.matching.controller.dto.RandomMatchingOrderDto;
import com.clip.api.matching.service.RandomMatchingOrderService;
import com.clip.api.payment.feign.TossPaymentFeign;
import com.clip.global.config.feign.FeignConfig;
import com.clip.infra.aws.s3.S3Config;
import com.clip.infra.aws.s3.S3FCMService;
import com.clip.infra.aws.s3.S3ImgService;
import com.clip.infra.fcm.config.FcmConfig;
import com.clip.matching.entity.RandomDistrict;
import com.clip.matching.entity.RandomMatching;
import com.clip.matching.entity.RandomMatchingCapacity;
import com.clip.matching.repository.RandomMatchingCapacityRepository;
import com.clip.matching.repository.RandomMatchingRepository;
import com.clip.matching.repository.UserRandomMatchingRepository;
import com.clip.order.repository.RandomOrderRepository;
import com.clip.user.entity.User;
import com.clip.user.repository.UserRepository;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionTemplate;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@ContextConfiguration(classes = ApiApplication.class)
@SpringBootTest
public class RandomMatchingOrderServiceTest {
    @MockitoBean
    private S3ImgService s3ImgService;
    @MockitoBean
    private S3Config s3Config;
    @MockitoBean
    private FeignConfig feignConfig;
    @MockitoBean
    private TossPaymentFeign tossPaymentFeign;
    @MockitoBean
    private S3FCMService s3FCMService;
    @MockitoBean
    private FcmConfig fcmConfig;

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
    private ApplicationContext applicationContext;

    @AfterEach
    void tearDown() {
        randomOrderRepository.deleteAllInBatch();
        randomMatchingRepository.deleteAllInBatch();
        userRandomMatchingRepository.deleteAllInBatch();
        randomMatchingCapacityRepository.deleteAllInBatch();
        userRepository.deleteAllInBatch();
    }

    @Nested
    @DisplayName("createOrder 메서드 테스트")
    class CreateOrderTests {

        @Test
        @DisplayName("여러 지역에서 다수 사용자가 동시에 매칭을 신청할 때 정상 처리되는 사용자와 정상 처리되지 않는 사용자의 매칭 신청을 확인한다.")
        void shouldHandleConcurrentMatchingRequests() throws InterruptedException {
            // 2개 지역의 랜덤 매칭 생성
            RandomMatching gangnamMatching = randomMatchingRepository.save(new RandomMatching(RandomDistrict.GANGNAM, "역삼역", "강남 맛집", LocalDateTime.now().plusDays(1)));
            RandomMatching hongdaeMatching = randomMatchingRepository.save(new RandomMatching(RandomDistrict.HONGDAE_HAPJEONG, "홍대입구역", "홍대 맛집", LocalDateTime.now().plusDays(1)));

            EntityManager entityManager = applicationContext.getBean(EntityManager.class);
            entityManager.flush();
            entityManager.clear();

            RandomMatchingCapacity gangnamCapacity = randomMatchingCapacityRepository.save(
                    new RandomMatchingCapacity(randomMatchingRepository.findById(gangnamMatching.getId()).get(), 6));
            RandomMatchingCapacity hongdaeCapacity = randomMatchingCapacityRepository.save(
                    new RandomMatchingCapacity(randomMatchingRepository.findById(hongdaeMatching.getId()).get(), 6));

            int threadCount = 40;
            ExecutorService executorService = Executors.newFixedThreadPool(threadCount);
            CountDownLatch latch = new CountDownLatch(threadCount);

            List<RandomMatchingOrderDto.Response> successResponses = Collections.synchronizedList(new ArrayList<>());
            List<Exception> exceptions = Collections.synchronizedList(new ArrayList<>());

            // When
            // 40명이 동시에 매칭 신청
            for (int i = 0; i < threadCount; i++) {
                final int index = i;
                executorService.submit(() -> {
                    try {
                        // 각 스레드에서 새로운 트랜잭션 시작
                        User user = User.builder()
                                .nickname("User")
                                .build();
                        User usr = userRepository.save(user);


                            try {
                                // 사용자별로 선호하는 지역 다르게 설정 (20명씩 강남/홍대)
                                List<RandomDistrict> districts = index < 20
                                        ? List.of(RandomDistrict.GANGNAM)
                                        : List.of(RandomDistrict.HONGDAE_HAPJEONG);

                                RandomMatchingOrderDto.Request request = RandomMatchingOrderDto.Request.builder()
                                        .districts(districts)
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
            assertThat(successResponses).hasSize(12);

            // 실패한 요청은 28개여야 함
            assertThat(exceptions).hasSize(28);

            // 강남 매칭과 홍대 매칭의 가용 인원이 모두 0이 되어야 함
            RandomMatchingCapacity updatedGangnamCapacity = randomMatchingCapacityRepository.findById(gangnamCapacity.getId()).orElseThrow();
            RandomMatchingCapacity updatedHongdaeCapacity = randomMatchingCapacityRepository.findById(hongdaeCapacity.getId()).orElseThrow();

            assertThat(updatedGangnamCapacity.getAvailableCapacity()).isEqualTo(0);
            assertThat(updatedHongdaeCapacity.getAvailableCapacity()).isEqualTo(0);

            // 강남과 홍대 각각 6명씩 매칭되었는지 확인
            int gangnamMatchCount = (int) userRandomMatchingRepository.findAll().stream()
                    .filter(urm -> urm.getRandomMatching().getId().equals(gangnamMatching.getId()))
                    .count();
            int hongdaeMatchCount = (int) userRandomMatchingRepository.findAll().stream()
                    .filter(urm -> urm.getRandomMatching().getId().equals(hongdaeMatching.getId()))
                    .count();

            assertThat(gangnamMatchCount).isEqualTo(6);
            assertThat(hongdaeMatchCount).isEqualTo(6);
        }
    }
}
