package com.clip.price.service;

import com.clip.price.entity.OneThingDiscount;
import com.clip.price.repository.OneThingDiscountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OneThingDiscountService {
    private final OneThingDiscountRepository oneThingDiscountRepository;

    public OneThingDiscount findBaseDiscount() {
        return oneThingDiscountRepository.findBaseDiscount()
                .orElseThrow(IllegalAccessError::new);
    }
}
