package boot.api.com.clip.api.report.service;

import com.clip.ApiApplication;
import com.clip.api.payment.feign.TossPaymentFeign;
import com.clip.api.report.controller.dto.ReportDto;
import com.clip.api.report.service.UserReportService;
import com.clip.global.config.feign.FeignConfig;
import com.clip.infra.aws.s3.S3Config;
import com.clip.infra.aws.s3.S3ImgService;
import com.clip.report.entity.Report;
import com.clip.report.entity.ReportCategory;
import com.clip.report.repository.ReportRepository;
import com.clip.user.entity.User;
import com.clip.user.repository.UserRepository;
import jakarta.validation.ConstraintViolationException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@ContextConfiguration(classes = ApiApplication.class)
@SpringBootTest
public class UserReportServiceTest {
    @Autowired
    private UserReportService userReportService;
    @Autowired
    private ReportRepository reportRepository;
    @Autowired
    private UserRepository userRepository;
    @MockitoBean
    private S3ImgService s3ImgService;
    @MockitoBean
    private S3Config s3Config;
    @MockitoBean
    private FeignConfig feignConfig;
    @MockitoBean
    private TossPaymentFeign tossPaymentFeign;

    @AfterEach
    void tearDown() {
        userRepository.deleteAllInBatch();
        reportRepository.deleteAllInBatch();
    }

    @DisplayName("userId로 마이페이지 신고 내역을 작성할 수 있다.")
    @Test
    void saveUserReportService() {
        //given
        String reportContent = "신고합니다";
        ReportCategory reportCategory = ReportCategory.ABUSING;
        User user = userRepository.save(User.builder().build());

        //when
        userReportService.saveUserReport(
                user.getId(),
                ReportDto.builder()
                        .content(reportContent)
                        .reportCategory(reportCategory)
                        .build()
        );

        //then
        List<Report> reports = reportRepository.findReports(user.getId());
        assertThat(reports.getFirst())
                .extracting(Report::getContent,Report::getReportCategory)
                .containsExactly(reportContent,reportCategory);

    }

    @DisplayName("신고 내역의 최대 글자는 500자이다.")
    @Test
    void saveReportSize500() {
        //given
        User user = userRepository.save(User.builder().build());
        String contentSize500 = "어느 봄날, 나는 길을 걷다가 우연히 오래된 서점을 발견했다. " +
                "서점은 좁았지만 따뜻한 분위기를 풍겼고, 나무 책장마다 다양한 책들이 빼곡히 꽂혀 있었다. " +
                "나는 조용히 책장을 훑어보다가 낡은 표지의 소설 한 권을 꺼냈다. 제목은 익숙했지만 내용을 기억할 수 없었다. " +
                "나는 책을 펼쳐 첫 문장을 읽어 내려갔다. 그러자 마치 시간 여행을 하는 듯한 기분이 들었다. " +
                "활자들은 조용히 이야기를 들려주었고, 나는 점점 이야기에 빠져들었다. 어느새 책장 한쪽에 마련된 작은 의자에 앉아 읽고 있었다. " +
                "창밖에서는 봄바람이 살랑이며 나뭇잎을 흔들었다. " +
                "서점 안에는 나 외에도 몇 명의 손님이 있었는데, 그들은 각자 조용히 책을 읽거나 고르고 있었다. " +
                "서점 주인은 낡은 책상 뒤에서 독서를 하고 있었고, 간혹 손님들에게 미소를 지어 보였다. " +
                "나는 책을 한 장, 또 한 장 넘기며 이야기 속으로 깊이 빠져들었다. 이 순간만큼은 시간도 멈춘 듯했다. " +
                "책을 읽는 동안 현실의 고민들은 희미해졌고,,,,,,,,,,,,,";

        //when
        userReportService.saveUserReport(user.getId(), ReportDto.builder().content(contentSize500).build());

        //then
        List<Report> reports = reportRepository.findReports(user.getId());
        assertThat(contentSize500).hasSize(500);
        assertThat(reports.getFirst()).extracting(Report::getContent).isEqualTo(contentSize500);
    }

    @DisplayName("신고 내역의 최대 글자는 500자를 초과하면 ConstraintViolationException이 발생한다.")
    @Test
    void saveFailReport() {
        //given
        User user = userRepository.save(User.builder().build());
        String contentSize500 = "어느 봄날, 나는 길을 걷다가 우연히 오래된 서점을 발견했다. " +
                "서점은 좁았지만 따뜻한 분위기를 풍겼고, 나무 책장마다 다양한 책들이 빼곡히 꽂혀 있었다. " +
                "나는 조용히 책장을 훑어보다가 낡은 표지의 소설 한 권을 꺼냈다. 제목은 익숙했지만 내용을 기억할 수 없었다. " +
                "나는 책을 펼쳐 첫 문장을 읽어 내려갔다. 그러자 마치 시간 여행을 하는 듯한 기분이 들었다. " +
                "활자들은 조용히 이야기를 들려주었고, 나는 점점 이야기에 빠져들었다. 어느새 책장 한쪽에 마련된 작은 의자에 앉아 읽고 있었다. " +
                "창밖에서는 봄바람이 살랑이며 나뭇잎을 흔들었다. " +
                "서점 안에는 나 외에도 몇 명의 손님이 있었는데, 그들은 각자 조용히 책을 읽거나 고르고 있었다. " +
                "서점 주인은 낡은 책상 뒤에서 독서를 하고 있었고, 간혹 손님들에게 미소를 지어 보였다. " +
                "나는 책을 한 장, 또 한 장 넘기며 이야기 속으로 깊이 빠져들었다. 이 순간만큼은 시간도 멈춘 듯했다. " +
                "책을 읽는 동안 현실의 고민들은 희미해졌고,,,,,,,,,,,,,,";



        //when & then
        assertThat(contentSize500).hasSize(501);
        assertThatThrownBy(()->userReportService.saveUserReport(user.getId(), ReportDto.builder().content(contentSize500).build()))
                .isInstanceOf(ConstraintViolationException.class);
    }
}
