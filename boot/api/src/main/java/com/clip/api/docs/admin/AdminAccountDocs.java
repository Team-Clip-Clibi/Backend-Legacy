package com.clip.api.docs.admin;

import com.clip.api.admin.controller.dto.AdminLoginDto;
import com.clip.global.config.jwt.TokenProvider;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;

@Tag(name = "오피스 관련", description = "어드민 로그인,")
@RequestMapping("/office")
public interface AdminAccountDocs {
    @Operation(
            summary = "Office 로그인",
            description = """
                    로그인 한 유저가 어드민 권한일 경우 로그인 가능합니다. 
                    response : usernamer, password
                    """
    )
    @ApiResponse(
            responseCode = "200",
            description = "로그인 완료",
            content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = AdminLoginDto.class)
            )
    )
    @ApiResponse(
            responseCode = "400",
            description = "로그인 실패"
    )
    @PostMapping("/login")
    TokenProvider.AdminToken loginAdminAccount(@RequestBody AdminLoginDto adminLoginDto);
}
