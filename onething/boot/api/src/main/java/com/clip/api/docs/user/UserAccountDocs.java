package com.clip.api.docs.user;

import com.clip.api.user.controller.dto.*;
import com.clip.global.config.jwt.TokenProvider;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@Tag(name = "회원관리", description = "회원가입, 로그인, 전화번호 업데이트 etc")
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
    TokenProvider.Token createUserAccount(@RequestBody SignupDto request);

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
    TokenProvider.Token loginUserAccount(@RequestBody LoginDto request);

    @Operation(
            summary = "번호 업데이트 API",
            description = """
                    유저의 번호를 업데이트합니다.
                    "꼭 공백과 - 를 제거하고 보내주세요. ex) 01012345678"
                    다른 유저가 사용중인 번호일 경우 HTTP 400 Bad Request를 반환합니다.
                    """
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "번호 업데이트 성공"
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "번호 업데이트 실패"
            )
    })
    @PatchMapping("/phone")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    void updatePhoneNumber(@RequestBody UpdatePhoneNumberDto updatePhoneNumberDto,
                                           @AuthenticationPrincipal UserDetails userDetails);

    @Operation(
            summary = "이름 업데이트 API",
            description = """
                    유저의 이름을 업데이트합니다.
                    """
    )
    @ApiResponse(
            responseCode = "200",
            description = "유저 이름 업데이트 성공"
    )
    @PatchMapping("/name")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    void updateName(@RequestBody UpdateNameDto updateNameDto,
                                    @AuthenticationPrincipal UserDetails userDetails);

    @Operation(
            summary = "닉네임 업데이트 API",
            description = """
                    유저의 닉네임을 업데이트합니다.
                    다른 유저가 사용중인 닉네임일 경우 HTTP 400 Bad Request를 반환합니다.
                    """
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "닉네임 업데이트 성공"
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "닉네임 업데이트 실패"
            )
    })
    @PatchMapping("/nickname")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    void updateNickname(@RequestBody UpdateNicknameDto updateNicknameDto,
                                        @AuthenticationPrincipal UserDetails userDetails);

    @GetMapping("/{phoneNumber}/info")
    UserInfoDto getUserInfo(@PathVariable String phoneNumber);
}
