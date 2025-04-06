package com.clip.api.docs.matching;

import com.clip.api.matching.controller.dto.MatchingReviewDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

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
            @PathVariable final String matchingType,
            @RequestBody final MatchingReviewDto request,
            @AuthenticationPrincipal final UserDetails userDetails);
}
