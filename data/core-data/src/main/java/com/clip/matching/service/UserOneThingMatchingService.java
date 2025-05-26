package com.clip.matching.service;

import com.clip.matching.entity.UserOneThingMatching;
import com.clip.matching.repository.UserOneThingMatchingRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class UserOneThingMatchingService {
    private final UserOneThingMatchingRepository userOneThingMatchingRepository;

    public UserOneThingMatching save(UserOneThingMatching userOneThingMatching) {
        return userOneThingMatchingRepository.save(userOneThingMatching);
    }

    public boolean isOneThingMatchingExist(long userId) {
        return userOneThingMatchingRepository.findLatestUserOneThingMatching(userId, LocalDateTime.now()).isPresent();
    }

    public void deleteOneThingMatching(long userId) {
        userOneThingMatchingRepository.deleteOneThingMatching(userId);
    }
}
