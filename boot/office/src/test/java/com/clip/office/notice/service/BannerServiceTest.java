package com.clip.office.notice.service;

import com.clip.OfficeApplication;
import com.clip.infra.aws.s3.S3Config;
import com.clip.infra.aws.s3.S3FCMService;
import com.clip.infra.aws.s3.S3ImgService;
import com.clip.infra.fcm.config.FcmConfig;
import com.clip.notice.entity.BannerType;
import com.clip.notice.repository.BannerRepository;
import com.clip.office.notice.controller.dto.CreateBannerDto;
import com.clip.office.notice.controller.dto.UpdateBannerDto;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ContextConfiguration(classes = OfficeApplication.class)
@SpringBootTest
@TestPropertySource(properties = {
        "spring.datasource.redis.host=localhost",
        "spring.datasource.redis.port=6379",
        "spring.security.login.max-fail-count=5",
        "spring.security.login.block-duration-seconds=3600"
})
class BannerServiceTest {

    @MockitoBean
    private RedissonClient redissonClient;

    @Autowired
    private BannerService bannerService;

    @Autowired
    private BannerRepository bannerRepository;

    @MockitoBean
    private S3ImgService s3ImgService;

    @MockitoBean
    private S3Config s3Config;

    @MockitoBean
    private S3FCMService s3FCMService;

    @MockitoBean
    private FcmConfig fcmConfig;

    @AfterEach
    void tearDown() {
        bannerRepository.deleteAllInBatch();
    }

    @DisplayName("office에서 사진과 배너 정보를 입력하면 배너를 생성할 수 있다.")
    @Test
    void createBanner() {
        // given
        MockMultipartFile mockMultipartFile = new MockMultipartFile(
                "image",
                "test.jpg",
                MediaType.IMAGE_JPEG_VALUE,
                "test image content".getBytes()
        );

        CreateBannerDto createBannerDto = CreateBannerDto.builder()
                .bannerType(BannerType.HOME)
                .head("배너 헤드")
                .sub("배너 서브")
                .exposureDate(LocalDate.of(2025, 3, 20))
                .isExposure(true)
                .build();

        // S3Service 모킹
        when(s3ImgService.imageUpload(any(MultipartFile.class)))
                .thenReturn("ff/test.jpg");

        // when
        CreateBannerDto savedBanner = bannerService.createBanner(createBannerDto, mockMultipartFile);

        // then
        assertThat(savedBanner).isNotNull();
        assertThat(savedBanner).extracting(
                CreateBannerDto::getHead,
                CreateBannerDto::getSub,
                CreateBannerDto::getImageUrl,
                CreateBannerDto::getBannerType,
                CreateBannerDto::getExposureDate,
                CreateBannerDto::isExposure
        ).containsExactly(
                "배너 헤드",
                "배너 서브",
                "ff/test.jpg",
                BannerType.HOME,
                LocalDate.of(2025, 3, 20),
                true
        );
    }

    @DisplayName("선택한 배너를 수정한다.")
    @Test
    void updateBanner() {
        // given
        MockMultipartFile mockMultipartFile = new MockMultipartFile(
                "image",
                "test.jpg",
                MediaType.IMAGE_JPEG_VALUE,
                "test image content".getBytes()
        );

        CreateBannerDto createBannerDto = CreateBannerDto.builder()
                .bannerType(BannerType.HOME)
                .head("배너 헤드")
                .sub("배너 서브")
                .exposureDate(LocalDate.of(2025, 3, 20))
                .isExposure(true)
                .build();

        // S3Service 모킹
        when(s3ImgService.imageUpload(any(MultipartFile.class)))
                .thenReturn("ff/test.jpg");

        // 배너 생성
        CreateBannerDto savedBanner = bannerService.createBanner(createBannerDto, mockMultipartFile);

        // when
        UpdateBannerDto updatedBanner = bannerService.updateBanner(
                savedBanner.getId(),
                UpdateBannerDto.builder()
                        .bannerType(BannerType.HOME)
                        .head("수정된 배너 헤드")
                        .sub("수정된 배너 서브")
                        .exposureDate(LocalDate.of(2025, 4, 20))
                        .isExposure(false)
                        .build(),
                mockMultipartFile
        );

        // then
        assertThat(updatedBanner).isNotNull();
        assertThat(updatedBanner).extracting(
                UpdateBannerDto::getBannerType,
                UpdateBannerDto::getHead,
                UpdateBannerDto::getSub,
                UpdateBannerDto::getImageUrl,
                UpdateBannerDto::getExposureDate,
                UpdateBannerDto::isExposure
        ).containsExactly(
                BannerType.HOME,
                "수정된 배너 헤드",
                "수정된 배너 서브",
                "ff/test.jpg",
                LocalDate.of(2025, 4, 20),
                false
        );
    }

    @DisplayName("선택한 배너의 사진만 수정한다.")
    @Test
    void updateBannerImage() {
        // given
        MockMultipartFile originalImage = new MockMultipartFile(
                "image",
                "original.jpg",
                MediaType.IMAGE_JPEG_VALUE,
                "original image content".getBytes()
        );

        CreateBannerDto createBannerDto = CreateBannerDto.builder()
                .bannerType(BannerType.HOME)
                .head("배너 헤드")
                .sub("배너 서브")
                .exposureDate(LocalDate.of(2025, 3, 20))
                .isExposure(true)
                .build();

        // 최초 업로드 이미지 URL
        when(s3ImgService.imageUpload(any(MultipartFile.class)))
                .thenReturn("ff/original.jpg");

        CreateBannerDto savedBanner = bannerService.createBanner(createBannerDto, originalImage);

        // 새로운 이미지로 업데이트
        MockMultipartFile updatedImage = new MockMultipartFile(
                "image",
                "updated.jpg",
                MediaType.IMAGE_JPEG_VALUE,
                "updated image content".getBytes()
        );

        // 이미지 변경 시 업로드된 URL
        when(s3ImgService.imageUpload(any(MultipartFile.class)))
                .thenReturn("ff/updated.jpg");

        // when
        UpdateBannerDto updatedBanner = bannerService.updateBanner(
                savedBanner.getId(),
                UpdateBannerDto.builder()
                        .bannerType(savedBanner.getBannerType())  // 기존 값 유지
                        .head(savedBanner.getHead())
                        .sub(savedBanner.getSub())
                        .exposureDate(savedBanner.getExposureDate())
                        .isExposure(savedBanner.isExposure())
                        .build(),
                updatedImage
        );

        // then
        assertThat(updatedBanner).isNotNull();
        assertThat(updatedBanner).extracting(
                UpdateBannerDto::getBannerType,
                UpdateBannerDto::getHead,
                UpdateBannerDto::getSub,
                UpdateBannerDto::getImageUrl,
                UpdateBannerDto::getExposureDate,
                UpdateBannerDto::isExposure
        ).containsExactly(
                savedBanner.getBannerType(),
                savedBanner.getHead(),
                savedBanner.getSub(),
                "ff/updated.jpg",
                savedBanner.getExposureDate(),
                savedBanner.isExposure()
        );
    }

    @DisplayName("선택한 배너를 삭제한다.")
    @Test
    void deleteBanner() {
        // given
        MockMultipartFile mockMultipartFile = new MockMultipartFile(
                "image",
                "test.jpg",
                MediaType.IMAGE_JPEG_VALUE,
                "test image content".getBytes()
        );

        CreateBannerDto createBannerDto = CreateBannerDto.builder()
                .bannerType(BannerType.HOME)
                .head("배너 헤드")
                .sub("배너 서브")
                .exposureDate(LocalDate.of(2025, 3, 20))
                .isExposure(true)
                .build();

        when(s3ImgService.imageUpload(any(MultipartFile.class)))
                .thenReturn("ff/test.jpg");

        CreateBannerDto savedBanner = bannerService.createBanner(createBannerDto, mockMultipartFile);

        // when
        bannerRepository.deleteById(savedBanner.getId());

        // then
        assertThat(bannerRepository.findById(savedBanner.getId())).isEmpty();
    }
}
