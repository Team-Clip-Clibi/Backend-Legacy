package com.clip.api.docs.report;

import com.clip.api.report.controller.dto.ReportDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;

@Tag(name = "신고", description = "신고")
@RequestMapping("/report")
public interface ReportDocs {

    @Operation(
            summary = "신고 등록 API",
            description = """
                    마이페이지에서 작성된 신고를 등록합니다.
                    """
    )
    @ApiResponse(
            responseCode = "204",
            description = "신고 등록 완료",
            content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = ReportDto.class)
            )
    )
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PostMapping
    void createRandomCommunity(
            @RequestBody ReportDto reportDto,
            @AuthenticationPrincipal UserDetails userDetails);
}
