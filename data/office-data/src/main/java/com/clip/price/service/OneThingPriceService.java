package com.clip.price.service;

import com.clip.price.entity.OneThingPrice;
import com.clip.price.repository.OneThingPriceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OneThingPriceService {
    private final OneThingPriceRepository oneThingPriceRepository;

    public OneThingPrice findBasicOneThingPrice() {
        return oneThingPriceRepository.findBasicPrice()
                .orElseThrow(IllegalAccessError::new);
    }
}
