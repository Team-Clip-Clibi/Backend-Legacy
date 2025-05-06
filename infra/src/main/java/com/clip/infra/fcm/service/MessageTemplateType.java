package com.clip.infra.fcm.service;

import lombok.Getter;

import java.util.function.Function;

@Getter
public enum MessageTemplateType {
    MATCHING_COMPLETED(
            params -> "신청한 모임이 매칭되었어요! 모임 참여를 확정해주세요 🙂",
            false,
            true
    ),

    MATCHING_INFO_OPENED(
            params -> params instanceof MessageParams.DayOfWeekAndTimeParams p ?
                    String.format("%s요일 %s시에 있을 모임 정보가 오픈되었어요! 두근두근 어떤 사람을 만나게 될지 보러가볼까요 👀",
                            p.dayOfWeek(), p.time()) :
                    "모임 정보가 오픈되었어요!",
            false,
            true
    ),

    MATCHING_TOMORROW(
            params -> "내일 모임이 예정되어있네요. 모임 전 주의사항을 꼭 확인해주세요.",
            false,
            true
    ),

    MATCHING_TODAY(
            params -> params instanceof MessageParams.TimeAndPlaceParams p ?
                    String.format("오늘 %s시 %s에서 모임이 예정되어있어요. 이따가 만나요 🥰",
                            p.time(), p.place()) :
                    "오늘 모임이 예정되어 있어요.",
            false,
            true
    ),

    MATCHING_ENDED(
            params -> params instanceof MessageParams.NicknameParams p ?
                    String.format("%s님, 오늘의 원띵 모임에서 좋은 인사이트 나누셨나요? 오늘 원띵 모임에 대한 리뷰를 남겨주세요 🥰",
                            p.nickname()) :
                    "모임 리뷰를 남겨주세요!",
            false,
            true
    ),

    MATCHING_STARTED(
            params -> params instanceof MessageParams.NicknameParams p ?
                    String.format("안녕하세요! 원띵 모임에 오신 여러분 환영해요. %s님은 휴대폰을 가운데 두고, 아이스브레이킹 원띵 모임을 시작해보세요 😆",
                            p.nickname()) :
                    "원띵 모임을 시작해보세요!",
            false,
            true
    ),

    MATCHING_MEETUP(
            params -> params instanceof MessageParams.NicknameParams p ?
                    String.format("오늘 퇴근하고 뭐해? \n" +
                                    "%s님의 퇴근길에 오늘 열리는 번개 모임이 있어요.",
                            p.nickname()) :
                    "번개 모임을 시작해보세요!",
            false,
            true
    ),
    ;


    private final Function<MessageParams, String> messageGenerator;
    private final boolean system;
    private final boolean general;

    MessageTemplateType(Function<MessageParams, String> messageGenerator, boolean system, boolean general) {
        this.messageGenerator = messageGenerator;
        this.system = system;
        this.general = general;
    }

    public String generateMessage(MessageParams params) {
        return messageGenerator.apply(params);
    }

}