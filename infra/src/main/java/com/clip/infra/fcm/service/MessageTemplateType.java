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

    LATE_ARRIVAL(
            params -> params instanceof MessageParams.NicknameAndTimeParams p ?
                    String.format("이번 모임의 %s님께서 약 %d분 이상 늦을 예정이에요. 먼저 메뉴를 주문하고 모임을 진행하세요.",
                            p.nickname(), p.time()) :
                    "이번 모임의 누군가가 약간 늦을 예정이에요. 먼저 메뉴를 주문하고 모임을 진행하세요.",
            false,
            true
    )
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