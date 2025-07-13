package com.clip.api.matching.controller.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;

@Getter
@NoArgsConstructor
public class MatchingProgressInfoDto {
    private MatchingProgressInfo matchingProgressInfo;

    @Builder
    public MatchingProgressInfoDto(List<String> nicknameList, List<String> tmiList, Map<String,String> oneThingMap) {
        this.matchingProgressInfo = MatchingProgressInfo.builder()
                .nicknameList(nicknameList)
                .tmiList(tmiList)
                .nicknameOnethingMap(oneThingMap)
                .build();
    }

    @Getter
    public static class MatchingProgressInfo{
        private final List<String> nicknameList;
        private final List<String> tmiList;
        private final Map<String,String> nicknameOnethingMap;

        @Builder
        private MatchingProgressInfo(List<String> nicknameList, List<String> tmiList, Map<String,String> nicknameOnethingMap) {
            this.nicknameList = nicknameList;
            this.tmiList = tmiList;
            this.nicknameOnethingMap = nicknameOnethingMap;
        }
    }

}
