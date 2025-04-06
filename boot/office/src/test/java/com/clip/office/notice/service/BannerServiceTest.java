//package com.clip.office.notice.service;
//
//import com.clip.OfficeApplication;
//import com.clip.global.service.S3Service;
//import com.clip.notice.repository.BannerRepository;
//import com.clip.office.notice.controller.dto.CreateBannerDto;
//import org.junit.jupiter.api.AfterEach;
//import org.junit.jupiter.api.DisplayName;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.http.MediaType;
//import org.springframework.mock.web.MockMultipartFile;
//import org.springframework.test.context.ContextConfiguration;
//import org.springframework.test.context.TestPropertySource;
//import org.springframework.test.context.bean.override.mockito.MockitoBean;
//import org.springframework.web.multipart.MultipartFile;
//
//import java.time.LocalDate;
//
//import static org.assertj.core.api.Assertions.assertThat;
//import static org.mockito.ArgumentMatchers.any;
//import static org.mockito.Mockito.when;
//
//@ContextConfiguration(classes = OfficeApplication.class)
//@SpringBootTest
//@TestPropertySource(properties = {
//        "spring.datasource.redis.host=localhost",
//        "spring.datasource.redis.port=6379",
//        "spring.security.login.max-fail-count=5",
//        "spring.security.login.block-duration-seconds=3600",
//        "cloud.aws.credentials.access-key=1010",
//        "cloud.aws.credentials.secret-key=1010",
//        "cloud.aws.region.static=ap-northeast-2",
//        "cloud.aws.s3.bucket=clip-office",
//})
//class BannerServiceTest {
//
//    @Autowired
//    private BannerService bannerService;
//
//    @Autowired
//    private BannerRepository bannerRepository;
//
//    @MockitoBean
//    private S3Service s3Service;
//
//    @AfterEach
//    void tearDown() {
//        bannerRepository.deleteAllInBatch();
//    }
//
//    @DisplayName("office에서 사진과 배너 정보를 입력하면 배너를 생성할 수 있다.")
//    @Test
//    void createBanner() {
//        // given
//        MockMultipartFile mockMultipartFile = new MockMultipartFile(
//                "image",
//                "test.jpg",
//                MediaType.IMAGE_JPEG_VALUE,
//                "test image content".getBytes()
//        );
//
//        CreateBannerDto createBannerDto = CreateBannerDto.builder()
//                .exposureLocation("home")
//                .head("배너 헤드")
//                .sub("배너 서브")
//                .exposureDate(LocalDate.of(2025, 3, 20))
//                .isExposure(true)
//                .build();
//
//        // S3Service 모킹
//        when(s3Service.imageUpload(any(MultipartFile.class)))
//                .thenReturn("ff/test.jpg");
//
//        // when
//        CreateBannerDto savedBanner = bannerService.createBanner(createBannerDto, mockMultipartFile);
//
//        // then
//        assertThat(savedBanner.getHead()).isEqualTo("배너 헤드");
//        assertThat(savedBanner.getSub()).isEqualTo("배너 서브");
//        assertThat(savedBanner.getImageUrl()).isEqualTo("ff/test.jpg");
//        assertThat(savedBanner.getExposureLocation()).isEqualTo("home");
//        assertThat(savedBanner.getExposureDate()).isEqualTo(LocalDate.of(2025, 3, 20));
//        assertThat(savedBanner.isExposure()).isTrue();
//    }
//}
