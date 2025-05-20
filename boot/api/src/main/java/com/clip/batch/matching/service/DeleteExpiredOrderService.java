package com.clip.batch.matching.service;

import com.clip.batch.matching.repository.RandomMatchingOrderBatchRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class DeleteExpiredOrderService {
    private final RandomMatchingOrderBatchRepository randomMatchingOrderBatchRepository;

    public void deleteExpiredOrder() {
        randomMatchingOrderBatchRepository.deleteExpiredOrder(LocalDateTime.now());
    }
}
