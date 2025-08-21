package com.clip.matching.entity;

public enum RandomMatchingStatus implements MatchingStatus {
    APPLIED("신청완료"),
    CONFIRMED("매칭확정"),
    COMPLETED("모임종료"),
    CANCELED_MATCHING_FAIL("매칭 실패 결제 취소"),
    CANCELED("취소");

    private final String MatchingStatusName;
    RandomMatchingStatus(String MatchingStatusName) {
        this.MatchingStatusName = MatchingStatusName;
    }
    public String getMatchingStatusName() {
        return MatchingStatusName;
    }
}
