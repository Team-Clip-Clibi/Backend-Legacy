package com.clip.infra.fcm.service;

public sealed interface MessageParams
        permits MessageParams.NicknameParams,
        MessageParams.DayOfWeekAndTimeParams,
        MessageParams.TimeAndPlaceParams {

    record NicknameParams(String nickname) implements MessageParams {}

    record DayOfWeekAndTimeParams(String dayOfWeek, String time) implements MessageParams {}

    record TimeAndPlaceParams(String time, String place) implements MessageParams {}
}