package com.clip.api.docs.matching;

import com.clip.api.matching.controller.dto.MatchingReviewDto;
import com.clip.api.matching.controller.dto.MatchingReviewPopupDto;
import com.clip.api.matching.controller.dto.MatchingType;
import com.clip.api.matching.controller.dto.ParticipantsInfoDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "매칭 후기 관리", description = "매칭 후기 등록, 후기 작성 여부 조회 etc ")
@RequestMapping("/reviews")
public interface MatchingReviewDocs {
    @Operation(
            summary = "매칭 후기 작성 API",
            description = """
                    - 매칭 후기를 작성합니다.
                    - 매칭 후기 작성 시, 자세한 경험(reviewContent)과 전원 참석 여부(isMemberAllAttended) 및 불참자(noShowMembers)는 선택 입력, 나머지는 필수 입력입니다.
                    - matchingType은 매칭 종류를 나타내며,'RANDOM' 또는 'ONE_THING'둘 중 하나로 보내주세요.
                    - mood ENUM 값은 다음과 같습니다.   \s
                        - DISAPPOINTED
                        - UNSATISFIED
                        - NEUTRAL
                        - GOOD
                        - EXCELLENT
                   \s""",
            security = @SecurityRequirement(name = "Bearer Token")
    )
    @ApiResponse(
            responseCode = "200",
            description = "등록 성공"
    )
    @PostMapping("/{matchingId}/{matchingType}")
    void createMatchingReview(
            @PathVariable final Long matchingId,
            @PathVariable final MatchingType matchingType,
            @RequestBody final MatchingReviewDto request,
            @AuthenticationPrincipal final UserDetails userDetails);

    @Operation(
            summary = "매칭 참여자 리스트 조회 API"
            , description = """
                    - 매칭 참여자 리스트를 조회합니다.
                    - matchingType은 매칭 종류를 나타내며, 'RANDOM' 또는 'ONE_THING' 둘 중 하나로 보내주세요.
                    - matchingId는 매칭의 고유 ID입니다. 내 모임에서 후기 작성하기 버튼을 누를 경우, 응답 필드에 보내드린 matchingId를 사용하여 해당 API를 호출해주세요.
                   \s"""
    )
    @ApiResponse(
            responseCode = "200",
            description = "조회 성공"
    )
    @GetMapping("/{matchingId}/{matchingType}/participants")
    List<ParticipantsInfoDto> getMatchingParticipants(
            @PathVariable final Long matchingId,
            @PathVariable final MatchingType matchingType,
            @AuthenticationPrincipal final UserDetails userDetails);

    @Operation(
            summary = "작성해야할 후기 팝업 조회 API"
            , description = """
                    - 작성해야할 매칭 후기 팝업을 조회합니다.
                    - matchingType은 매칭 종류를 나타내며, 'RANDOM' 또는 'ONE_THING' 둘 중 하나로 보내주세요.
                    - matchingId는 매칭의 고유 ID입니다. 응답 필드에 보내드린 matchingId를 매칭 후기 작성 API,
                      매칭 참여자 리스트 조회 API에 사용해주세요.
                   \s"""
    )
    @ApiResponse(
            responseCode = "200",
            description = "조회 성공"
    )
    @GetMapping
    List<MatchingReviewPopupDto> getMatchingReviewPopupInfo(
            @AuthenticationPrincipal final UserDetails userDetails
    );

    @Operation(
            summary = "작성해야할 후기 팝업 다음에 작성하기 API",
            description = """
                    - 작성해야할 매칭 후기 팝업에서 다음에 작성하기 버튼을 누르면 해당 API를 호출합니다.
                   \s"""
    )
    @ApiResponse(
            responseCode = "200",
            description = "다음에 작성하기 성공"
    )
    @PatchMapping("/{matchingId}/{matchingType}/postpone")
    void postponeMatchingReview(
            @PathVariable final Long matchingId,
            @PathVariable final MatchingType matchingType,
            @AuthenticationPrincipal final UserDetails userDetails
    );
}
