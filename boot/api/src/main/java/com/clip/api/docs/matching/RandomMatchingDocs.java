package com.clip.api.docs.matching;

import com.clip.api.matching.controller.dto.RandomMatchingDuplicateCheckDto;
import com.clip.api.matching.controller.dto.RandomMatchingOrderDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@Tag(name = "랜덤 매칭", description = "랜덤 매칭 신청")
@RequestMapping("/random")
public interface RandomMatchingDocs {
    @Operation(
            summary = "랜덤 주문서 발행",
            description = """
                     - 랜덤 주문서 발행 API입니다.
                     - 해당 API는 주문서ID, 주문 금액 및 매칭 정보를 반환합니다.
                     - 반환한 orderId, amount 기준으로 결제를 진행해주세요.
                    \s""",
            security = @SecurityRequirement(name = "Bearer Token")
    )
    @ApiResponse(
            responseCode = "200",
            description = "조회 성공"
    )
    @ApiResponse(
            responseCode = "400",
            description = "매칭 처리 중 실패 - 낙관적 락에 의한 예외 발생 (동시성 문제), 매칭 가용 인원이 없는 경우, 가용 인원 차감 과정에서 오류가 발생하는 경우"
    )
    @PostMapping("/orders")
    RandomMatchingOrderDto.Response createOrder(
             @Valid @RequestBody final RandomMatchingOrderDto.Request request,
             @AuthenticationPrincipal final UserDetails userDetails
    );

    @Operation(
            summary = "랜덤 매칭 가용 인원 복구",
            description = """
                     - 랜덤 매칭 가용 인원 복구 API입니다.
                     - 랜덤 매칭 페이지에서(신청 완료 버튼 있는 페이지) 뒤로 가기 버튼을 누를 시 해당 API를 호출해주세요.
                    \s""",
            security = @SecurityRequirement(name = "Bearer Token")
    )
    @ApiResponse(
            responseCode = "200",
            description = "수정 성공"
    )
    @ApiResponse(
            responseCode = "400",
            description = "수량 복구 처리 중 실패 - 낙관적 락에 의한 예외 발생 (동시성 문제)"
    )
    @PatchMapping("/orders/{matchingId}/capacity")
    void restoreCapacity(
            @AuthenticationPrincipal final UserDetails userDetails,
            @PathVariable("matchingId") final Long matchingId
    );

    @Operation(
            summary = "랜덤 매칭 중복 신청 여부 조회",
            description = """
                     - 랜덤 매칭 중복 신청 여부 조회 API입니다.
                     - 랜덤 매칭 페이지에서 중복 신청 여부를 확인할 때 사용합니다.
                    """
    )
    @ApiResponse(
            responseCode = "200",
            description = "조회 성공"
    )
    @GetMapping("/duplicate-check")
    RandomMatchingDuplicateCheckDto checkDuplicateMatching(
            @AuthenticationPrincipal final UserDetails userDetails
    );
}
