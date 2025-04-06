package com.clip.office.matching.service;

import com.clip.OfficeApplication;
import com.clip.matching.entity.RandomMatching;
import com.clip.matching.repository.RandomMatchingRepository;
import com.clip.office.matching.controller.dto.CreateRandomMatchingDto;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;

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
        "cloud.aws.credentials.access-key=1010",
        "cloud.aws.credentials.secret-key=1010",
        "cloud.aws.region.static=ap-northeast-2",
        "cloud.aws.s3.bucket=clip-office",
})
class RandomMatchingServiceTest {

    @Autowired
    private RandomMatchingService randomMatchingService;

    @Autowired
    private RandomMatchingRepository randomMatchingRepository;


    @AfterEach
    void tearDown() {
        randomMatchingRepository.deleteAllInBatch();
    }

    @DisplayName("모임을 생성할 수 있다.")
    @Test
    void createRandomMatching() {
        // given
        CreateRandomMatchingDto createRandomMatchingDto = CreateRandomMatchingDto.builder()
                .location("부산광역시 금정구 부산대학로63번길 21")
                .restaurantName("톤쇼우")
                .meetingTime(LocalDateTime.of(2025, 3, 20, 22, 45, 0))
                .build();

        // when
        CreateRandomMatchingDto savedTestDto = randomMatchingService.createRandomMatching(createRandomMatchingDto);

        // then
        Optional<RandomMatching> savedTestMatching = randomMatchingRepository.findAll().stream().findFirst();
        assertThat(savedTestMatching).isPresent();

        RandomMatching randomMatching = savedTestMatching.get();
        assertThat(randomMatching.getId()).isNotNull();
        assertThat(randomMatching.getLocation()).isEqualTo("부산광역시 금정구 부산대학로63번길 21");
        assertThat(randomMatching.getRestaurantName()).isEqualTo("톤쇼우");
        assertThat(randomMatching.getMeetingTime()).isEqualTo(LocalDateTime.of(2025, 3, 20, 22, 45, 0));


        assertThat(savedTestDto.getLocation()).isEqualTo("부산광역시 금정구 부산대학로63번길 21");
        assertThat(savedTestDto.getRestaurantName()).isEqualTo("톤쇼우");
        assertThat(savedTestDto.getMeetingTime()).isEqualTo(LocalDateTime.of(2025, 3, 20, 22, 45, 0));
    }
}