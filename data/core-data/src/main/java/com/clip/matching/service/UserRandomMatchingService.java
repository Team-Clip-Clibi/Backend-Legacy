package com.clip.matching.service;

import com.clip.matching.entity.UserRandomMatching;
import com.clip.matching.repository.UserRandomMatchingRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserRandomMatchingService {
    private final UserRandomMatchingRepository userRandomMatchingRepository;

    public UserRandomMatching save(UserRandomMatching userRandomMatching) {
        return userRandomMatchingRepository.save(userRandomMatching);
    }
}
