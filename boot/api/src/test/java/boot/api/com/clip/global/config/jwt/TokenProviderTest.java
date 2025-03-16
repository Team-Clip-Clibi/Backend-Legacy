package boot.api.com.clip.global.config.jwt;

import com.clip.global.config.jwt.JWTProperties;
import com.clip.global.config.jwt.TokenProvider;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.jackson.io.JacksonDeserializer;
import io.jsonwebtoken.security.Keys;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.Base64;
import java.util.Date;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class TokenProviderTest {

    @Mock
    private JWTProperties jwtProperties;
    @InjectMocks
    private TokenProvider tokenProvider;

    @DisplayName("Access Token은 yml에 설정한 AccessTokenExpirationPeriodDay 일(day) 뒤 만료된다")
    @Test
    void generateAccessToken() {
        //given
        int accessTokenExpirationPeriodDay = 1;
        long userId = 852741963L;
        String secretKey = Base64.getEncoder().encodeToString(Jwts.SIG.HS256.key().build().getEncoded());
        LocalDateTime currentDateTime = LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS);

        given(jwtProperties.getAccessTokenExpirationPeriodDay()).willReturn(accessTokenExpirationPeriodDay);
        given(jwtProperties.getSecretKey()).willReturn(secretKey);

        //when
        String accessToken = tokenProvider.generateAccessToken(userId, currentDateTime);

        //then
        Date expiration = Jwts.parser().json(new JacksonDeserializer<>())
                .verifyWith(Keys.hmacShaKeyFor(jwtProperties.getSecretKey().getBytes(StandardCharsets.UTF_8)))
                .build().parseSignedClaims(accessToken)
                .getPayload().getExpiration();

        assertThat(expiration).isEqualTo(Date.from(currentDateTime.plusDays(accessTokenExpirationPeriodDay).atZone(ZoneId.of("Asia/Seoul")).toInstant()));

    }

    @DisplayName("Refresh Token은 yml에 설정한 RefreshTokenExpirationPeriodMonth 달(month) 뒤 만료된다")
    @Test
    void generateRefreshToken() {
        //given
        int refreshTokenExpirationPeriodDay = 1;
        long userId = 852741963L;
        String secretKey = Base64.getEncoder().encodeToString(Jwts.SIG.HS256.key().build().getEncoded());
        LocalDateTime currentDateTime = LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS);

        given(jwtProperties.getRefreshTokenExpirationPeriodMonth()).willReturn(refreshTokenExpirationPeriodDay);
        given(jwtProperties.getSecretKey()).willReturn(secretKey);

        //when
        String refreshToken = tokenProvider.generateRefreshToken(userId, currentDateTime);

        //then
        Date expiration = Jwts.parser().json(new JacksonDeserializer<>())
                .verifyWith(Keys.hmacShaKeyFor(jwtProperties.getSecretKey().getBytes(StandardCharsets.UTF_8)))
                .build().parseSignedClaims(refreshToken)
                .getPayload().getExpiration();

        assertThat(expiration).isEqualTo(Date.from(currentDateTime.plusMonths(refreshTokenExpirationPeriodDay).atZone(ZoneId.of("Asia/Seoul")).toInstant()));

    }
}