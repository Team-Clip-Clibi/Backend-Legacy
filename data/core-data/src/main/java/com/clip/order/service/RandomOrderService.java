package com.clip.order.service;

import com.clip.order.entity.RandomOrder;
import com.clip.order.exception.OrderNotFoundException;
import com.clip.order.repository.RandomOrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;import java.util.UUID;

@Service
@RequiredArgsConstructor
public class RandomOrderService {
    private final RandomOrderRepository randomOrderRepository;

    public RandomOrder findRandomOrder(long userId, UUID orderId) {
        return randomOrderRepository.findRandomOrder(userId, orderId)
                .orElseThrow(OrderNotFoundException::new);
    }
}
