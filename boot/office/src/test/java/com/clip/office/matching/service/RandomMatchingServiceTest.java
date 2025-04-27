package com.clip.office.matching.service;

import com.clip.OfficeApplication;
import com.clip.infra.aws.s3.S3Config;
import com.clip.infra.aws.s3.S3FCMService;
import com.clip.infra.aws.s3.S3ImgService;
import com.clip.infra.fcm.config.FcmConfig;
import com.clip.matching.entity.RandomDistrict;
import com.clip.matching.entity.RandomMatching;
import com.clip.matching.repository.RandomMatchingRepository;
import com.clip.matching.service.MatchingService;
import com.clip.office.matching.controller.dto.CreateRandomMatchingDto;
import com.clip.office.matching.controller.dto.UpdateRandomMatchingDto;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;


@ContextConfiguration(classes = OfficeApplication.class)
@SpringBootTest
@TestPropertySource(properties = {
        "spring.datasource.redis.host=localhost",
        "spring.datasource.redis.port=6379",
        "spring.security.login.max-fail-count=5",
        "spring.security.login.block-duration-seconds=3600",
        "spring.servlet.multipart.max-file-size=10485760",
        "spring.servlet.multipart.max-request-size=10485760",
        "cloud.aws.credentials.access-key=1010",
        "cloud.aws.credentials.secret-key=1010",
        "cloud.aws.region.static=ap-northeast-2",
        "cloud.aws.s3.bucket=clip-office",
        "cloud.aws.s3.region=ap-northeast-2",

})
class RandomMatchingServiceTest {

    @MockitoBean
    private RedissonClient redissonClient;

    @Autowired
    private RandomMatchingService randomMatchingService;

    @Autowired
    private RandomMatchingRepository randomMatchingRepository;
    @MockitoBean
    private S3ImgService s3ImgService;
    @MockitoBean
    private S3Config s3Config;
    @MockitoBean
    private S3FCMService s3FCMService;
    @MockitoBean
    private FcmConfig fcmConfig;

    @Autowired
    private MatchingService matchingService;

    @AfterEach
    void tearDown() {
        randomMatchingRepository.deleteAllInBatch();
    }

    @DisplayName("모임을 생성할 수 있다.")
    @Test
    void createRandomMatching() {
        // given
        CreateRandomMatchingDto createRandomMatchingDto = CreateRandomMatchingDto.builder()
                .randomDistrict(RandomDistrict.GANGNAM)
                .location("서울특별시 강남구 강남대로 421")
                .restaurantName("쉐이크쉑버거")
                .meetingTime(LocalDateTime.of(2025, 3, 20, 22, 45, 0))
                .build();

        // when
        randomMatchingService.createRandomMatching(createRandomMatchingDto);

        // then
        List<RandomMatching> all = randomMatchingRepository.findAll();
        assertThat(all).hasSize(1); // 저장된 데이터가 정확히 1개인지 확인 (createRandomMatchingDto와 동일한 데이터)

        RandomMatching saved = all.get(0);
        assertThat(saved)
                .extracting(
                        RandomMatching::getRandomDistrict,
                        RandomMatching::getLocation,
                        RandomMatching::getRestaurantName,
                        RandomMatching::getMeetingTime
                )
                .containsExactly(
                        RandomDistrict.GANGNAM,
                        "서울특별시 강남구 강남대로 421",
                        "쉐이크쉑버거",
                        LocalDateTime.of(2025, 3, 20, 22, 45, 0)
                );
    }


    @DisplayName("모임을 수정할 수 있다.")
    @Test
    void updateRandomMatching() {
        // given
        RandomMatching randomMatching = RandomMatching.builder()
                .randomDistrict(RandomDistrict.GANGNAM)
                .location("서울특별시 강남구 강남대로 421")
                .restaurantName("쉐이크쉑버거")
                .meetingTime(LocalDateTime.of(2025, 3, 20, 22, 45, 0))
                .build();

        randomMatchingRepository.save(randomMatching);

        // when
        UpdateRandomMatchingDto updateRandomMatchingDto = UpdateRandomMatchingDto.builder()
                .randomDistrict(RandomDistrict.GANGNAM)
                .location("서울특별시 강남구 테헤란로 123")
                .restaurantName("버거킹")
                .meetingTime(LocalDateTime.of(2025, 3, 21, 22, 45, 0))
                .build();

        randomMatchingService.updateRandomMatching(randomMatching.getId(), updateRandomMatchingDto);

        // then
        RandomMatching updaterandomMatching = matchingService.findRandomMatching(randomMatching.getId());

        assertThat(updaterandomMatching).extracting(
                RandomMatching::getRandomDistrict,
                RandomMatching::getLocation,
                RandomMatching::getRestaurantName,
                RandomMatching::getMeetingTime
        ).containsExactly(
                RandomDistrict.GANGNAM,
                "서울특별시 강남구 테헤란로 123",
                "버거킹",
                LocalDateTime.of(2025, 3, 21, 22, 45, 0)
        );
    }

    @DisplayName("모임을 삭제할 수 있다.")
    @Test
    void deleteRandomMatching() {
        // given
        RandomMatching randomMatching = RandomMatching.builder()
                .randomDistrict(RandomDistrict.GANGNAM)
                .location("서울특별시 강남구 강남대로 421")
                .restaurantName("쉐이크쉑버거")
                .meetingTime(LocalDateTime.of(2025, 3, 20, 22, 45, 0))
                .build();

        randomMatchingRepository.save(randomMatching);

        // when
        randomMatchingService.deleteRandomMatching(randomMatching.getId());

        // then
        Optional<RandomMatching> deletedTestMatching = randomMatchingRepository.findById(randomMatching.getId());
        assertThat(deletedTestMatching).isEmpty();
    }
}