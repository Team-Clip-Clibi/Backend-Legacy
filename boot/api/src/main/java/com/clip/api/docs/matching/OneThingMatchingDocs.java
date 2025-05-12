package com.clip.api.docs.matching;

import com.clip.api.matching.controller.dto.OneThingOrderDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@Tag(name = "원띵 매칭", description = "원띵 매칭 신청")
@RequestMapping("/onethings")
public interface OneThingMatchingDocs {

    @Operation(
            summary = "원띵 주문서 발행",
            description = """
                     - 원띵 주문서 발행 API입니다.
                     - 해당 API는 주문서ID, 주문 금액을 반환합니다.
                     - 반환한 orderId, amount 기준으로 결제를 진행해주세요.
                    \s""",
            security = @SecurityRequirement(name = "Bearer Token")
    )
    @ApiResponse(
            responseCode = "200",
            description = "조회 성공"
    )
    @PostMapping("/order")
    OneThingOrderDto.Response createOrder(
            @Valid @RequestBody OneThingOrderDto.Request request,
            @AuthenticationPrincipal UserDetails userDetails);
}
