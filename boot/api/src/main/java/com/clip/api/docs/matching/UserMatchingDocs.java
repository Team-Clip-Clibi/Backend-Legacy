package com.clip.api.docs.matching;

import com.clip.api.matching.controller.dto.MatchingProgressStatusDto;
import com.clip.api.matching.controller.dto.MatchingSummaryDto;
import com.clip.api.matching.controller.dto.MatchingType;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

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

    @Operation(
            summary = "진행중인 모임 정보 조회",
            description = """
                    - 홈 화면에서 사용되는 API입니다.
                    - 진행중인 모임이 있는 경우 홈화면 하단에 노출되는 정보를 조회하는 API입니다.
                   \s""",
            security = @SecurityRequirement(name = "Bearer Token")
    )
    @ApiResponse(
            responseCode = "200",
            description = "조회 성공",
            content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = MatchingProgressStatusDto.class)
            )
    )
    @GetMapping("/progress-status")
    MatchingProgressStatusDto getMatchingStatus(@AuthenticationPrincipal UserDetails userDetails);

    @Operation(
            summary = "진행중인 모임 정보 조회 읽음 상태로 변경",
            description = """
                     - 홈 화면에서 사용되는 API입니다.
                     - 진행중인 모임이 있는 경우 홈화면 하단에 노출되는 정보를 조회한 상태로 업데이트하는 API입니다.
                     - URL 경로의 matchingType은 ONE_THING, RANDOM 중 하나입니다.
                    \s""",
            security = @SecurityRequirement(name = "Bearer Token")
    )
    @ApiResponse(
            responseCode = "204",
            description = "업데이트 성공"
    )
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PatchMapping("/{matchingType}/{matchingId}/progress-status/check")
    void updateMatchingStatusChecked(@PathVariable MatchingType matchingType,
                                     @PathVariable long matchingId,
                                     @AuthenticationPrincipal UserDetails userDetails);
}