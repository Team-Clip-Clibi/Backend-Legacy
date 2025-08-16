package com.clip.order.service;

import com.clip.global.exception.ResourceNotFoundException;
import com.clip.order.entity.OneThingOrder;
import com.clip.order.entity.OneThingOrderStatus;
import com.clip.order.repository.OneThingOrderRepository;
import com.clip.price.entity.OneThingDiscount;
import com.clip.price.entity.OneThingPrice;
import com.clip.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class OneThingOrderService {
    private final OneThingOrderRepository oneThingOrderRepository;

    public OneThingOrder createOrder(User user, OneThingPrice oneThingPrice, OneThingDiscount oneThingDiscount) {
        OneThingOrder oneThingOrder = OneThingOrder.builder()
                .orderId(UUID.randomUUID())
                .user(user)
                .status(OneThingOrderStatus.WAIT_FOR_PAYMENT)
                .oneThingPrice(oneThingPrice)
                .oneThingDiscount(oneThingDiscount)
                .build();
        return oneThingOrderRepository.save(oneThingOrder);
    }

    public OneThingOrder findOneThingOrder(long userId, UUID orderId) {
        return oneThingOrderRepository.findOneThingOrder(userId, orderId)
                .orElseThrow(()-> new ResourceNotFoundException("onethingOrder", orderId.toString()));
    }

    public List<OneThingOrder> saveAll(List<OneThingOrder> oneThingOrders) {
        return oneThingOrderRepository.saveAll(oneThingOrders);
    }
}
