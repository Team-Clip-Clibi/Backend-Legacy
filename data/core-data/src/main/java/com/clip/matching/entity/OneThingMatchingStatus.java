package com.clip.matching.entity;

public enum OneThingMatchingStatus implements MatchingStatus {
    WAIT_FOR_PAYMENT("결제대기"),
    APPLIED("신청완료"),
    CONFIRMED("매칭확정"),
    COMPLETED("모임종료"),
    CANCELED("취소");
    private final String MatchingStatusName;
    OneThingMatchingStatus(String MatchingStatusName) {
        this.MatchingStatusName = MatchingStatusName;
    }
    public String getMatchingStatusName() {
        return MatchingStatusName;
    }
}
