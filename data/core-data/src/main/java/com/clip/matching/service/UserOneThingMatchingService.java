package com.clip.matching.service;

import com.clip.matching.entity.UserOneThingMatching;
import com.clip.matching.repository.UserOneThingMatchingRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserOneThingMatchingService {
    private final UserOneThingMatchingRepository userOneThingMatchingRepository;

    public UserOneThingMatching save(UserOneThingMatching userOneThingMatching) {
        return userOneThingMatchingRepository.save(userOneThingMatching);
    }
}
