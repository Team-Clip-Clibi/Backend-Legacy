package boot.api.com.clip.api.user.service;

import com.clip.ApiApplication;
import com.clip.api.user.controller.dto.LoginDto;
import com.clip.api.user.controller.dto.SignupDto;
import com.clip.api.user.controller.dto.UpdateUserDetailInfoDto;
import com.clip.api.user.exception.NotFoundUserException;
import com.clip.api.user.service.UserAccountService;
import com.clip.auth.entity.Token;
import com.clip.auth.repository.TokenRepository;
import com.clip.global.config.jwt.JWTProperties;
import com.clip.global.config.jwt.TokenProvider;
import com.clip.user.entity.*;
import com.clip.user.exception.NicknameAlreadyExistsException;
import com.clip.user.exception.PhoneNumberAlreadyExistsException;
import com.clip.user.repository.UserRepository;
import com.clip.user.service.UserService;
import io.jsonwebtoken.Jwts;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import java.time.LocalDate;
import java.util.Base64;
import java.util.Collection;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.DynamicTest.dynamicTest;
import static org.mockito.BDDMockito.given;

@ContextConfiguration(classes = ApiApplication.class)
@SpringBootTest
public class UserAccountServiceTest {

    @Autowired
    private UserAccountService userAccountService;
    @Autowired
    private UserService userService;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    TokenProvider tokenProvider;
    @Autowired
    TokenRepository tokenRepository;
    @MockitoBean
    JWTProperties jwtProperties;

    @AfterEach
    void tearDown() {
        userRepository.deleteAllInBatch();
    }

    @DisplayName("SocialId와 Platform으로 User를 생성 후 UserId가 담긴 JWT를 반환한다.")
    @Test
    void signup() {
        //given
        String socialId = "socialId";
        Platform platform = Platform.APPLE;
        String secretKey = Base64.getEncoder().encodeToString(Jwts.SIG.HS256.key().build().getEncoded());
        SignupDto signupDto = SignupDto.builder()
                .marketingPermission(true)
                .servicePermission(true)
                .privatePermission(true)
                .socialId(socialId)
                .platform(platform)
                .build();
        given(jwtProperties.getSecretKey()).willReturn(secretKey);
        given(jwtProperties.getAccessTokenExpirationPeriodDay()).willReturn(1);

        //when
        TokenProvider.Token token = userAccountService.signup(signupDto);
        String userId = tokenProvider.extractUserId(token.accessToken());
        User user = userService.findOptUser(socialId, platform).get();

        //then
        assertThat(Long.parseLong(userId)).isEqualTo(user.getId());
    }

    @DisplayName("회원가입 시나리오")
    @TestFactory
    Collection<DynamicTest> signupDynamicTests() {
        //given
        String secretKey = Base64.getEncoder().encodeToString(Jwts.SIG.HS256.key().build().getEncoded());
        given(jwtProperties.getSecretKey()).willReturn(secretKey);
        given(jwtProperties.getAccessTokenExpirationPeriodDay()).willReturn(1);

        String socialId = "socialId";
        Platform platform = Platform.APPLE;

        return List.of(dynamicTest("가입되지 않은 유저가 회원가입 요청 시 해당 회원 정보를 저장한다. ",()->{
            //given
            DeviceType deviceType = DeviceType.IOS;
            String osVersion = "14.0";
            String firebaseToken = "firebaseToken";
            SignupDto signupDto = SignupDto.builder()
                    .marketingPermission(true)
                    .servicePermission(true)
                    .privatePermission(true)
                    .socialId(socialId)
                    .platform(platform)
                    .deviceType(deviceType)
                    .osVersion(osVersion)
                    .firebaseToken(firebaseToken)
                    .build();

            //when
            TokenProvider.Token responseToken = userAccountService.signup(signupDto);
            long userId = Long.parseLong(tokenProvider.extractUserId(responseToken.accessToken()));
            User user = userRepository.findById(userId).get();
            Token token = tokenRepository.findToken(userId).get();

            //then
            assertThat(responseToken.refreshToken()).isEqualTo(token.getRefreshToken());
            assertThat(user).extracting(
                    User::getSocialId,
                    User::getPlatform,
                    User::getDeviceType,
                    User::getOsVersion,
                    User::getFirebaseToken
            ).containsExactly(
                    socialId,
                    platform,
                    deviceType,
                    osVersion,
                    firebaseToken
            );

        }), dynamicTest("기가입 유저가 회원가입 요청 시 회원 정보를 업데이트한다.",()->{

            DeviceType deviceType = DeviceType.ANDROID;
            String osVersion = "20.0";
            String firebaseToken = "firebaseTokenV2";
            SignupDto signupDto = SignupDto.builder()
                    .marketingPermission(true)
                    .servicePermission(true)
                    .privatePermission(true)
                    .socialId(socialId)
                    .platform(platform)
                    .deviceType(deviceType)
                    .osVersion(osVersion)
                    .firebaseToken(firebaseToken)
                    .build();

            //when
            TokenProvider.Token responseToken = userAccountService.signup(signupDto);
            long userId = Long.parseLong(tokenProvider.extractUserId(responseToken.accessToken()));
            User user = userRepository.findById(userId).get();
            Token token = tokenRepository.findToken(userId).get();

            //then
            assertThat(responseToken.refreshToken()).isEqualTo(token.getRefreshToken());
            assertThat(user).extracting(
                    User::getSocialId,
                    User::getPlatform,
                    User::getDeviceType,
                    User::getOsVersion,
                    User::getFirebaseToken
            ).containsExactly(
                    socialId,
                    platform,
                    deviceType,
                    osVersion,
                    firebaseToken
            );

        }));


    }

    @DisplayName("SocialId와 Platform으로 User를 찾아 JWT를 반환한다.")
    @Test
    void login() {
        //given
        String socialId = "socialId";
        Platform platform = Platform.APPLE;
        String secretKey = Base64.getEncoder().encodeToString(Jwts.SIG.HS256.key().build().getEncoded());
        userService.save(User.builder()
                .socialId(socialId)
                .platform(platform)
                .build());
        given(jwtProperties.getSecretKey()).willReturn(secretKey);
        given(jwtProperties.getAccessTokenExpirationPeriodDay()).willReturn(1);

        //when
        TokenProvider.Token token = userAccountService.login(LoginDto.builder().platform(platform).socialId(socialId).build());
        String userId = tokenProvider.extractUserId(token.accessToken());
        User user = userService.findOptUser(socialId, platform).get();

        //then
        assertThat(Long.parseLong(userId)).isEqualTo(user.getId());
    }

    @DisplayName("SocialId와 Platform으로 User를 검색 후 기가입 유저가 아니면 NotFoundUserException이 발생한다.")
    @Test
    void loginFail() {
        //given
        String socialId = "socialId";
        Platform platform = Platform.APPLE;
        String secretKey = Base64.getEncoder().encodeToString(Jwts.SIG.HS256.key().build().getEncoded());

        given(jwtProperties.getSecretKey()).willReturn(secretKey);
        given(jwtProperties.getAccessTokenExpirationPeriodDay()).willReturn(1);

        //when & then
        assertThatThrownBy(() -> userAccountService.login(LoginDto.builder().platform(platform).socialId(socialId).build()))
                .isInstanceOf(NotFoundUserException.class);
    }

    @DisplayName("userId로 phoneNumber를 업데이트 한다.")
    @Test
    void updatePhoneNumber() {
        //given
        String phoneNumber = "01012345678";
        User user = userService.save(User.builder().build());

        //when
        userAccountService.updatePhoneNumber(user.getId(), phoneNumber);
        user = userService.findUser(phoneNumber);

        //then
        assertThat(user.getPhoneNumber()).isEqualTo(phoneNumber);
    }

    @DisplayName("다른 유저가 사용중인 번호로로 업데이트를 요청하면 가 발생한다.")
    @Test
    void updatePhoneNumberFail() {
        //given
        String phoneNumber = "01012345678";
        User oldUser = userService.save(User.builder().phoneNumber(phoneNumber).build());
        User newUser = userService.save(User.builder().build());

        //when & then
        assertThatThrownBy(() -> userService.updatePhoneNumber(newUser.getId(), phoneNumber))
                .isInstanceOf(PhoneNumberAlreadyExistsException.class);
        assertThat(newUser.getNickname()).isNull();
    }

    @DisplayName("userId로 userName을 업데이트 한다.")
    @Test
    void updateName() {
        //given
        String userName = "홍길동";
        String phoneNumber = "01012345678";
        User user = userService.save(User.builder().phoneNumber(phoneNumber).build());

        //when
        userAccountService.updateName(user.getId(), userName);
        user = userService.findUser(user.getPhoneNumber());

        //then
        assertThat(user.getUsername()).isEqualTo(userName);
    }

    @DisplayName("userId로 nickname을 업데이트 한다.")
    @Test
    void updateNickname() {
        //given
        String nickname = "닉네임";
        String phoneNumber = "01012345678";
        User user = userService.save(User.builder().phoneNumber(phoneNumber).build());

        //when
        userAccountService.updateNickname(user.getId(), nickname);
        user = userService.findUser(user.getPhoneNumber());

        //then
        assertThat(user.getNickname()).isEqualTo(nickname);
    }

    @DisplayName("다른 유저가 사용중인 닉네임으로 업데이트를 요청하면 NicknameAlreadyExistsException이 발생한다.")
    @Test
    void updateNicknameFail() {
        //given
        String nickname = "닉네임";
        String phoneNumber1 = "01012345678";
        String phoneNumber2 = "01087654321";
        User oldUser = userService.save(User.builder().nickname(nickname).phoneNumber(phoneNumber1).build());
        User newUser = userService.save(User.builder().phoneNumber(phoneNumber2).build());

        //when & then
        assertThatThrownBy(() -> userAccountService.updateNickname(newUser.getId(), nickname))
                .isInstanceOf(NicknameAlreadyExistsException.class);
        assertThat(newUser.getNickname()).isNull();
    }

    @DisplayName("userId로 gender, birth, city, county를 업데이트 한다.")
    @Test
    void updateUserDetailInfo() {
        //given
        String phoneNumber = "01012345678";
        LocalDate birth = LocalDate.now();
        City city = City.GYEONGGI;
        County county = County.ANDONG_SI;
        Gender gender = Gender.MALE;
        UpdateUserDetailInfoDto userDetailInfoDto = UpdateUserDetailInfoDto.builder()
                .birth(birth)
                .city(city)
                .county(county)
                .gender(gender)
                .build();
        User user = userService.save(User.builder().phoneNumber(phoneNumber).build());

        //when
        userAccountService.updateUserDetailInfo(user.getId(), userDetailInfoDto);
        user = userService.findUser(phoneNumber);

        //then
        assertThat(user).extracting(
                User::getBirth,
                User::getCity,
                User::getCounty,
                User::getGender
        ).containsExactly(
                birth,
                city,
                county,
                gender
        );
    }
}
