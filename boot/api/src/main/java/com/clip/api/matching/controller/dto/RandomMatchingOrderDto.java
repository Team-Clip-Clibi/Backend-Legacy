package com.clip.api.matching.controller.dto;

import com.clip.matching.entity.RandomDistrict;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@NoArgsConstructor
public class RandomMatchingOrderDto {

    @Getter
    public static class Request {
        private final String topic;
        @Size(max = 2)
        private final List<RandomDistrict> districts;
        private final String tmiContent;

        @Builder
        public Request(String topic, List<RandomDistrict> districts, String tmiContent) {
            this.topic = topic;
            this.districts = districts;
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
