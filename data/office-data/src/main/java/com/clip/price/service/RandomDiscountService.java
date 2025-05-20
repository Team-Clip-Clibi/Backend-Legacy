package com.clip.price.service;

import com.clip.price.entity.RandomDiscount;
import com.clip.price.repository.RandomDiscountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RandomDiscountService {

     private final RandomDiscountRepository randomDiscountRepository;

     public RandomDiscount findBasicRandomDiscount() {
         return randomDiscountRepository.findBaseDiscount()
                 .orElseThrow(IllegalAccessError::new);
     }
}
