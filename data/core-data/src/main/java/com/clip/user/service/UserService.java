package com.clip.user.service;

import com.clip.global.exception.ResourceAlreadyExistException;
import com.clip.global.exception.ResourceNotFoundException;
import com.clip.user.entity.*;
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

    public User findUser(String socialId, Platform platform) {
        return userRepository.findUser(socialId, platform).orElseThrow(()-> new ResourceNotFoundException("user", socialId));
    }

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
            throw new ResourceAlreadyExistException("phoneNumber", phoneNumber);
        }
    }

    public void updateUserName(long userId, String userName) {
        userRepository.updateUserName(userId, userName);
    }

    public void updateNickname(long userId, String nickname) {
        try {
            userRepository.updateNickname(userId, nickname);
        }catch (DataIntegrityViolationException e){
            throw new ResourceAlreadyExistException("nickname", nickname);
        }
    }

    public void updateDeviceInfo(long userId, DeviceType deviceType, String osVersion, String firebaseToken, boolean isAllowNotify) {
        userRepository.updateDeviceInfo(userId, deviceType, osVersion, firebaseToken, isAllowNotify);
    }

    public void updateUserDetailInfo(long userId, Gender gender, LocalDate birth, City city, County county) {
        userRepository.updateUserDetailInfo(userId, gender, birth, city, county);
    }

    public User findUserExcludeOwner(long ownerId, String phoneNumber) {
        return userRepository.findUserExcludeOwner(ownerId, phoneNumber)
                .orElseThrow(()-> new ResourceNotFoundException("user", phoneNumber));
    }

    public User findUser(long userId) {
        return userRepository.findById(userId)
                .orElseThrow(()-> new ResourceNotFoundException("user", userId));
    }

    public boolean isExistNickname(String nickname) {
        return userRepository.existsByNickname(nickname);
    }

    public void deleteUser(User user) {
        userRepository.delete(user);
    }
}
