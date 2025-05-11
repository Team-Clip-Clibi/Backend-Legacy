package com.clip.api.docs.notification;

import com.clip.api.notification.controller.dto.NotificationBannerDto;
import com.clip.api.notification.controller.dto.NotificationDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "알림", description = "알림 조회, 알림 읽음 처리")
@RequestMapping("/notifications")
public interface UserNotificationDocs {

    @Operation(
            summary = "새로운 알림 조회 API",
            description = """
                    - 최초 조회 시 /notifications/unread 로 호출하며, 이후부터는 /notifications/unread/{lastNotificationId}로 호출합니다.
                    - 이때 lastNotificationId는 마지막 번째 알림의 ID입니다.
                    - 알림은 한번 조회 시 최대 50개를 반환하며, 50개 미만을 경우 다음 페이지의 알림은 존재하지 않습니다.
                    - 다음 페이지의 알림이 존재하지 않는 경우에 마지막 notificationId로 조회하면 않으면 204 No Content를 반환합니다.
                    - 또한 알림은 최신순으로 정렬되어 반환됩니다.
                    """,
            security = @SecurityRequirement(name = "Bearer Token")
    )
    @ApiResponse(
            responseCode = "200",
            description = "조회 성공"
    )
    @GetMapping({"/unread", "/unread/{lastNotificationId}"})
    List<NotificationDto> findUnreadNotifications(@PathVariable(required = false, value = "lastNotificationId") Long lastId,
                                                  @AuthenticationPrincipal UserDetails userDetails);


    @Operation(
            summary = "읽은 알림 조회 API",
            description = """
                    - 최초 조회 시 /notifications/read 로 호출하며, 이후부터는 /notifications/read/{lastNotificationId}로 호출합니다.
                    - 이때 lastNotificationId는 마지막 번째 알림의 ID입니다.
                    - 알림은 한번 조회 시 최대 50개를 반환하며, 50개 미만을 경우 다음 페이지의 알림은 존재하지 않습니다.
                    - 다음 페이지의 알림이 존재하지 않는 경우에 마지막 notificationId로 조회하면 204 No Content를 반환합니다.
                    - 또한 알림은 최신순으로 정렬되어 반환됩니다.
                    """,
            security = @SecurityRequirement(name = "Bearer Token")
    )
    @ApiResponse(
            responseCode = "200",
            description = "조회 성공"
    )
    @GetMapping({"/read", "/read/{lastNotificationId}"})
    List<NotificationDto> findReadNotifications(@PathVariable(required = false, value = "lastNotificationId") Long lastId,
                                                @AuthenticationPrincipal UserDetails userDetails);

    @Operation(
            summary = "알림 읽음 상태 업데이트 API",
            description = """
                    유저가 notificationId에 해당하는 알림을 읽은 경우 해당 API를 호출하여 알림을 읽음 처리합니다.
                    """,
            security = @SecurityRequirement(name = "Bearer Token")
    )
    @ApiResponse(
            responseCode = "200",
            description = "조회 성공"
    )
    @PatchMapping("/status/{notificationId}")
    void updateToReadStatus(@PathVariable(value = "notificationId") Long lastId,
                                                @AuthenticationPrincipal UserDetails userDetails);

    @Operation(
            summary = "홈 화면 알림 배너 조회 API",
            description = """
                    - 홈 화면에서 알림 배너를 조회합니다.
                    - 알림 배너 타입은 3가지로, MATCHING(매칭 완료 안내), MATCHING_INFO(매칭 안내문 확인 안내), REVIEW(리뷰 작성 안내)입니다.
                    """,
            security = @SecurityRequirement(name = "Bearer Token")
    )
    @ApiResponse(
            responseCode = "200",
            description = "조회 성공"
    )
    @GetMapping("/banner")
    List<NotificationBannerDto> findNotificationBanners(@AuthenticationPrincipal UserDetails userDetails);

}
