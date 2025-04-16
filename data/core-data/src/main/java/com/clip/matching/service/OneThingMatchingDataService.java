package com.clip.matching.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.clip.matching.entity.OneThingMatching;
import com.clip.matching.repository.OneThingMatchingRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class OneThingMatchingDataService {

    private final OneThingMatchingRepository oneThingMatchingRepository;

    public OneThingMatching save(OneThingMatching oneThingMatching) {
        return oneThingMatchingRepository.save(oneThingMatching);
    }

    public List<OneThingMatching> findAllOneThingMatchings() {return oneThingMatchingRepository.findAll();}
    public void delete(Long oneThingMatchingId) {
        oneThingMatchingRepository.deleteOneThingMatching(oneThingMatchingId);
    }
}
