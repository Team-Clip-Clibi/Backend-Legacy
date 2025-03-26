package com.clip.office.admin.client.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class TokenProviderApiDto {
    private String accessToken;
    private String refreshToken;
}
