package com.clip.api.matching.controller.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;

@Getter
@NoArgsConstructor
public class MatchingProgressInfoDto {
    private List<String> nicknameList;
    private List<String> tmiList;
    private Map<String,String> nicknameOnethingMap;

    @Builder
    public MatchingProgressInfoDto(List<String> nicknameList, List<String> tmiList, Map<String, String> nicknameOnethingMap) {
        this.nicknameList = nicknameList;
        this.tmiList = tmiList;
        this.nicknameOnethingMap = nicknameOnethingMap;
    }
}
