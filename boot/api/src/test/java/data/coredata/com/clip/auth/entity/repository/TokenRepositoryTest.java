package data.coredata.com.clip.auth.entity.repository;

import com.clip.ApiApplication;
import com.clip.auth.entity.Token;
import com.clip.auth.repository.TokenRepository;
import com.clip.user.entity.User;
import com.clip.user.repository.UserRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ContextConfiguration;

@ContextConfiguration(classes = ApiApplication.class)
@DataJpaTest
public class TokenRepositoryTest {
    @Autowired
    TokenRepository tokenRepository;
    @Autowired
    UserRepository userRepository;

    @DisplayName("User로 token table의 refresh token을 업데이트 한다.")
    @Test
    void updateRefreshToken() {
        //given
        User user = userRepository.save(User.builder().build());
        tokenRepository.save(Token.builder().user(user).refreshToken("refresh1").build());

        //when
        tokenRepository.updateRefreshToken(user,"refresh2");
        String refreshToken = tokenRepository.findById(user.getId()).get().getRefreshToken();
        //then
        Assertions.assertThat(refreshToken).isEqualTo("refresh2");

    }
}
