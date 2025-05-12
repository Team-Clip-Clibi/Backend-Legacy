package com.clip.api.docs.payment;

import com.clip.api.payment.controller.dto.PaymentDto;
import com.clip.api.report.controller.dto.ReportDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;

@Tag(name = "결제 승인", description = "결제 승인 API")
@RequestMapping("/payments")
public interface PaymentDocs {

    @Operation(
            summary = "결제 승인 API",
            description = """
                    paymentKey를 통해 결제 내역을 검증하고 승인합니다.
                    """,
            security = @SecurityRequirement(name = "Bearer Token")
    )
    @ResponseStatus(HttpStatus.OK)
    @PostMapping("/confirm")
    void createRandomCommunity(
            @RequestBody PaymentDto paymentDto,
            @AuthenticationPrincipal UserDetails userDetails);
}
