package com.clip.office.matching.controller;

import com.clip.matching.entity.OnethingDistrict;
import com.clip.matching.entity.RandomDistrict;
import com.clip.office.matching.controller.dto.*;
import com.clip.office.matching.service.AdminMatchingService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Slice;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@RequestMapping("/matchings")
@RestController
@RequiredArgsConstructor
public class MatchingController {

    private final AdminMatchingService adminMatchingService;

    /**
     * 원띵 모임 삭제 API
     */
    @DeleteMapping("/onethings/{id}")
    public void deleteOnethingMatching(@PathVariable Long id) {
        adminMatchingService.deleteOnethingMatching(id);
    }

    /**
     * 랜덤 모임 삭제 API
     */
    @DeleteMapping("/randoms/{id}")
    public void deleteRandomMatching(@PathVariable Long id) {
        adminMatchingService.deleteRandomMatching(id);
    }

    /**
     * 원띵 모임 생성 API
     */
    @PostMapping("/onethings")
    public void saveOnethingMatching(@RequestBody CreateOnethingMatchingDto request) {
        adminMatchingService.createOnethingMatching(request);
    }

    /**
     * 랜덤 모임 생성 API
     */
    @PostMapping("/randoms")
    public void saveRandomMatching(@RequestBody CreateRandomMatchingDto request) {
        adminMatchingService.createRandomMatching(request);
    }

    /**
     * 원띵 모임 목록 조회 API
     */
    @GetMapping("/onethings/{page}")
    public Slice<MatchingInfoDto> getOnethingMatchingList(
            @PathVariable Integer page,
            @RequestParam(required = false) LocalDate date,
            @RequestParam(required = false) OnethingDistrict district
    ) {
        return adminMatchingService.getOnethingMatchingList(date, district, page);
    }

    /**
     * 랜덤 모임 목록 조회 API
     */
    @GetMapping("/randoms/{page}")
    public Slice<MatchingInfoDto> getRandomMatchingList(
            @PathVariable Integer page,
            @RequestParam(required = false) LocalDate date,
            @RequestParam(required = false) RandomDistrict district
    ) {
        return adminMatchingService.getRandomMatchingList(date, district, page);
    }

    /**
     * 원띵 매칭에 배정된 참여자 목록 조회 API
     */
    @GetMapping("/onethings/participants/assigned/{page}")
    public Slice<ParticipantInfoDto> getAssignedOnethingParticipantList(
            @PathVariable Integer page,
            @RequestParam Long onethingMatchingId
    ) {
        return adminMatchingService.getAssignedOnethingParticipantList(onethingMatchingId, page);
    }

    /**
     * 원띵 매칭에 배정되지 않은 참여자 목록 조회 API
     */
    @GetMapping("/onethings/participants/unassigned/{page}")
    public Slice<ParticipantInfoDto> getUnassignedOnethingParticipantList(
            @PathVariable Integer page,
            @RequestParam OnethingDistrict onethingDistrict,
            @RequestParam LocalDate date
    ) {
        return adminMatchingService.getUnassignedOnethingParticipantList(onethingDistrict, date, page);
    }

    /**
     * 원띵 매칭에 참여자 삭제 API
     */
    @DeleteMapping("/onethings/participants/assigned/{userOnethingMatchingId}")
    public void deleteParticipantFromOnethingMatching(
            @PathVariable long userOnethingMatchingId
    ){
        adminMatchingService.deleteParticipantFromOnethingMatching(userOnethingMatchingId);
    }

    /**
     * 원띵 매칭에 참여자 등록 API
     */
    @PatchMapping("/onethings/participants")
    public void registerOnethingMatchingParticipants(@RequestBody RegisterOnethingParticipantDto request) {
        adminMatchingService.registerOnethingMatchingParticipants(request);
    }
}
