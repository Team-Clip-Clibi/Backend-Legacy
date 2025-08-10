package com.clip.matching.service;

import com.clip.matching.entity.OneThingMatching;
import com.clip.matching.entity.OnethingDistrict;
import com.clip.matching.repository.OnethingMatchingRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class OnethingMatchingService {
    private final OnethingMatchingRepository oneThingMatchingRepository;

    public Slice<OneThingMatching> findMatchingList(
            LocalDateTime startDateTime,
            LocalDateTime endDateTime,
            OnethingDistrict district,
            int page
    ) {
        return oneThingMatchingRepository.findMatchingList(
                startDateTime,
                endDateTime,
                district,
                PageRequest.of(page, 30)
        );
    }

    public OneThingMatching findById(Long oneThingMatchingId) {
        return oneThingMatchingRepository.findById(oneThingMatchingId)
                .orElseThrow(IllegalArgumentException::new);
    }

    public OneThingMatching save(OneThingMatching oneThingMatching) {
        return oneThingMatchingRepository.save(oneThingMatching);
    }

    public void delete(OneThingMatching oneThingMatching) {
        oneThingMatchingRepository.delete(oneThingMatching);
    }

    public List<OneThingMatching> findByIds(List<Long> ids) {
        return oneThingMatchingRepository.findAllByIds(ids);
    }

    public List<OneThingMatching> saveAll(List<OneThingMatching> oneThingMatchings) {
        return oneThingMatchingRepository.saveAll(oneThingMatchings);
    }

    public Slice<OneThingMatching> findMatchingListFetchQuestion(int page) {
        return oneThingMatchingRepository.findMatchingListFetchQuestion(PageRequest.of(page, 30));
    }
}
