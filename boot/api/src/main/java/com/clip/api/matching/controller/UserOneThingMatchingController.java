package com.clip.api.matching.controller;

import com.clip.api.docs.matching.OneThingMatchingDocs;
import com.clip.api.matching.controller.dto.OneThingOrderDto;
import com.clip.api.matching.service.OneThingMatchingOrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class UserOneThingMatchingController implements OneThingMatchingDocs {
    private final OneThingMatchingOrderService userOneThingMatchingService;

    @Override
    public OneThingOrderDto.Response createOrder(OneThingOrderDto.Request request, UserDetails userDetails) {
        return userOneThingMatchingService.createOrder(Long.parseLong(userDetails.getUsername()), request);
    }
}
