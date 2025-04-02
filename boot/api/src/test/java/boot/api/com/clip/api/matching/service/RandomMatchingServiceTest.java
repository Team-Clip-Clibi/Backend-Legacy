package boot.api.com.clip.api.matching.service;

import com.clip.ApiApplication;
import com.clip.api.matching.controller.dto.CreateRandomMatchingDto;
import com.clip.api.matching.service.RandomMatchingService;
import com.clip.matching.entity.RandomMatching;
import com.clip.matching.repository.RandomMatchingRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@ContextConfiguration(classes = ApiApplication.class)
@SpringBootTest
class RandomMatchingServiceTest {

    @Autowired
    private RandomMatchingService randomMatchingService;

    @Autowired
    private RandomMatchingRepository randomMatchingRepository;

    @AfterEach
    void tearDown() {
        randomMatchingRepository.deleteAllInBatch();
    }

    @DisplayName("모임 생성 테스트")
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
