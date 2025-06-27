package com.clip.api.docs.matching;

import com.clip.api.matching.controller.dto.*;
import com.clip.matching.entity.RandomMatchingStatus;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

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

    @Operation(
            summary = "모임 현황 조회",
            description = """
                     - 내 모임 화면에서 사용되는 API입니다.
                     - 다음 모임 날짜 및 신청 완료 & 매칭 확정 모임 정보 및 안내문 전체 조회 여부 값을 조회하는 API입니다.
                    \s""",
            security = @SecurityRequirement(name = "Bearer Token")
    )
    @ApiResponse(
            responseCode = "200",
            description = "조회 성공",
            content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = MatchingOverviewDto.class)
            )
    )
    @GetMapping("/overview")
    MatchingOverviewDto getMatchingOverview(@AuthenticationPrincipal UserDetails userDetails);

    @Operation(
            summary = "모임 리스트 조회 API",
            description = """
                    - 모임 리스트를 조회합니다.
                    - 모임 리스트는 매칭 상태에 따라 구분됩니다.
                    - 매칭 상태는 APPLIED, CONFIRMED, COMPLETED, CANCELED 로 구분됩니다.
                    - 전체 조회 시, 파라미터 없이 요청합니다.
                    - 다음 페이지의 매칭이 존재하지 않는 경우에 마지막 meetingTime으로 조회 시 204 No Content를 반환합니다.
                    - 또한 매칭은 최신순으로 정렬되어 반환됩니다.
                    - isReviewWritten 필드는 매칭이 완료되었고, 후기를 작성한 경우에만 true로 반환됩니다. default는 false입니다.
                   \s""",
            security = @SecurityRequirement(name = "Bearer Token")
    )
    @ApiResponse(
            responseCode = "200",
            description = "조회 성공"
    )
    @ApiResponse(
            responseCode = "204",
            description = "조회 성공, 다음 페이지 없음"
    )
    @GetMapping
    List<MatchingDto> findMatchings(
            @RequestParam(required = false) RandomMatchingStatus status,
            @RequestParam(required = false) LocalDateTime lastMeetingTime,
            @AuthenticationPrincipal UserDetails userDetails
    );

    @Operation(
            summary = "모임 상세 조회 API",
            description = """
                    - 모임의 상세 정보를 조회합니다.
                    - 모임 ID를 통해 해당 모임의 상세 정보를 가져옵니다.
                    - 반환된 정보에는 모임의 시간, 신청 정보, 나의 모임 정보 등이 포함됩니다.
                    - 매칭 타입으로는 ONE_THING, RANDOM이 있습니다.
                   \s""",
            security = @SecurityRequirement(name = "Bearer Token")
    )
    @ApiResponse(
            responseCode = "200",
            description = "조회 성공",
            content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(
                            implementation = MatchingDetailDto.class,
                            oneOf = {OneThingMatchingDetailDto.class, RandomMatchingDetailDto.class},
                            discriminatorProperty = "matchingType"
                    )
            )
    )
    @GetMapping("/{matchingType}/{id}")
    MatchingDetailDto getMatchingDetail(
            @AuthenticationPrincipal UserDetails userDetails,
            @PathVariable MatchingType matchingType,
            @PathVariable long id
    );

    @Operation(
            summary = "모임 신청 취소 API",
            description = """
                    - 모임 신청을 취소합니다.
                    - 모임 ID를 통해 해당 모임의 신청을 취소합니다.
                    - 매칭 타입으로는 ONE_THING, RANDOM이 있습니다.
                   \s""",
            security = @SecurityRequirement(name = "Bearer Token")
    )
    @ApiResponse(
            responseCode = "204",
            description = "취소 성공"
    )
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PatchMapping("/{matchingType}/{id}")
    void cancelMatching(
            @AuthenticationPrincipal UserDetails userDetails,
            @PathVariable MatchingType matchingType,
            @PathVariable long id
    );

    @Operation(
            summary = "모임 지각 상태 업데이트 및 FCM 알림 전송 API",
            description = """
                    - 모임 ID를 통해 해당 모임의 지각 상태를 업데이트하고 FCM 알림을 전송합니다.
                    - 매칭 타입으로는 ONE_THING, RANDOM이 있습니다.
                   \s""",
            security = @SecurityRequirement(name = "Bearer Token")
    )
    @ApiResponse(
            responseCode = "204",
            description = "업데이트 성공"
    )
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PostMapping("/{matchingType}/{id}")
    void updateLastMinutesAndSendNotification(
            @AuthenticationPrincipal UserDetails userDetails,
            @PathVariable MatchingType matchingType,
            @PathVariable long id,
            @RequestBody LateMinutesUpdateDto lateMinutesUpdateDto
    );

    @Operation(
            summary = "모임 안내문 API",
            description = """
                    - 모임 안내문을 조회합니다.
                    - 다음 페이지의 매칭이 존재하지 않는 경우에 마지막 meetingTime으로 조회 시 204 No Content를 반환합니다.
                    - 또한 매칭은 최신순으로 정렬되어 반환됩니다.
                   \s""",
            security = @SecurityRequirement(name = "Bearer Token")
    )
    @ApiResponse(
            responseCode = "200",
            description = "조회 성공"
    )
    @ApiResponse(
            responseCode = "204",
            description = "조회 성공, 다음 페이지 없음"
    )
    @GetMapping("/notices")
    List<MatchingNoticeDto> getMatchingNotice(
            @RequestParam(required = false) LocalDateTime lastMeetingTime,
            @AuthenticationPrincipal UserDetails userDetails
    );

}