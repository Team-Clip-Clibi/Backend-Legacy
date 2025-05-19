package data.coredata.com.clip.order.entity;

import com.clip.ApiApplication;
import com.clip.order.entity.OneThingOrder;
import com.clip.order.repository.OneThingOrderRepository;
import com.clip.price.entity.*;
import com.clip.price.repository.OneThingDiscountRepository;
import com.clip.price.repository.OneThingPriceRepository;
import com.clip.user.entity.User;
import com.clip.user.repository.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ContextConfiguration;

import java.math.BigDecimal;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@ContextConfiguration(classes = ApiApplication.class)
@DataJpaTest
public class OneThingOrderTest {
    @Autowired
    UserRepository userRepository;
    @Autowired
    OneThingOrderRepository oneThingOrderRepository;
    @Autowired
    private OneThingPriceRepository oneThingPriceRepository;
    @Autowired
    private OneThingDiscountRepository oneThingDiscountRepository;

    @DisplayName("정가에서 금액 할인 정책만큼 차감된 금액을 반환한다.")
    @Test
    void getDiscountedPriceByAmount() {
        //given
        UUID orderId = UUID.randomUUID();
        User user = userRepository.save(User.builder().build());
        OneThingPrice price = oneThingPriceRepository.save(OneThingPrice.builder()
                .basePrice(BigDecimal.valueOf(10000))
                .priceType(OneThingPriceType.BASIC)
                .build());
        OneThingDiscount discount = oneThingDiscountRepository.save(OneThingDiscount.builder()
                .discountType(DiscountType.BASE)
                .discountValue(BigDecimal.valueOf(4000))
                .discountUnit(DiscountUnit.AMOUNT)
                .build());

        OneThingOrder order = oneThingOrderRepository.save(OneThingOrder.builder()
                .orderId(orderId)
                .oneThingPrice(price)
                .oneThingDiscount(discount)
                .user(user).build()
        );
        //when
        BigDecimal discountedPrice = order.getDiscountedPrice();

        //then
        assertThat(discountedPrice).isEqualTo(BigDecimal.valueOf(6000));
    }

    @DisplayName("정가보다 금액 할인 정책 액수가 크면 0원을 반환한다.")
    @Test
    void getZeroPriceByAmount() {
        //given
        UUID orderId = UUID.randomUUID();
        User user = userRepository.save(User.builder().build());
        OneThingPrice price = oneThingPriceRepository.save(OneThingPrice.builder()
                .basePrice(BigDecimal.valueOf(10000))
                .priceType(OneThingPriceType.BASIC)
                .build());
        OneThingDiscount discount = oneThingDiscountRepository.save(OneThingDiscount.builder()
                .discountType(DiscountType.BASE)
                .discountValue(BigDecimal.valueOf(99999))
                .discountUnit(DiscountUnit.AMOUNT)
                .build());

        OneThingOrder order = oneThingOrderRepository.save(OneThingOrder.builder()
                .orderId(orderId)
                .oneThingPrice(price)
                .oneThingDiscount(discount)
                .user(user).build()
        );
        //when
        BigDecimal discountedPrice = order.getDiscountedPrice();

        //then
        assertThat(discountedPrice).isEqualTo(BigDecimal.valueOf(0));
    }

    @DisplayName("정가에서 퍼센트 할인 정책만큼 차감된 금액을 반환한다.")
    @Test
    void getDiscountedPriceByPercentage() {
        //given
        UUID orderId = UUID.randomUUID();
        User user = userRepository.save(User.builder().build());
        OneThingPrice price = oneThingPriceRepository.save(OneThingPrice.builder()
                .basePrice(BigDecimal.valueOf(10000))
                .priceType(OneThingPriceType.BASIC)
                .build());
        OneThingDiscount discount = oneThingDiscountRepository.save(OneThingDiscount.builder()
                .discountType(DiscountType.BASE)
                .discountValue(BigDecimal.valueOf(20))
                .discountUnit(DiscountUnit.PERCENTAGE)
                .build());

        OneThingOrder order = oneThingOrderRepository.save(OneThingOrder.builder()
                .orderId(orderId)
                .oneThingPrice(price)
                .oneThingDiscount(discount)
                .user(user).build()
        );
        //when
        BigDecimal discountedPrice = order.getDiscountedPrice();

        //then
        assertThat(discountedPrice).isEqualTo(BigDecimal.valueOf(8000));
    }

    @DisplayName("퍼센트 할인 정책 비율이 100% 이상이면 0을 반환한다.")
    @Test
    void getZeroPriceByPercentage() {
        //given
        UUID orderId = UUID.randomUUID();
        User user = userRepository.save(User.builder().build());
        OneThingPrice price = oneThingPriceRepository.save(OneThingPrice.builder()
                .basePrice(BigDecimal.valueOf(10000))
                .priceType(OneThingPriceType.BASIC)
                .build());
        OneThingDiscount discount = oneThingDiscountRepository.save(OneThingDiscount.builder()
                .discountType(DiscountType.BASE)
                .discountValue(BigDecimal.valueOf(100))
                .discountUnit(DiscountUnit.PERCENTAGE)
                .build());

        OneThingOrder order = oneThingOrderRepository.save(OneThingOrder.builder()
                .orderId(orderId)
                .oneThingPrice(price)
                .oneThingDiscount(discount)
                .user(user).build()
        );
        //when
        BigDecimal discountedPrice = order.getDiscountedPrice();

        //then
        assertThat(discountedPrice).isEqualTo(BigDecimal.valueOf(0));
    }
}
