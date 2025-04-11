package com.clip.office.notice.service;

import com.clip.OfficeApplication;
import com.clip.notice.entity.News;
import com.clip.notice.repository.NewsRepository;
import com.clip.office.notice.controller.dto.CreateNewsDto;
import com.clip.office.notice.controller.dto.UpdateNewsDto;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;

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
class NewsServiceTest {

    @MockitoBean
    private RedissonClient redissonClient;

    @Autowired
    private NewsService newsService;

    @Autowired
    private NewsRepository newsRepository;

    @AfterEach
    void tearDown(){
        newsRepository.deleteAllInBatch();
    }

    @DisplayName("새소식을 저장한다.")
    @Test
    void saveNews() {
        // given
        CreateNewsDto createNewsDto = CreateNewsDto.builder()
                .content("새소식 내용")
                .link("00 페이지")
                .exposureDate(LocalDate.of(2023, 10, 1))
                .isExposure(false)
                .build();
        // when
        CreateNewsDto saveNewsDto = newsService.createNews(createNewsDto);

        // then
        assertThat(saveNewsDto).isNotNull();
        assertThat(saveNewsDto).extracting(
                CreateNewsDto::getContent,
                CreateNewsDto::getLink,
                CreateNewsDto::getExposureDate,
                CreateNewsDto::isExposure
        ).containsExactly(
                "새소식 내용",
                "00 페이지",
                LocalDate.of(2023, 10, 1),
                false
        );
    }


    @DisplayName("선택한 새소식 내용을 수정한다.")
    @Test
    void updateNews() {
        // given
        News news = newsRepository.save(
                News.builder()
                        .content("기존 새소식 내용")
                        .link("기존 링크")
                        .exposureDate(LocalDate.of(2023, 10, 1))
                        .isExposure(false)
                        .build()
        );

        // when
        UpdateNewsDto updatedNews = newsService.updateNews(
                news.getId(),
                UpdateNewsDto.builder()
                        .id(news.getId())
                        .content("수정된 새소식 내용")
                        .link("수정된 링크")
                        .exposureDate(LocalDate.of(2023, 11, 1))
                        .isExposure(true)
                        .build()
        );

        // then
        assertThat(updatedNews).isNotNull();
        assertThat(updatedNews).extracting(
                UpdateNewsDto::getContent,
                UpdateNewsDto::getLink,
                UpdateNewsDto::getExposureDate,
                UpdateNewsDto::isExposure
        ).containsExactly(
                "수정된 새소식 내용",
                "수정된 링크",
                LocalDate.of(2023, 11, 1),
                true
        );
    }

    @DisplayName("선택한 소식을 삭제한다.")
    @Test
    void deleteNews() {
        // given
        News news = newsRepository.save(
                News.builder()
                        .content("삭제할 새소식 내용")
                        .link("삭제할 링크")
                        .exposureDate(LocalDate.of(2023, 10, 1))
                        .isExposure(false)
                        .build()
        );

        // when
        newsService.deleteNews(news.getId());

        // then
        assertThat(newsRepository.findById(news.getId())).isEmpty();
    }
}