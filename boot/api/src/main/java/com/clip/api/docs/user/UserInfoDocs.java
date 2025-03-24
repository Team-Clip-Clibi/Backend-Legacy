package com.clip.api.docs.user;

import com.clip.api.user.controller.dto.*;
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

@Tag(name = "회원 정보 관리", description = "전화번호 업데이트 etc")
@RequestMapping("/users")
public interface UserInfoDocs {

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
                    responseCode = "204",
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
            responseCode = "204",
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
                    responseCode = "204",
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

    @Operation(
            summary = "사용 가능한 닉네임 확인 API",
            description = """
                    해당 닉네임이 사용 가능한지 확인합니다.
                    다른 유저가 사용중인 닉네임일 경우 HTTP 400 Bad Request를 반환합니다.
                    """
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "사용 가능한 닉네임"
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "사용 불가능한 닉네임"
            )
    })
    @PostMapping("/nickname/available")
    void checkNicknameAvailable(@RequestBody UpdateNicknameDto updateNicknameDto);

    @Operation(
            summary = "유저 상세 정보 업데이트 API",
            description = """
                    유저의 성별, 생년월일, 활동지역을 업데이트합니다.
                    """
    )
    @ApiResponse(
            responseCode = "204",
            description = "디테일 정보 업데이트 성공"
    )
    @PatchMapping("/detail")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    void updateUserDetailInfo(@RequestBody UpdateUserDetailInfoDto updateUserDetailInfoDto,
                              @AuthenticationPrincipal UserDetails userDetails);

    @Operation(
            summary = "프로필 기본 정보 조회 API",
            description = """
                    마이페이지의 프로필 기본 정보를 조회합니다.
                    """
    )
    @ApiResponse(
            responseCode = "200",
            description = "조회 성공",
            content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = RetrieveUserProfileInfo.class)
            )
    )
    @GetMapping("/profile")
    RetrieveUserProfileInfo getProfileInfo(@AuthenticationPrincipal UserDetails userDetails);

    @Operation(
            summary = "FCM 업데이트 API",
            description = """
                    유저의 FCM을 업데이트합니다.
                    """
    )
    @ApiResponse(
            responseCode = "204",
            description = "FCM 업데이트 성공"
    )
    @PatchMapping("/fcm")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    void updateFCMToken(@RequestBody UpdateFCMDto updateFCMDto,
                        @AuthenticationPrincipal UserDetails userDetails);

    @Operation(
            summary = "알림 on/off API",
            description = """
                    유저의 알림 설정을 업데이트합니다.
                    """
    )
    @ApiResponse(
            responseCode = "204",
            description = "업데이트 성공"
    )
    @PatchMapping("/notify")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    void updateNotifyAllow(@RequestBody UpdateNotifyAllowDto updateNotifyAllowDto,
                           @AuthenticationPrincipal UserDetails userDetails);

    @Operation(
            summary = "하는 일 변경 API",
            description = """
                    유저의 하는 일 정보를 변경합니다.
                    """
    )
    @ApiResponse(
            responseCode = "204",
            description = "업데이트 성공"
    )
    @PatchMapping("/job")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    void updateJob(@RequestBody JobDto jobDto,
                   @AuthenticationPrincipal UserDetails userDetails);

    @Operation(
            summary = "하는 일 조회 API",
            description = """
                    유저의 하는 일 정보를 조회합니다.
                    """
    )
    @ApiResponse(
            responseCode = "200",
            description = "조회 성공",
            content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = JobDto.class)
            )
    )
    @GetMapping("/job")
    JobDto getJob(@AuthenticationPrincipal UserDetails userDetails);

    @Operation(
            summary = "연애 상태 변경 API",
            description = """
                    유저의 연애 상태 정보를 변경합니다.
                    """
    )
    @ApiResponse(
            responseCode = "204",
            description = "업데이트 성공"
    )
    @PatchMapping("/relationship")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    void updateRelationship(@RequestBody RelationshipDto relationshipDto,
                            @AuthenticationPrincipal UserDetails userDetails);

    @Operation(
            summary = "연애 상태 변경 조회 API",
            description = """
                    유저의 연애 상태 정보를 조회합니다.
                    """
    )
    @ApiResponse(
            responseCode = "200",
            description = "조회 성공",
            content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = RelationshipDto.class)
            )
    )
    @GetMapping("/relationship")
    RelationshipDto getRelationship(@AuthenticationPrincipal UserDetails userDetails);

    @Operation(
            summary = "식단 제한 변경 API",
            description = """
                    유저의 식단 제한 정보를 변경합니다.
                    """
    )
    @ApiResponse(
            responseCode = "204",
            description = "업데이트 성공"
    )
    @PatchMapping("/dietary")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    void updateDietaryOption(@RequestBody DietaryDto dietaryDto,
                             @AuthenticationPrincipal UserDetails userDetails);

    @Operation(
            summary = "식단 제한 조회 API",
            description = """
                    유저의 식단 제한 정보를 조회합니다.
                    """
    )
    @ApiResponse(
            responseCode = "200",
            description = "조회 성공",
            content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = DietaryDto.class)
            )
    )
    @GetMapping("/dietary")
    DietaryDto getDietaryOption(@AuthenticationPrincipal UserDetails userDetails);

    @Operation(
            summary = "사용 언어 변경 API",
            description = """
                    유저의 사용 언어 정보를 변경합니다.
                    """
    )
    @ApiResponse(
            responseCode = "204",
            description = "업데이트 성공"
    )
    @PatchMapping("/language")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    void updateLanguage(@RequestBody LanguageDto languageDto,
                        @AuthenticationPrincipal UserDetails userDetails);

    @Operation(
            summary = "사용 언어 조회 API",
            description = """
                    유저의 사용 언어 정보를 조회합니다.
                    """
    )
    @ApiResponse(
            responseCode = "200",
            description = "조회 성공",
            content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = LanguageDto.class)
            )
    )
    @GetMapping("/language")
    LanguageDto getLanguage(@AuthenticationPrincipal UserDetails userDetails);
}
