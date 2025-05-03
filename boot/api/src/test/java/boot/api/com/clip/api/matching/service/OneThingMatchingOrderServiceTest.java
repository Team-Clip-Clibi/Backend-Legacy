package boot.api.com.clip.api.matching.service;

import com.clip.ApiApplication;
import com.clip.api.matching.controller.dto.OneThingOrderDto;
import com.clip.api.matching.service.OneThingMatchingOrderService;
import com.clip.infra.aws.s3.S3Config;
import com.clip.infra.aws.s3.S3ImgService;
import com.clip.matching.repository.UserOneThingMatchingRepository;
import com.clip.order.repository.OneThingOrderRepository;
import com.clip.user.entity.User;
import com.clip.user.repository.UserRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

@ContextConfiguration(classes = ApiApplication.class)
@SpringBootTest
public class OneThingMatchingOrderServiceTest {
    @MockitoBean
    private S3ImgService s3ImgService;
    @MockitoBean
    private S3Config s3Config;

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private OneThingOrderRepository oneThingOrderRepository;
    @Autowired
    private UserOneThingMatchingRepository userOneThingMatchingRepository;
    @Autowired
    private OneThingMatchingOrderService oneThingMatchingOrderService;



    @AfterEach
    void tearDown() {
        oneThingOrderRepository.deleteAllInBatch();
        userOneThingMatchingRepository.deleteAllInBatch();
        userRepository.deleteAllInBatch();
    }

    @DisplayName("userId와 신청 정보를 기반으로 주문번호와 가격을 반환한다.")
    @Test
    void createOneThingOrder() {
        //given
        User user = userRepository.save(User.builder().build());
        OneThingOrderDto.Request request = OneThingOrderDto.Request.builder().build();

        //when
        OneThingOrderDto.Response res = oneThingMatchingOrderService.createOrder(user.getId(), request);

        //then
        Assertions.assertThat(res.getOrderId()).isNotNull();
        Assertions.assertThat(res.getAmount()).isNotNull();
    }
}
