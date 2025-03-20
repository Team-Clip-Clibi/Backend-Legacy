package com.clip.api.docs.matching;

import com.clip.api.matching.controller.dto.CreateRandomMatchingDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@Tag(name = "매칭관리", description = "매칭 생성, 참가, 조회 ")
@RequestMapping("/match")
public interface MatchingDocs {
    @Operation(
            summary = "",
            description = """
                    모임을 생성하는 API(백오피스 전용)
                    Response : 지역(시/도), 상세주소(레스토랑주소), 레스토랑 이름, 시간
                    """
    )
    @ApiResponse(
            responseCode = "200",
            description = "모임 생성 완료",
            content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = CreateRandomMatchingDto.class)
            )
    )
    @ApiResponse(
            responseCode = "400",
            description = "모임 생성 실패"
    )
    @PostMapping("/random/create")
    CreateRandomMatchingDto createRandomCommunity(@RequestBody CreateRandomMatchingDto createRandomMatchingDto);
}
