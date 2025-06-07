package com.clip.batch.matching.service;

import com.clip.batch.matching.repository.RandomMatchingCapacityBatchRepository;
import com.clip.batch.matching.repository.RandomMatchingOrderBatchRepository;
import com.clip.batch.matching.repository.UserRandomMatchingBatchRepository;
import com.clip.order.entity.RandomOrder;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class HandleExpiredOrderService {
    private final RandomMatchingOrderBatchRepository randomMatchingOrderBatchRepository;
    private final UserRandomMatchingBatchRepository userRandomMatchingBatchRepository;
    private final RandomMatchingCapacityBatchRepository randomMatchingCapacityBatchRepository;

    @Transactional
    public void deleteExpiredOrdersAndUpdateCapacity() {
        List<RandomOrder> expiredOrders = randomMatchingOrderBatchRepository.findExpiredOrders(LocalDateTime.now());

        List<Long> matchingIds = expiredOrders.stream()
                .map(order -> order.getRandomMatching().getId())
                .distinct()
                .toList();

        userRandomMatchingBatchRepository.deleteByMatchingIds(matchingIds);
        randomMatchingOrderBatchRepository.deleteAll(expiredOrders);
        randomMatchingCapacityBatchRepository.updateAvailableCapacity(matchingIds);
    }
}
