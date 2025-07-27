package com.clip.api.docs.user;

import com.clip.api.user.controller.dto.LoginDtoV2;
import com.clip.api.user.controller.dto.SignupDtoV2;
import com.clip.global.config.jwt.TokenProvider;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
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

    @Operation(
            summary = "회원가입 API",
            description = """
                    소셜로그인 후 필수 약관에 모두 동의하면 소셜id(불변값)와 플랫폼(KAKAO or APPLE) 정보, 약관 동의 내역을 저장하고,
                    Access Token과 Refresh Token을 발급하여 반환합니다.
                    """
    )
    @ApiResponse(
            responseCode = "200",
            description = "회원 가입 성공",
            content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = TokenProvider.Token.class)
            )
    )
    @PostMapping("/signup")
    TokenProvider.Token createUserAccount(@RequestBody @Valid SignupDtoV2 request);

    @Operation(
            summary = "로그인 API",
            description = """
                    소셜id(불변값)와 플랫폼(KAKAO or APPLE) 정보를 기반으로 회원정보를 검색하여
                    Access Token과 Refresh Token을 발급하여 반환합니다.
                    단, 가입되지 않은 유저일 경우 HTTP 400 Bad Request를 반환합니다.
                    """
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "로그인 성공",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = TokenProvider.Token.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "로그인 실패",
                    content = @Content(
                            schema = @Schema()
                    )
            )
    })
    @PostMapping("/signin")
    TokenProvider.Token loginUserAccount(@RequestBody @Valid LoginDtoV2 request);
}
