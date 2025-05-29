package com.clip.matching.service;

import com.clip.matching.entity.UserRandomMatching;
import com.clip.matching.repository.UserRandomMatchingRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class UserRandomMatchingService {
    private final UserRandomMatchingRepository userRandomMatchingRepository;

    public UserRandomMatching save(UserRandomMatching userRandomMatching) {
        return userRandomMatchingRepository.save(userRandomMatching);
    }

    public boolean isRandomMatchingExist(long userId) {
        return userRandomMatchingRepository.findLatestUserRandomMatching(userId, LocalDateTime.now()).isPresent();
    }

    public void deleteRandomMatching(long userId) {
        userRandomMatchingRepository.deleteRandomMatching(userId);
    }

    public boolean isDuplicatedMatching(long userId, LocalDateTime meetingTime) {
        return userRandomMatchingRepository.findUserRandomMatching(userId, meetingTime).isPresent();
    }
}
