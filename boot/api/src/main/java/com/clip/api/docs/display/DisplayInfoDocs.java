package com.clip.api.docs.display;

import com.clip.api.display.controller.dto.BannerInfoDto;
import com.clip.notice.entity.BannerType;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Tag(name = "화면 컨텐츠 관리", description = "화면 컨텐츠 관리 API")
@RequestMapping("/displays")
public interface DisplayInfoDocs {

    @Operation(
            summary = "배너 조회 API",
            description = """
                    - 배너 목록을 조회합니다.
                    - 배너 종류는 HOME, LOGIN 으로 구분됩니다.
                    """
    )
    @ApiResponse(
            responseCode = "200",
            description = "조회 성공",
            content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = BannerInfoDto.class)
            )
    )
    @GetMapping("/banners/{bannerType}")
    List<BannerInfoDto> getBanners(@PathVariable final BannerType bannerType);


}
