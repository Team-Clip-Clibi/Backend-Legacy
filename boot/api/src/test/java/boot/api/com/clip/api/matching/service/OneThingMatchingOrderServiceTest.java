package boot.api.com.clip.api.matching.service;

import com.clip.ApiApplication;
import com.clip.api.matching.controller.dto.OneThingOrderDto;
import com.clip.api.matching.service.OneThingMatchingOrderService;
import com.clip.api.payment.feign.TossPaymentFeign;
import com.clip.global.config.feign.FeignConfig;
import com.clip.global.exception.InvalidRequestException;
import com.clip.infra.aws.s3.S3Config;
import com.clip.infra.aws.s3.S3FCMService;
import com.clip.infra.aws.s3.S3ImgService;
import com.clip.infra.fcm.config.FcmConfig;
import com.clip.matching.entity.UserOneThingMatching;
import com.clip.matching.repository.UserOneThingMatchingRepository;
import com.clip.order.repository.OneThingOrderRepository;
import com.clip.price.entity.*;
import com.clip.price.repository.OneThingDiscountRepository;
import com.clip.price.repository.OneThingPriceRepository;
import com.clip.user.entity.User;
import com.clip.user.repository.UserRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@ContextConfiguration(classes = ApiApplication.class)
@SpringBootTest
public class OneThingMatchingOrderServiceTest {
    @MockitoBean
    private S3ImgService s3ImgService;
    @MockitoBean
    private S3Config s3Config;
    @MockitoBean
    private FeignConfig feignConfig;
    @MockitoBean
    private TossPaymentFeign tossPaymentFeign;
    @MockitoBean
    private S3FCMService s3FCMService;
    @MockitoBean
    private FcmConfig fcmConfig;

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private OneThingOrderRepository oneThingOrderRepository;
    @Autowired
    private UserOneThingMatchingRepository userOneThingMatchingRepository;
    @Autowired
    private OneThingMatchingOrderService oneThingMatchingOrderService;
    @Autowired
    private OneThingPriceRepository oneThingPriceRepository;
    @Autowired
    private OneThingDiscountRepository oneThingDiscountRepository;


    @AfterEach
    void tearDown() {
        userOneThingMatchingRepository.deleteAllInBatch();
        oneThingOrderRepository.deleteAllInBatch();
        oneThingPriceRepository.deleteAllInBatch();
        oneThingDiscountRepository.deleteAllInBatch();
        userRepository.deleteAllInBatch();
    }

    @DisplayName("userId와 신청 정보를 기반으로 주문번호와 가격을 반환한다.")
    @Test
    void createOneThingOrder() {
        //given
        User user = userRepository.save(User.builder().build());
        OneThingOrderDto.Request request = OneThingOrderDto.Request.builder().preferredDates(List.of(UserOneThingMatching.PreferredDate.builder().date(LocalDate.now()).build())).build();
        OneThingPrice price = oneThingPriceRepository.save(OneThingPrice.builder()
                .basePrice(BigDecimal.valueOf(8900))
                .priceType(OneThingPriceType.BASIC)
                .build());
        OneThingDiscount discount = oneThingDiscountRepository.save(OneThingDiscount.builder()
                .discountType(DiscountType.BASE)
                .discountValue(BigDecimal.valueOf(6000))
                .discountUnit(DiscountUnit.AMOUNT)
                .build());

        //when
        OneThingOrderDto.Response res = oneThingMatchingOrderService.createOrder(user.getId(), request);

        //then
        Assertions.assertThat(res.getOrderId()).isNotNull();
        Assertions.assertThat(res.getAmount()).isNotNull();
    }

    @DisplayName("원띵 신청 날짜는 현재일로 부터 4일 이전, 21일 이후를 벗어나면 InvalidRequestException이 발생한다.")
    @TestFactory
    List<DynamicTest> createOneThingOrderInvalidRequestException() {
        //given
        User user = userRepository.save(User.builder().build());
        UserOneThingMatching.PreferredDate beforeDay = UserOneThingMatching.PreferredDate.builder().date(LocalDate.now().minusDays(4)).build();
        UserOneThingMatching.PreferredDate afterDay = UserOneThingMatching.PreferredDate.builder().date(LocalDate.now().plusDays(21)).build();

        return List.of(DynamicTest.dynamicTest("beforeDay", () -> {
            //when
            OneThingOrderDto.Request request = OneThingOrderDto.Request.builder()
                    .preferredDates(List.of(beforeDay))
                    .build();

            //then
            Assertions.assertThatThrownBy(() -> oneThingMatchingOrderService.createOrder(user.getId(), request))
                    .isInstanceOf(InvalidRequestException.class);
        }), DynamicTest.dynamicTest("afterDay", () -> {
            //when
            OneThingOrderDto.Request request = OneThingOrderDto.Request.builder()
                    .preferredDates(List.of(afterDay))
                    .build();

            //then
            Assertions.assertThatThrownBy(() -> oneThingMatchingOrderService.createOrder(user.getId(), request))
                    .isInstanceOf(InvalidRequestException.class);
        }), DynamicTest.dynamicTest("beforeDay and afterDay", () -> {
            //when
            OneThingOrderDto.Request request = OneThingOrderDto.Request.builder()
                    .preferredDates(List.of(beforeDay, afterDay))
                    .build();

            //then
            Assertions.assertThatThrownBy(() -> oneThingMatchingOrderService.createOrder(user.getId(), request))
                    .isInstanceOf(InvalidRequestException.class);
        }));
    }
}
