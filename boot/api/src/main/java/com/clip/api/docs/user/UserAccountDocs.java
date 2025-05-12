package com.clip.api.docs.user;

import com.clip.api.user.controller.dto.*;
import com.clip.global.config.jwt.TokenProvider;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@Tag(name = "회원관리", description = "회원가입, 로그인")
@RequestMapping("/users")
public interface UserAccountDocs {


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
    TokenProvider.Token createUserAccount(@RequestBody @Valid SignupDto request);

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
    TokenProvider.Token loginUserAccount(@RequestBody @Valid LoginDto request);

    @Operation(
            summary = "번호로 가입된 계정 조회 API",
            description = """
                    휴대폰 번호로 기존에 가입된 계정의 정보를 조회합니다.
                    """,
            security = @SecurityRequirement(name = "Bearer Token")
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "조회 성공",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = UserInfoDto.class)
                    )
            )
    })
    @GetMapping("/{phoneNumber}/info")
    UserInfoDto getUserInfo(@PathVariable String phoneNumber, @AuthenticationPrincipal UserDetails userDetails);

    @Operation(
            summary = "Access Token 재발급 API",
            description = """
                    Refresh Token으로 Access Token을 재발급한다.
                    """
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "조회 성공",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = TokenProvider.AccessToken.class)
                    )
            )
    })
    @PostMapping("/tokens")
    TokenProvider.AccessToken getAccessToken(@RequestBody TokenProvider.RefreshToken refreshToken);

    @Operation(
            summary = "회원 탈퇴 API",
            description = """
                    Refresh Token으로 회원탈퇴를 진행한다. Authorization에 Refresh Token을 넣어준다.
                    """
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "204",
                    description = "탈퇴 성공"
            )
    })
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/me")
    void deleteUser(@RequestHeader("Authorization") String authorizationHeader);
}
