package boot.api.com.clip.api.user.service;

import com.clip.ApiApplication;
import com.clip.api.payment.feign.TossPaymentFeign;
import com.clip.api.user.controller.dto.*;
import com.clip.api.user.service.UserAccountService;
import com.clip.auth.entity.Token;
import com.clip.auth.repository.TokenRepository;
import com.clip.global.config.feign.FeignConfig;
import com.clip.global.config.jwt.JWTProperties;
import com.clip.global.config.jwt.TokenProvider;
import com.clip.infra.aws.s3.S3Config;
import com.clip.infra.aws.s3.S3FCMService;
import com.clip.infra.aws.s3.S3ImgService;
import com.clip.infra.fcm.config.FcmConfig;
import com.clip.matching.entity.*;
import com.clip.matching.repository.OneThingMatchingRepository;
import com.clip.matching.repository.RandomMatchingRepository;
import com.clip.matching.repository.UserOneThingMatchingRepository;
import com.clip.matching.repository.UserRandomMatchingRepository;
import com.clip.user.entity.*;
import com.clip.user.exception.NicknameAlreadyExistsException;
import com.clip.user.exception.PhoneNumberAlreadyExistsException;
import com.clip.user.exception.UserNotFoundException;
import com.clip.user.repository.UserJobRepository;
import com.clip.user.repository.UserRepository;
import com.clip.user.service.UserService;
import io.jsonwebtoken.Jwts;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionManager;
import org.springframework.transaction.support.TransactionTemplate;

import java.time.LocalDate;
import java.time.LocalDateTime;
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
    private PlatformTransactionManager platformTransactionManager;
    @Autowired
    private UserAccountService userAccountService;
    @Autowired
    private UserService userService;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private TokenProvider tokenProvider;
    @Autowired
    private TokenRepository tokenRepository;
    @MockitoBean
    private JWTProperties jwtProperties;
    @Autowired
    private UserJobRepository userJobRepository;
    @MockitoBean
    private S3ImgService s3ImgService;
    @MockitoBean
    private S3Config s3Config;
    @MockitoBean
    private S3FCMService s3FCMService;
    @MockitoBean
    private FcmConfig fcmConfig;
    @MockitoBean
    private FeignConfig feignConfig;
    @MockitoBean
    private TossPaymentFeign tossPaymentFeign;
    @Autowired
    private OneThingMatchingRepository oneThingMatchingRepository;
    @Autowired
    private RandomMatchingRepository randomMatchingRepository;
    @Autowired
    private UserOneThingMatchingRepository userOneThingMatchingRepository;
    @Autowired
    private UserRandomMatchingRepository userRandomMatchingRepository;

    @AfterEach
    void tearDown() {
        tokenRepository.deleteAllInBatch();
        randomMatchingRepository.deleteAllInBatch();
        userRepository.deleteAllInBatch();
        userJobRepository.deleteAllInBatch();
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
                .isAllowNotify(true)
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
            DeviceType deviceType = DeviceType.iOS;
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
                    .isAllowNotify(true)
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
                    .isAllowNotify(true)
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
        TokenProvider.Token token = userAccountService.login(LoginDto.builder()
                .platform(platform)
                .socialId(socialId)
                .deviceType(DeviceType.iOS)
                .osVersion("1.1.1")
                .firebaseToken("firebaseToken")
                .isAllowNotify(true)
                .build()
        );
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
                .isInstanceOf(UserNotFoundException.class);
    }

    @DisplayName("userId로 phoneNumber를 업데이트 한다.")
    @Test
    void updatePhoneNumber() {
        //given
        String phoneNumber = "01012345678";
        User user = userService.save(User.builder().build());

        //when
        userAccountService.updatePhoneNumber(user.getId(), phoneNumber);
        user = userService.findUser(user.getId());

        //then
        assertThat(user.getPhoneNumber()).isEqualTo(phoneNumber);
    }

    @DisplayName("다른 유저가 사용중인 번호로로 업데이트를 요청하면 PhoneNumberAlreadyExistsException가 발생한다.")
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
        user = userService.findUser(user.getId());

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
        user = userService.findUser(user.getId());

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
        City city = City.INCHEON;
        County county = County.GANGSEO;
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
        user = userService.findUser(user.getId());

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

    @DisplayName("사용중인 닉네임이면 NicknameAlreadyExistsException이 발생한다.")
    @Test
    void checkNicknameAvailable() {
        //given
        String nickname = "닉네임";
        userService.save(User.builder().nickname(nickname).build());

        //when & then
        assertThatThrownBy(() -> userAccountService.checkNicknameAvailable(nickname))
                .isInstanceOf(NicknameAlreadyExistsException.class);
    }

    @DisplayName("userId로 프로필 기본 정보를 조회할 수 있다.")
    @Test
    void getProfileInfo() {
        //given
        String phoneNumber = "01012345678";
        String username = "홍길동";
        String nickname = "닉네임";
        Platform platform = Platform.APPLE;
        User user = userService.save(User.builder()
                .username(username)
                .nickname(nickname)
                .platform(platform)
                .phoneNumber(phoneNumber)
                .build());

        //when
        RetrieveUserProfileInfo userProfileInfo = userAccountService.getUserProfileInfo(user.getId());

        //then
        assertThat(userProfileInfo).extracting(
                RetrieveUserProfileInfo::getUsername,
                RetrieveUserProfileInfo::getNickname,
                RetrieveUserProfileInfo::getPhoneNumber,
                RetrieveUserProfileInfo::getPlatform
        ).containsExactly(
                user.getUsername(),
                user.getNickname(),
                user.getPhoneNumber(),
                user.getPlatform()
        );
    }

    @DisplayName("userId로 FCMToken을 업데이트 한다.")
    @Test
    void updateFCMToken() {
        //given
        String oldFcmToken = "FCM_V1";
        String newFcmToken = "FCM_V2";
        Long userId = userRepository.save(User.builder().firebaseToken(oldFcmToken).build()).getId();

        //when
        userAccountService.updateFCMToken(userId, newFcmToken);

        //then
        assertThat(userRepository.findById(userId).get().getFirebaseToken()).isEqualTo(newFcmToken);
    }

    @DisplayName("userId로 isAllowNotify(알림 ON/OFF 여부)를 업데이트 한다.")
    @Test
    void updateIsAllowNotify() {
        //given
        boolean oldIsAllowNotify = true;
        boolean newIsAllowNotify = false;
        Long userId = userRepository.save(User.builder().isAllowNotify(oldIsAllowNotify).build()).getId();

        //when
        userAccountService.updateNotifyAllow(userId, newIsAllowNotify);

        //then
        assertThat(userRepository.findById(userId).get().isAllowNotify()).isEqualTo(newIsAllowNotify);
    }

    @DisplayName("userId로 직업 정보를 업데이트 할 수 있다.")
    @Test
    void updateJob() {
        //given
        Job job = userJobRepository.save(Job.builder().jobCategory(JobCategory.IT).build());
        Long userId = userService.save(User.builder().build()).getId();
        JobCategory it = JobCategory.IT;

        //when
        userAccountService.updateJob(userId, it);

        new TransactionTemplate(platformTransactionManager).execute(status -> {
            Job userJob = userService.findUser(userId).getJob();
            //then
            assertThat(userJob)
                    .extracting(Job::getJobCategory).isEqualTo(it);
            return null;
        });



    }

    @DisplayName("userId로 연애상태 정보를 업데이트 할 수 있다.")
    @Test
    void updateRelationship() {
        //given
        Long userId = userService.save(User.builder().build()).getId();
        RelationshipStatus relationshipStatus = RelationshipStatus.SINGLE;
        boolean isSameRelationshipConsidered = true;

        //when
        userAccountService.updateRelationship(userId, relationshipStatus, isSameRelationshipConsidered);
        User user = userService.findUser(userId);

        //then
        assertThat(user).extracting(
                User::getRelationshipStatus,
                User::getIsSameRelationshipConsidered
        ).containsExactly(
                relationshipStatus,
                isSameRelationshipConsidered
        );
    }

    @DisplayName("userId로 식단 정보를 업데이트 할 수 있다.")
    @Test
    void updateDietaryOption() {
        //given
        Long userId = userService.save(User.builder().build()).getId();
        String dietaryOption = "암어버섯헤이러";

        //when
        userAccountService.updateDietaryOption(userId, dietaryOption);
        User user = userService.findUser(userId);

        //then
        assertThat(user.getDietaryOption()).isEqualTo(dietaryOption);
    }

    @DisplayName("userId로 언어 정보를 업데이트 할 수 있다.")
    @Test
    void updateLanguage() {
        //given
        Long userId = userService.save(User.builder().build()).getId();
        Language korean = Language.KOREAN;

        //when
        userAccountService.updateLanguage(userId, LanguageDto.builder().language(korean).build());
        User user = userService.findUser(userId);

        //then
        assertThat(user.getLanguage()).isEqualTo(korean.getValue());
    }

    @DisplayName("매칭 예정 유저 확인 테스트")
    @Nested
    class IsMyMatchingExist{
        @BeforeEach
        void setUp() {
            User user = userRepository.save(User.builder().nickname("user").socialId("socialId").platform(Platform.APPLE).build());
            OneThingMatching oneThingMatching = oneThingMatchingRepository.save(OneThingMatching.builder()
                    .meetingTime(LocalDateTime.now().plusDays(1))
                    .build());
            RandomMatching randomMatching = randomMatchingRepository.save(RandomMatching.builder()
                    .meetingTime(LocalDateTime.now().plusDays(1))
                    .build());
        }

        @AfterEach
        void tearDown() {
            userOneThingMatchingRepository.deleteAllInBatch();
            userRandomMatchingRepository.deleteAllInBatch();
            oneThingMatchingRepository.deleteAllInBatch();
        }

        @DisplayName("원띵 매칭 예정이면 True를 반환한다.")
        @Test
        void oneThingMatchingExist() {
            //given
            User user = userRepository.findUser("socialId", Platform.APPLE).get();
            OneThingMatching oneThingMatching = oneThingMatchingRepository.findAll().get(0);
            userOneThingMatchingRepository.save(UserOneThingMatching.builder().user(user).oneThingMatching(oneThingMatching).build());

            //when
            boolean existsMyMatching = userAccountService.isExistsMyMatching(user.getId());

            //then
            assertThat(existsMyMatching).isTrue();
        }

        @DisplayName("랜덤 매칭 예정이면 True를 반환한다.")
        @Test
        void randomMatchingExist() {
            //given
            User user = userRepository.findUser("socialId", Platform.APPLE).get();
            RandomMatching randomMatching = randomMatchingRepository.findAll().get(0);
            userRandomMatchingRepository.save(UserRandomMatching.builder().user(user).randomMatching(randomMatching).build());

            //when
            boolean existsMyMatching = userAccountService.isExistsMyMatching(user.getId());

            //then
            assertThat(existsMyMatching).isTrue();
        }

        @DisplayName("랜덤 매칭과 원띵 매칭 예정이면 True를 반환한다.")
        @Test
        void randomAndOneThingMatchingExist() {
            //given
            User user = userRepository.findUser("socialId", Platform.APPLE).get();
            OneThingMatching oneThingMatching = oneThingMatchingRepository.findAll().get(0);
            userOneThingMatchingRepository.save(UserOneThingMatching.builder().user(user).oneThingMatching(oneThingMatching).build());
            RandomMatching randomMatching = randomMatchingRepository.findAll().get(0);
            userRandomMatchingRepository.save(UserRandomMatching.builder().user(user).randomMatching(randomMatching).build());

            //when
            boolean existsMyMatching = userAccountService.isExistsMyMatching(user.getId());

            //then
            assertThat(existsMyMatching).isTrue();
        }

        @DisplayName("랜덤 매칭과 원띵 매칭 예정이면 True를 반환한다.")
        @Test
        void matchingNotExist() {
            //given
            User user = userRepository.findUser("socialId", Platform.APPLE).get();

            //when
            boolean existsMyMatching = userAccountService.isExistsMyMatching(user.getId());

            //then
            assertThat(existsMyMatching).isFalse();
        }
    }
}
