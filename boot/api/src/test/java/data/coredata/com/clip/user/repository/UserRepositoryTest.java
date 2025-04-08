package data.coredata.com.clip.user.repository;

import com.clip.ApiApplication;
import com.clip.user.entity.*;
import com.clip.user.repository.UserRepository;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.test.context.ContextConfiguration;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@ContextConfiguration(classes = ApiApplication.class)
@DataJpaTest
public class UserRepositoryTest {
    @Autowired
    UserRepository userRepository;
    @Autowired
    EntityManager entityManager;

    @DisplayName("userId로 phoneNumber를 업데이트 한다.")
    @Test
    void updatePhoneNumber() {
        //given
        String phoneNumber = "010-1234-5678";
        User user = userRepository.save(User.builder().build());

        //when
        userRepository.updatePhoneNumber(user.getId(), phoneNumber);

        //then
        user = userRepository.findById(user.getId()).get();
        assertThat(user)
                .extracting(User::getPhoneNumber, User::isPhoneNumVerified)
                .containsExactly(phoneNumber, true);
    }

    @DisplayName("다른 유저가 사용중인 번호로는 업데이트 할 수 없다.")
    @Test
    void updatePhoneNumberFail() {
        //given
        String phoneNumber = "01012345678";
        User oldUser = userRepository.save(User.builder().phoneNumber(phoneNumber).build());
        User newUser = userRepository.save(User.builder().build());

        //when & then
        assertThatThrownBy(() -> userRepository.updatePhoneNumber(newUser.getId(), phoneNumber))
                .isInstanceOf(DataIntegrityViolationException.class);
        assertThat(newUser)
                .extracting(User::getPhoneNumber, User::isPhoneNumVerified)
                .containsExactly(null, false);
    }

    @DisplayName("userId로 userName을 업데이트 한다.")
    @Test
    void updateUserName() {
        //given
        String userName = "홍길동";
        User user = userRepository.save(User.builder().build());

        //when
        userRepository.updateUserName(user.getId(), userName);
        user = userRepository.findById(user.getId()).get();

        //then
        assertThat(user.getUsername()).isEqualTo(userName);
    }

    @DisplayName("userId로 nickname을 업데이트 한다.")
    @Test
    void updateNickname() {
        //given
        String nickname = "닉네임";
        User user = userRepository.save(User.builder().build());

        //when
        userRepository.updateNickname(user.getId(), nickname);
        user = userRepository.findById(user.getId()).get();

        //then
        assertThat(user.getNickname()).isEqualTo(nickname);
    }

    @DisplayName("다른 유저가 사용중인 닉네임으로 업데이트 할 수 없다.")
    @Test
    void updateNicknameFail() {
        //given
        String nickname = "닉네임";
        User oldUser = userRepository.save(User.builder().nickname(nickname).build());
        User newUser = userRepository.save(User.builder().build());

        //when & then
        assertThatThrownBy(()->userRepository.updateNickname(newUser.getId(), nickname)).isInstanceOf(DataIntegrityViolationException.class);
    }

    @DisplayName("userId로 deviceType, osVersion, firebaseToken을 업데이트 한다.")
    @Test
    void updateDeviceInfo() {
        //given
        String osVersion = "14.5";
        String firebaseToken = "firebaseToken";
        boolean isAllowNotify = true;
        User user = userRepository.save(User.builder().build());

        //when
        userRepository.updateDeviceInfo(user.getId(), DeviceType.iOS, osVersion, firebaseToken, isAllowNotify);
        user = userRepository.findById(user.getId()).get();

        //then
        assertThat(user)
                .extracting(
                        User::getDeviceType,
                        User::getOsVersion,
                        User::getFirebaseToken)
                .containsExactly(
                        DeviceType.iOS,
                        osVersion,
                        firebaseToken
                );
    }

    @DisplayName("userId로 gender, birth, city, county를 업데이트 한다.")
    @Test
    void updateUserDetailInfo() {
        //given
        LocalDate currentDate = LocalDate.now();
        User user = userRepository.save(User.builder().build());

        //when
        userRepository.updateUserDetailInfo(user.getId(), Gender.FEMALE, currentDate, City.GYEONGGI, County.YONGIN_SI);
        user = userRepository.findById(user.getId()).get();

        //then
        assertThat(user)
                .extracting(
                        User::getGender,
                        User::getBirth,
                        User::getCity,
                        User::getCounty
                )
                .containsExactly(
                        Gender.FEMALE,
                        currentDate,
                        City.GYEONGGI,
                        County.YONGIN_SI
                );
    }

    @DisplayName("phoneNumber으로 User를 찾는다.")
    @Test
    void findUser() {
        //given
        String phoneNumber = "01012345678";
        User user = userRepository.save(User.builder().phoneNumber(phoneNumber).build());

        //when
        User foundUser = userRepository.findUser(phoneNumber).get();
        entityManager.clear();
        //then
        assertThat(foundUser).isEqualTo(user);
    }

    @DisplayName("nickname이 존재하는지 확인한다.")
    @Test
    void existsByNickname() {
        //given
        String nickname = "닉네임";
        userRepository.save(User.builder().nickname(nickname).build());

        //when
        boolean exists = userRepository.existsByNickname(nickname);

        //then
        assertThat(exists).isTrue();
    }
}
