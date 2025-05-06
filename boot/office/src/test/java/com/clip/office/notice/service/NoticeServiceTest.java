package com.clip.office.notice.service;

import com.clip.OfficeApplication;
import com.clip.infra.aws.s3.S3Config;
import com.clip.infra.aws.s3.S3FCMService;
import com.clip.infra.aws.s3.S3ImgService;
import com.clip.infra.fcm.config.FcmConfig;
import com.clip.notice.entity.Notice;
import com.clip.notice.entity.NoticeType;
import com.clip.notice.repository.NoticeRepository;
import com.clip.office.notice.controller.dto.CreateNoticeDto;
import com.clip.office.notice.controller.dto.UpdateNoticeDto;
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
class NoticeServiceTest {

    @MockitoBean
    private RedissonClient redissonClient;

    @Autowired
    private NoticeService noticeService;

    @Autowired
    private NoticeRepository noticeRepository;

    @MockitoBean
    private S3ImgService s3ImgService;

    @MockitoBean
    private S3Config s3Config;

    @MockitoBean
    private S3FCMService s3FCMService;

    @MockitoBean
    private FcmConfig fcmConfig;

    @AfterEach
    void tearDown(){
        noticeRepository.deleteAllInBatch();
    }

    @DisplayName("새소식을 저장한다.")
    @Test
    void saveNotice() {
        // given
        CreateNoticeDto createNoticeDto = CreateNoticeDto.builder()
                .noticeType(NoticeType.NOTICE)
                .content("새소식 내용")
                .link("00 페이지")
                .exposureDate(LocalDate.of(2023, 10, 1))
                .isExposure(false)
                .build();
        // when
        CreateNoticeDto saveNoticeDto = noticeService.createNotice(createNoticeDto);

        // then
        assertThat(saveNoticeDto).isNotNull();
        assertThat(saveNoticeDto).extracting(
                CreateNoticeDto::getNoticeType,
                CreateNoticeDto::getContent,
                CreateNoticeDto::getLink,
                CreateNoticeDto::getExposureDate,
                CreateNoticeDto::isExposure
        ).containsExactly(
                NoticeType.NOTICE,
                "새소식 내용",
                "00 페이지",
                LocalDate.of(2023, 10, 1),
                false
        );
    }

    @DisplayName("새공지사항을 저장한다.")
    @Test
    void saveArticle() {
        // given
        CreateNoticeDto createNoticeDto = CreateNoticeDto.builder()
                .noticeType(NoticeType.ARTICLE)
                .content("새공지사항 내용")
                .link("00 페이지")
                .exposureDate(LocalDate.of(2023, 10, 1))
                .isExposure(false)
                .build();
        // when
        CreateNoticeDto saveNoticeDto = noticeService.createNotice(createNoticeDto);

        // then
        assertThat(saveNoticeDto).isNotNull();
        assertThat(saveNoticeDto).extracting(
                CreateNoticeDto::getNoticeType,
                CreateNoticeDto::getContent,
                CreateNoticeDto::getLink,
                CreateNoticeDto::getExposureDate,
                CreateNoticeDto::isExposure
        ).containsExactly(
                NoticeType.ARTICLE,
                "새공지사항 내용",
                "00 페이지",
                LocalDate.of(2023, 10, 1),
                false
        );
    }


    @DisplayName("선택한 새소식 내용을 수정한다.")
    @Test
    void updateNotice() {
        // given
        Notice notice = noticeRepository.save(
                Notice.builder()
                        .noticeType(NoticeType.NOTICE)
                        .content("기존 새소식 내용")
                        .link("기존 링크")
                        .exposureDate(LocalDate.of(2023, 10, 1))
                        .isExposure(false)
                        .build()
        );

        // when
        UpdateNoticeDto updatedNotice = noticeService.updateNotice(
                notice.getId(),
                UpdateNoticeDto.builder()
                        .id(notice.getId())
                        .noticeType(notice.getNoticeType())
                        .content("수정된 새소식 내용")
                        .link("수정된 링크")
                        .exposureDate(LocalDate.of(2023, 11, 1))
                        .isExposure(true)
                        .build()
        );

        // then
        assertThat(updatedNotice).isNotNull();
        assertThat(updatedNotice).extracting(
                UpdateNoticeDto::getNoticeType,
                UpdateNoticeDto::getContent,
                UpdateNoticeDto::getLink,
                UpdateNoticeDto::getExposureDate,
                UpdateNoticeDto::isExposure
        ).containsExactly(
                NoticeType.NOTICE,
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
        Notice notice = noticeRepository.save(
                Notice.builder()
                        .noticeType(NoticeType.NOTICE)
                        .content("삭제할 새소식 내용")
                        .link("삭제할 링크")
                        .exposureDate(LocalDate.of(2023, 10, 1))
                        .isExposure(false)
                        .build()
        );

        // when
        noticeService.deleteNotice(notice.getId());

        // then
        assertThat(noticeRepository.findById(notice.getId())).isEmpty();
    }
}