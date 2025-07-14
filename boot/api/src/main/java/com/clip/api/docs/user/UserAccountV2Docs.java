package com.clip.api.docs.user;

import com.clip.global.config.jwt.TokenProvider;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;

@Tag(name = "회원관리", description = "회원가입, 로그인")
@RequestMapping("/v2/users")
public interface UserAccountV2Docs {
    @Operation(
            summary = "Access Token 재발급 API",
            description = """
                    Refresh Token으로 Access Token을 재발급한다.
                    - 신규 발급된 Access Token과 기존 유효기간 만큼만 동일하게 유효한 Refresh Token을 반환한다.
                    """
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "조회 성공",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = TokenProvider.Token.class)
                    )
            )
    })
    @PostMapping("/tokens")
    TokenProvider.Token getReissueToken(@RequestBody TokenProvider.RefreshToken refreshToken);
}
