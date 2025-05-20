package com.clip.price.service;

import com.clip.price.entity.RandomPrice;
import com.clip.price.repository.RandomPriceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RandomPriceService {
    private final RandomPriceRepository randomPriceRepository;

    public RandomPrice findBasicRandomPrice() {
        return randomPriceRepository.findBasicPrice()
                .orElseThrow(IllegalAccessError::new);
    }
}
