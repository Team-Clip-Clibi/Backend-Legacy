package com.clip.user.service;

import com.clip.user.entity.*;
import com.clip.user.exception.NicknameAlreadyExistsException;
import com.clip.user.exception.PhoneNumberAlreadyExistsException;
import com.clip.user.exception.UserNotFoundException;
import com.clip.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;

    public Optional<User> findOptUser(String socialId, Platform platform) {
        return userRepository.findUser(socialId, platform);
    }

    public User save(User user) {
        return userRepository.save(user);
    }

    public void updatePhoneNumber(long userId, String phoneNumber) {
        try {
            userRepository.updatePhoneNumber(userId, phoneNumber);
        }catch (DataIntegrityViolationException e){
            throw new PhoneNumberAlreadyExistsException();
        }
    }

    public void updateUserName(long userId, String userName) {
        userRepository.updateUserName(userId, userName);
    }

    public void updateNickname(long userId, String nickname) {
        try {
            userRepository.updateNickname(userId, nickname);
        }catch (DataIntegrityViolationException e){
            throw new NicknameAlreadyExistsException();
        }
    }

    public void updateDeviceInfo(long userId, DeviceType deviceType, String osVersion, String firebaseToken) {
        userRepository.updateDeviceInfo(userId, deviceType, osVersion, firebaseToken);
    }

    public void updateUserDetailInfo(long userId, Gender gender, LocalDate birth, City city, County county) {
        userRepository.updateUserDetailInfo(userId, gender, birth, city, county);
    }

    public User findUser(String phoneNumber) {
        return userRepository.findUser(phoneNumber)
                .orElseThrow(UserNotFoundException::new);
    }

    public boolean isExistNickname(String nickname) {
        return userRepository.existsByNickname(nickname);
    }
}
