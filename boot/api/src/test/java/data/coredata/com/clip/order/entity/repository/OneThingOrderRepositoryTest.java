package data.coredata.com.clip.order.entity.repository;

import com.clip.ApiApplication;
import com.clip.matching.entity.OneThingMatching;
import com.clip.matching.entity.RandomMatching;
import com.clip.matching.repository.OneThingMatchingRepository;
import com.clip.matching.repository.RandomMatchingRepository;
import com.clip.order.entity.OneThingOrder;
import com.clip.order.entity.OneThingOrderStatus;
import com.clip.order.entity.RandomOrder;
import com.clip.order.entity.RandomOrderStatus;
import com.clip.order.repository.OneThingOrderRepository;
import com.clip.order.repository.RandomOrderRepository;
import com.clip.toss.entity.TossPayment;
import com.clip.user.entity.User;
import com.clip.user.repository.UserRepository;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.test.context.ContextConfiguration;

import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@ContextConfiguration(classes = ApiApplication.class)
@DataJpaTest
public class OneThingOrderRepositoryTest {

    @Autowired
    UserRepository userRepository;
    @Autowired
    OneThingMatchingRepository oneThingMatchingRepository;
    @Autowired
    OneThingOrderRepository oneThingOrderRepository;
    @Autowired
    RandomMatchingRepository randomMatchingRepository;
    @Autowired
    RandomOrderRepository randomOrderRepository;

    @DisplayName("유저가 원띵 모임을 신청하면 READY 상태의 주문서를 저장할 수 있다.")
    @Test
    void saveReadyOneThingOrder() {
        //given
        User user = userRepository.save(User.builder().build());
        OneThingMatching oneThingMatching = oneThingMatchingRepository.save(new OneThingMatching());

        //when
        OneThingOrder oneThingOrder = OneThingOrder.builder()
                .oneThingMatching(oneThingMatching)
                .user(user)
                .status(OneThingOrderStatus.READY)
                .build();
        OneThingOrder savedOneThingOrder = oneThingOrderRepository.save(oneThingOrder);

        //then
        assertThat(savedOneThingOrder)
                .extracting(
                        OneThingOrder::getUser,
                        OneThingOrder::getStatus,
                        OneThingOrder::getTossPayment
                ).containsExactly(
                        user,
                        OneThingOrderStatus.READY,
                        null
                );
    }

    @DisplayName("유저는 동일한 원띵 매칭 주문서를 2개 이상 발행할 수 없다.")
    @Test
    void failSaveOneThingMatchingOrder() {
        //given
        User user = userRepository.save(User.builder().build());
        OneThingMatching oneThingMatching = oneThingMatchingRepository.save(new OneThingMatching());

        //when
        OneThingOrder oneThingOrder1 = OneThingOrder.builder()
                .oneThingMatching(oneThingMatching)
                .user(user)
                .status(OneThingOrderStatus.READY)
                .build();
        OneThingOrder savedOneThingOrder1 = oneThingOrderRepository.save(oneThingOrder1);

        //then
        OneThingOrder oneThingOrder2 = OneThingOrder.builder()
                .oneThingMatching(oneThingMatching)
                .user(user)
                .status(OneThingOrderStatus.READY)
                .build();
        assertThatThrownBy(() -> oneThingOrderRepository.save(oneThingOrder2))
                .isInstanceOf(DataIntegrityViolationException.class);
    }

    @DisplayName("원띵 주문내역 변경 시나리오 테스트")
    @TestFactory
    List<DynamicTest> oneThingOrderChange() {
        //given
        String paymentId = "paymentId";
        UUID orderId = UUID.randomUUID();
        Integer amount = 4500;

        User user = userRepository.save(User.builder().build());
        OneThingMatching oneThingMatching = oneThingMatchingRepository.save(new OneThingMatching());

        return List.of(DynamicTest.dynamicTest("유저가 원띵 모임을 신청하면 READY 상태의 주문서가 발행된다.",()->{
            //when
            OneThingOrder oneThingOrder = OneThingOrder.builder()
                    .oneThingMatching(oneThingMatching)
                    .user(user)
                    .status(OneThingOrderStatus.READY)
                    .build();
            OneThingOrder savedOneThingOrder = oneThingOrderRepository.save(oneThingOrder);

            //then
            assertThat(savedOneThingOrder)
                    .extracting(
                            OneThingOrder::getUser,
                            OneThingOrder::getStatus,
                            OneThingOrder::getTossPayment
                    ).containsExactly(user, OneThingOrderStatus.READY, null);
        }), DynamicTest.dynamicTest("유저가 결제를 완료하면 DONE 상태로 변경되며 결제에 해당하는 tossPayment가 생성된다.",()->{

            //when
            OneThingOrder oneThingOrder = oneThingOrderRepository.findOneThingOrder(user.getId(), oneThingMatching.getId()).get();
            oneThingOrder.updateStatus(OneThingOrderStatus.DONE);
            TossPayment tossPayment = TossPayment.builder()
                    .orderId(orderId)
                    .paymentId(paymentId)
                    .amount(amount)
                    .build();
            oneThingOrder.addTossPayment(tossPayment);
            OneThingOrder savedOneThingOrder = oneThingOrderRepository.save(oneThingOrder);

            //then
            assertThat(savedOneThingOrder)
                    .extracting(OneThingOrder::getStatus)
                    .isEqualTo(OneThingOrderStatus.DONE);
            assertThat(oneThingOrder.getTossPayment()).hasSize(1);
        }), DynamicTest.dynamicTest("유저가 결제를 취소하면 상태로 변경되며 결제에 해당하는 tossPayment가 생성된다.",()->{

            //when
            OneThingOrder oneThingOrder = oneThingOrderRepository.findOneThingOrder(user.getId(), oneThingMatching.getId()).get();
            oneThingOrder.updateStatus(OneThingOrderStatus.CANCELED);
            TossPayment tossPayment = TossPayment.builder()
                    .orderId(orderId)
                    .paymentId(paymentId)
                    .amount(amount)
                    .build();
            oneThingOrder.addTossPayment(tossPayment);
            OneThingOrder savedOneThingOrder = oneThingOrderRepository.save(oneThingOrder);

            //then
            assertThat(savedOneThingOrder)
                    .extracting(OneThingOrder::getStatus)
                    .isEqualTo(OneThingOrderStatus.CANCELED);
            assertThat(oneThingOrder.getTossPayment()).hasSize(2);
        }));
    }

    @DisplayName("유저가 랜덤 모임을 신청하면 DONE 상태의 주문서를 저장할 수 있다.")
    @Test
    void saveReadyRandomOrder() {
        //given
        User user = userRepository.save(User.builder().build());
        RandomMatching randomMatching = randomMatchingRepository.save(RandomMatching.builder().build());

        //when
        RandomOrder oneThingOrder = RandomOrder.builder()
                .randomMatching(randomMatching)
                .user(user)
                .status(RandomOrderStatus.DONE)
                .build();
        RandomOrder savedRandomOrder = randomOrderRepository.save(oneThingOrder);

        //then
        assertThat(savedRandomOrder)
                .extracting(
                        RandomOrder::getUser,
                        RandomOrder::getStatus,
                        RandomOrder::getTossPayment
                ).containsExactly(
                        user,
                        RandomOrderStatus.DONE,
                        null
                );
    }

    @DisplayName("유저는 동일한 랜덤 매칭 주문서를 2개 이상 발행할 수 없다.")
    @Test
    void failSaveRandomMatchingOrder() {
        //given
        User user = userRepository.save(User.builder().build());
        RandomMatching randomMatching = randomMatchingRepository.save(RandomMatching.builder().build());

        //when
        RandomOrder randomOrder1 = RandomOrder.builder()
                .randomMatching(randomMatching)
                .user(user)
                .status(RandomOrderStatus.ABORTED)
                .build();
        RandomOrder savedRandomOrder = randomOrderRepository.save(randomOrder1);

        //then
        RandomOrder randomOrder2 = RandomOrder.builder()
                .randomMatching(randomMatching)
                .user(user)
                .status(RandomOrderStatus.ABORTED)
                .build();
        assertThatThrownBy(() -> randomOrderRepository.save(randomOrder2))
                .isInstanceOf(DataIntegrityViolationException.class);
    }

    @DisplayName("랜덤 주문내역 변경 시나리오 테스트")
    @TestFactory
    List<DynamicTest> randomOrderChange() {
        //given
        String paymentId = "paymentId";
        UUID orderId = UUID.randomUUID();
        Integer amount = 4500;

        User user = userRepository.save(User.builder().build());
        RandomMatching randomMatching = randomMatchingRepository.save(RandomMatching.builder().build());
        randomOrderRepository.save(RandomOrder.builder().user(user).randomMatching(randomMatching).build());

        return List.of(DynamicTest.dynamicTest("유저가 결제를 완료하면 DONE 상태로 변경되며 결제에 해당하는 tossPayment가 생성된다.",()->{

            //when
            RandomOrder randomOrder = randomOrderRepository.findRandomOrder(user.getId(), randomMatching.getId()).get();
            randomOrder.updateStatus(RandomOrderStatus.DONE);
            TossPayment tossPayment = TossPayment.builder()
                    .orderId(orderId)
                    .paymentId(paymentId)
                    .amount(amount)
                    .build();
            randomOrder.addTossPayment(tossPayment);
            RandomOrder savedRandomOrder = randomOrderRepository.save(randomOrder);

            //then
            assertThat(savedRandomOrder)
                    .extracting(RandomOrder::getStatus)
                    .isEqualTo(RandomOrderStatus.DONE);
            assertThat(randomOrder.getTossPayment()).hasSize(1);
        }), DynamicTest.dynamicTest("유저가 결제를 취소하면 상태로 변경되며 결제에 해당하는 tossPayment가 생성된다.",()->{

            //when
            RandomOrder randomOrder = randomOrderRepository.findRandomOrder(user.getId(), randomMatching.getId()).get();
            randomOrder.updateStatus(RandomOrderStatus.CANCELED);
            TossPayment tossPayment = TossPayment.builder()
                    .orderId(orderId)
                    .paymentId(paymentId)
                    .amount(amount)
                    .build();
            randomOrder.addTossPayment(tossPayment);
            RandomOrder savedRandomOrder = randomOrderRepository.save(randomOrder);

            //then
            assertThat(savedRandomOrder)
                    .extracting(RandomOrder::getStatus)
                    .isEqualTo(RandomOrderStatus.CANCELED);
            assertThat(randomOrder.getTossPayment()).hasSize(2);
        }));
    }
}
