package com.clip.api.matching.controller.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Getter
@NoArgsConstructor
public class MatchingProgressStatusDto {
    private long matchingId;
    private MatchingType matchingType;
    private LocalDateTime latestMatchingDateTime;
    private boolean isCheckedMatchingStart;
    private MatchingProgressInfo matchingProgressInfo;

    @Builder
    public MatchingProgressStatusDto(long matchingId, MatchingType matchingType, LocalDateTime latestMatchingDateTime, boolean isCheckedMatchingStart, List<String> nicknameList, List<String> quizList, Map<String,String> oneThingMap) {
        this.matchingId = matchingId;
        this.matchingType = matchingType;
        this.latestMatchingDateTime = latestMatchingDateTime;
        this.isCheckedMatchingStart = isCheckedMatchingStart;
        this.matchingProgressInfo = MatchingProgressInfo.builder()
                .nicknameList(nicknameList)
                .quizList(quizList)
                .oneThingMap(oneThingMap)
                .build();
    }

    @Getter
    public static class MatchingProgressInfo{
        private List<String> nicknameList;
        private List<String> quizList;
        private Map<String,String> oneThingMap;

        @Builder
        private MatchingProgressInfo(List<String> nicknameList, List<String> quizList, Map<String,String> oneThingMap) {
            this.nicknameList = nicknameList;
            this.quizList = quizList;
            this.oneThingMap = oneThingMap;
        }
    }

}
