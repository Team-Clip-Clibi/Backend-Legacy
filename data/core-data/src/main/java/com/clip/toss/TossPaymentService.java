package com.clip.toss;

import com.clip.toss.entity.TossPayment;
import com.clip.toss.repository.TossPaymentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TossPaymentService {
    private final TossPaymentRepository tossPaymentRepository;

    public TossPayment save(TossPayment tossPayment) {
        return tossPaymentRepository.save(tossPayment);
    }

    public TossPayment findById(Long id) {
        return tossPaymentRepository.findById(id)
                .orElseThrow(IllegalArgumentException::new);
    }

    public List<TossPayment> saveAll(List<TossPayment> tossPayments) {
        return tossPaymentRepository.saveAll(tossPayments);
    }
}
