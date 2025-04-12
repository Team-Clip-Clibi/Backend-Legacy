package com.clip.office.matching.service;

import com.clip.OfficeApplication;
import com.clip.infra.aws.s3.S3Config;
import com.clip.infra.aws.s3.S3ImgService;
import com.clip.matching.entity.RandomDistrict;
import com.clip.matching.entity.RandomMatching;
import com.clip.matching.repository.RandomMatchingRepository;
import com.clip.office.matching.controller.dto.CreateRandomMatchingDto;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

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
        CreateRandomMatchingDto savedTestDto = randomMatchingService.createRandomMatching(createRandomMatchingDto);

        // then
        Optional<RandomMatching> savedTestMatching = randomMatchingRepository.findAll().stream().findFirst();
        assertThat(savedTestMatching).isPresent();

        RandomMatching randomMatching = savedTestMatching.get();
        assertThat(randomMatching.getId()).isNotNull();
        assertThat(randomMatching).extracting(
                RandomMatching::getRandomDistrict,
                RandomMatching::getLocation,
                RandomMatching::getRestaurantName,
                RandomMatching::getMeetingTime
        ).containsExactly(
                RandomDistrict.GANGNAM,
                "서울특별시 강남구 강남대로 421",
                "쉐이크쉑버거",
                LocalDateTime.of(2025, 3, 20, 22, 45, 0)
        );

        assertThat(savedTestDto).extracting(
                CreateRandomMatchingDto::getRandomDistrict,
                CreateRandomMatchingDto::getLocation,
                CreateRandomMatchingDto::getRestaurantName,
                CreateRandomMatchingDto::getMeetingTime
        ).containsExactly(
                RandomDistrict.GANGNAM,
                "서울특별시 강남구 강남대로 421",
                "쉐이크쉑버거",
                LocalDateTime.of(2025, 3, 20, 22, 45, 0)
        );
    }
}