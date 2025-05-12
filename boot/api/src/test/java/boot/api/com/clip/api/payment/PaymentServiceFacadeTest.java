package boot.api.com.clip.api.payment;

import com.clip.ApiApplication;
import com.clip.api.payment.controller.dto.OrderType;
import com.clip.api.payment.controller.dto.PaymentDto;
import com.clip.api.payment.feign.TossPaymentFeign;
import com.clip.api.payment.feign.dto.PaymentObject;
import com.clip.api.payment.service.PaymentServiceFacade;
import com.clip.api.payment.service.event.PaymentExceptionEvent;
import com.clip.global.config.feign.FeignConfig;
import com.clip.infra.aws.s3.S3Config;
import com.clip.infra.aws.s3.S3FCMService;
import com.clip.infra.aws.s3.S3ImgService;
import com.clip.infra.fcm.config.FcmConfig;
import com.clip.order.entity.OneThingOrder;
import com.clip.order.entity.OneThingOrderStatus;
import com.clip.order.repository.OneThingOrderRepository;
import com.clip.order.service.OneThingOrderService;
import com.clip.user.entity.User;
import com.clip.user.repository.UserRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.context.bean.override.mockito.MockitoSpyBean;
import org.springframework.test.context.event.ApplicationEvents;
import org.springframework.test.context.event.RecordApplicationEvents;

import java.util.UUID;

import static org.assertj.core.api.Assertions.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.*;

@ContextConfiguration(classes = ApiApplication.class)
@RecordApplicationEvents
@SpringBootTest
public class PaymentServiceFacadeTest {

    @Autowired
    private PaymentServiceFacade paymentServiceFacade;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private OneThingOrderRepository oneThingOrderRepository;

    @MockitoBean
    private S3ImgService s3ImgService;
    @MockitoBean
    private S3Config s3Config;
    @MockitoBean
    private S3FCMService s3FCMService;
    @MockitoBean
    private FcmConfig fcmConfig;
    @MockitoBean
    private FeignConfig feignConfig;
    @MockitoBean
    private TossPaymentFeign tossPaymentFeign;
    @MockitoSpyBean
    private OneThingOrderService oneThingOrderService;
    @Autowired
    private ApplicationEvents applicationEvents;


    @AfterEach
    void tearDown() {
        oneThingOrderRepository.deleteAllInBatch();
        userRepository.deleteAllInBatch();
    }

    @DisplayName("oneThing주문 결제를 승인 후 payment 객체를 주문서에 저장한다.")
    @Test
    void confirmPayment() {
        //given
        UUID orderId = UUID.randomUUID();
        User user = userRepository.save(User.builder().build());
        oneThingOrderRepository.save(OneThingOrder.builder().orderId(orderId).amount(2900).user(user).build());

        PaymentObject paymentObject = PaymentObject.builder()
                .paymentKey("paymentKey")
                .orderId(orderId)
                .totalAmount(2900)
                .build();
        given(tossPaymentFeign.confirmPayment(any())).willReturn(paymentObject);

        //when
        paymentServiceFacade.confirm(
                user.getId(),
                PaymentDto.builder()
                        .orderType(OrderType.ONETHING)
                        .orderId(orderId)
                        .build()
        );

        //then
        OneThingOrder oneThingOrder = oneThingOrderRepository.findOneThingOrder(user.getId(), orderId).get();
        assertThat(oneThingOrder.getTossPayment()).hasSize(1);
        assertThat(oneThingOrder.getStatus()).isEqualTo(OneThingOrderStatus.DONE);
    }

    @DisplayName("oneThing주문 결제를 승인 중 예외가 발생하면 payment 정보는 저장되지 않는다.")
    @Test
    void confirmPaymentAPIException() {
        //given
        UUID orderId = UUID.randomUUID();
        User user = userRepository.save(User.builder().build());

        oneThingOrderRepository.save(OneThingOrder.builder()
                .orderId(orderId)
                .amount(2900)
                .user(user)
                .status(OneThingOrderStatus.WAIT_FOR_PAYMENT)
                .build()
        );

        given(tossPaymentFeign.confirmPayment(any())).willThrow(new RuntimeException("결제 정보 저장 중 예외 발생"));

        //when
        assertThatThrownBy(() -> paymentServiceFacade.confirm(
                user.getId(),
                PaymentDto.builder()
                        .orderType(OrderType.ONETHING)
                        .orderId(orderId)
                        .build()
        )).isInstanceOf(RuntimeException.class);

        //then
        OneThingOrder oneThingOrder = oneThingOrderRepository.findOneThingOrder(user.getId(), orderId).get();
        assertThat(oneThingOrder.getTossPayment()).isEmpty();
        assertThat(oneThingOrder.getStatus()).isEqualTo(OneThingOrderStatus.WAIT_FOR_PAYMENT);
    }

    @DisplayName("oneThing주문 결제 정보 저장 중 예외가 발생하면 PaymentExceptionEvent가 발행된다.")
    @Test
    void confirmPaymentExceptionEvent() {
        //given
        UUID orderId = UUID.randomUUID();
        User user = userRepository.save(User.builder().build());
        oneThingOrderRepository.save(OneThingOrder.builder()
                .orderId(orderId)
                .amount(2900)
                .user(user)
                .build()
        );

        OneThingOrder mockOneThingOrder = Mockito.mock(OneThingOrder.class);
        given(oneThingOrderService.findOneThingOrder(user.getId(), orderId))
                .willReturn(mockOneThingOrder);
        willThrow(new RuntimeException())
                .given(mockOneThingOrder).updateStatus(OneThingOrderStatus.DONE);
        given(tossPaymentFeign.confirmPayment(any()))
                .willReturn(PaymentObject.builder().orderId(orderId).build());

        //when
        assertThatThrownBy(() -> paymentServiceFacade.confirm(
                user.getId(),
                PaymentDto.builder()
                        .paymentKey("paymentKey")
                        .orderType(OrderType.ONETHING)
                        .orderId(orderId)
                        .build()
        )).isInstanceOf(RuntimeException.class);

        //then
        assertThat(applicationEvents.stream(PaymentExceptionEvent.class)).hasSize(1);
    }
}
