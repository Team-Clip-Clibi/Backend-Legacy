package matching;

import com.clip.OfficeApplication;
import com.clip.global.config.RedissonConfig;
import com.clip.global.security.util.DistributeLockService;
import com.clip.infra.aws.s3.S3FCMService;
import com.clip.infra.aws.s3.S3ImgService;
import com.clip.infra.aws.s3.config.S3Config;
import com.clip.infra.aws.s3.config.S3PathProperties;
import com.clip.infra.fcm.config.FcmConfig;
import com.clip.matching.entity.OneThingMatching;
import com.clip.matching.entity.UserOneThingMatching;
import com.clip.matching.repository.OnethingMatchingRepository;
import com.clip.matching.repository.UserOneThingMatchingRepository;
import com.clip.office.matching.controller.dto.RegisterOnethingParticipantDto;
import com.clip.office.matching.service.AdminMatchingService;
import com.clip.user.entity.User;
import com.clip.user.repository.UserRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

import static org.assertj.core.api.Assertions.*;

@ContextConfiguration(classes = OfficeApplication.class)
@SpringBootTest
public class MatchingTest {

    @MockitoBean
    private RedissonConfig redissonConfig;
    @MockitoBean
    private DistributeLockService distributeLockService;
    @MockitoBean
    private S3ImgService s3ImgService;
    @MockitoBean
    private S3Config s3Config;
    @MockitoBean
    private S3PathProperties s3PathProperties;
    @MockitoBean
    private S3FCMService s3FCMService;
    @MockitoBean
    private FcmConfig fcmConfig;

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private OnethingMatchingRepository onethingMatchingRepository;
    @Autowired
    private UserOneThingMatchingRepository userOneThingMatchingRepository;
    @Autowired
    private AdminMatchingService adminMatchingService;

    @DisplayName("매칭에 참여한 유저는 다른 매칭에 등록할 수 없다.")
    @Test
    void userOnethingMatchingJoinMultipleMatchings() throws InterruptedException {
        // given
        int threadCnt = 5;
        AtomicInteger atomicInteger = new AtomicInteger(0);
        CountDownLatch countDownLatch = new CountDownLatch(threadCnt);
        ExecutorService executorService = Executors.newFixedThreadPool(threadCnt);
        User user = userRepository.save(User.builder().build());
        OneThingMatching oneThingMatching1 = onethingMatchingRepository.save(OneThingMatching.builder().build());
        OneThingMatching oneThingMatching2 = onethingMatchingRepository.save(OneThingMatching.builder().build());
        UserOneThingMatching userOneThingMatching = userOneThingMatchingRepository.save(UserOneThingMatching.builder().user(user).build());

        // when
        for (int i = 1; i <= threadCnt; i++) {
            int idx = i;
            executorService.execute(() -> {
                try {
                    adminMatchingService.registerOnethingMatchingParticipants(
                            new RegisterOnethingParticipantDto(
                                    idx == 1 ? oneThingMatching1.getId() : oneThingMatching2.getId(),
                                    List.of(userOneThingMatching.getId())
                            )
                    );
                }catch (Exception e){
                    atomicInteger.incrementAndGet();
                }finally {
                    countDownLatch.countDown();
                }
            });
        }

        countDownLatch.await();
        executorService.shutdown();

        // then
        assertThat(atomicInteger.get())
                .isEqualTo(threadCnt - 1);
    }
}
