package com.clip.auth.service;

import com.clip.auth.entity.Token;
import com.clip.auth.repository.TokenRepository;
import com.clip.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TokenService {
    private final TokenRepository tokenRepository;

    public Token save(Token token) {
        return tokenRepository.save(token);
    }

    public void updateRefreshToken(User user, String refreshToken) {
        tokenRepository.updateRefreshToken(user, refreshToken);
    }
}
