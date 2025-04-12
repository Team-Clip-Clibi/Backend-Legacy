package boot.api.com.clip.api.user.service;

import com.clip.ApiApplication;
import com.clip.api.user.service.UserAccountService;
import com.clip.auth.service.TokenService;
import com.clip.global.config.jwt.JWTProperties;
import com.clip.global.config.jwt.TokenProvider;
import com.clip.infra.aws.s3.S3Config;
import com.clip.infra.aws.s3.S3ImgService;
import com.clip.user.entity.User;
import com.clip.user.repository.UserRepository;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.jackson.io.JacksonDeserializer;
import io.jsonwebtoken.security.Keys;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.Base64;
import java.util.Date;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;

@ContextConfiguration(classes = ApiApplication.class)
@SpringBootTest
public class UserAccountServiceTokenTest {
    @Autowired
    private UserAccountService userAccountService;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private TokenProvider tokenProvider;
    @MockitoBean
    private JWTProperties jwtProperties;
    @MockitoBean
    private TokenService tokenService;
    @MockitoBean
    private S3ImgService s3ImgService;
    @MockitoBean
    private S3Config s3Config;


    @DisplayName("RefreshToken의 기간이 유요하며 TokenType이 RefreshToken이면 1일간 유효한 AccessToken이 발급된다.")
    @Test
    void getAccessToken() {
        //given
        int refreshTokenExpirationPeriodMonth = 1;
        int accessTokenExpirationPeriodDay = 1;
        long userId = 852741963L;
        String secretKey = Base64.getEncoder().encodeToString(Jwts.SIG.HS256.key().build().getEncoded());
        LocalDateTime oneMonthAgo = LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS);
        User user = userRepository.save(User.builder().build());

        given(jwtProperties.getRefreshTokenExpirationPeriodMonth()).willReturn(refreshTokenExpirationPeriodMonth);
        given(jwtProperties.getAccessTokenExpirationPeriodDay()).willReturn(accessTokenExpirationPeriodDay);
        given(jwtProperties.getSecretKey()).willReturn(secretKey);
        String refreshToken = tokenProvider.generateRefreshToken(userId, oneMonthAgo);
        given(tokenService.findRefreshToken(refreshToken)).willReturn(user);

        //when
        TokenProvider.AccessToken accessToken = userAccountService.getAccessToken(new TokenProvider.RefreshToken(refreshToken));

        //then
        Date expiration = Jwts.parser().json(new JacksonDeserializer<>())
                .verifyWith(Keys.hmacShaKeyFor(jwtProperties.getSecretKey().getBytes(StandardCharsets.UTF_8)))
                .build().parseSignedClaims(accessToken.accessToken())
                .getPayload()
                .getExpiration();

        assertThat(Math.abs(expiration.getTime() - Date.from(LocalDateTime.now().plusDays(accessTokenExpirationPeriodDay).atZone(ZoneId.of("Asia/Seoul")).toInstant()).getTime()))
                .isLessThanOrEqualTo(1000);
    }
}
