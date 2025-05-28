package com.clip.api.matching.controller.dto;

import com.clip.matching.entity.RandomDistrict;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@NoArgsConstructor
public class RandomMatchingOrderDto {

    @Getter
    @Schema(name = "RandomOrderRequest")
    public static class Request {
        private final String topic;
        @Size(max = 2)
        private final RandomDistrict district;
        private final String tmiContent;

        @Builder
        public Request(String topic, RandomDistrict district, String tmiContent) {
            this.topic = topic;
            this.district = district;
            this.tmiContent = tmiContent;
        }
    }

    @Getter
    public static class Response{
        private final UUID orderId;
        private final int amount;
        private final LocalDateTime meetingTime;
        private final String meetingPlace;
        private final String meetingLocation;
        private final Long matchingId;

        @Builder
        public Response(UUID orderId, int amount, LocalDateTime meetingTime, String meetingPlace, String meetingLocation, Long matchingId) {
            this.orderId = orderId;
            this.amount = amount;
            this.meetingTime = meetingTime;
            this.meetingPlace = meetingPlace;
            this.meetingLocation = meetingLocation;
            this.matchingId = matchingId;
        }
    }
}
