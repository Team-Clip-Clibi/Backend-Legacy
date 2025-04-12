package com.clip.api.docs.matching;

import com.clip.api.matching.controller.dto.MatchingSummaryDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Tag(name = "매칭 관리", description = "매칭 신청, 조회 etc ")
@RequestMapping("/matchings")
public interface UserMatchingDocs {

    @Operation(
            summary = "매칭된 모임 정보 요약 조회",
            description = """
                    - 홈 화면에서 사용되는 API입니다.
                    - daysUntilMeeting가 0일 경우, 화면에 '모임 당일' 이라 표시하면 됩니다.
                   \s""",
            security = @SecurityRequirement(name = "Bearer Token")
    )
    @ApiResponse(
            responseCode = "200",
            description = "조회 성공",
            content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = MatchingSummaryDto.class)
            )
    )
    @GetMapping("/summaries")
    MatchingSummaryDto getMatchingSummaryInfo(@AuthenticationPrincipal UserDetails userDetails);

}
