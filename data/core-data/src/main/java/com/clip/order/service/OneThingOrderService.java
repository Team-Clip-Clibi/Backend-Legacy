package com.clip.order.service;

import com.clip.order.entity.OneThingOrder;
import com.clip.order.entity.OneThingOrderStatus;
import com.clip.order.exception.OrderNotFoundException;
import com.clip.order.repository.OneThingOrderRepository;
import com.clip.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class OneThingOrderService {
    private final OneThingOrderRepository oneThingOrderRepository;

    public OneThingOrder createOrder(User user, int amount) {
        OneThingOrder oneThingOrder = OneThingOrder.builder()
                .orderId(UUID.randomUUID())
                .user(user)
                .status(OneThingOrderStatus.WAIT_FOR_PAYMENT)
                .amount(amount)
                .build();
        return oneThingOrderRepository.save(oneThingOrder);
    }

    public OneThingOrder findOneThingOrder(long userId, UUID orderId) {
        return oneThingOrderRepository.findOneThingOrder(userId, orderId)
                .orElseThrow(OrderNotFoundException::new);
    }
}
