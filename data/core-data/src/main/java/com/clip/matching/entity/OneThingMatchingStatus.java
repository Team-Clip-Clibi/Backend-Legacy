package com.clip.matching.entity;

import lombok.Getter;

@Getter
public enum OneThingMatchingStatus implements MatchingStatus {
    WAIT_FOR_PAYMENT("결제대기"),
    APPLIED("신청완료"),
    CONFIRMED("매칭확정"),
    COMPLETED("모임종료"),
    CANCELED_MATCHING_FAIL("매칭 실패 결제 취소"),
    CANCELED("취소");
    private final String MatchingStatusName;
    OneThingMatchingStatus(String MatchingStatusName) {
        this.MatchingStatusName = MatchingStatusName;
    }
}
