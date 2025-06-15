package com.clip.matching.entity;

public enum RandomMatchingStatus implements MatchingStatus {
    APPLIED("신청완료"),
    CONFIRMED("매칭확정"),
    COMPLETED("모임종료"),
    CANCELED("취소"),
    NO_SHOW("노쇼");

    private final String MatchingStatusName;
    RandomMatchingStatus(String MatchingStatusName) {
        this.MatchingStatusName = MatchingStatusName;
    }
    public String getMatchingStatusName() {
        return MatchingStatusName;
    }
}
