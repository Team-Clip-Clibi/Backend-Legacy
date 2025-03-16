package com.clip.user.service;

import com.clip.user.entity.TermsAcceptance;
import com.clip.user.repository.TermsAcceptanceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TermsAcceptanceService {
    private final TermsAcceptanceRepository termsAcceptanceRepository;

    public TermsAcceptance save(TermsAcceptance termsAcceptance) {
        return termsAcceptanceRepository.save(termsAcceptance);
    }
}
