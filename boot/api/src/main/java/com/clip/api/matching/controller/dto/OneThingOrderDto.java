package com.clip.api.matching.controller.dto;

import com.clip.matching.entity.OneThingDistrict;
import com.clip.matching.entity.OneThingBudgetRange;
import com.clip.matching.entity.UserOneThingMatching;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.UUID;

@NoArgsConstructor
public class OneThingOrderDto {

    @Getter
    public static class Request{
        private final String topic;
        @Size(max = 2)
        private final List<OneThingDistrict> districts;
        @Size(max = 3)
        private final List<UserOneThingMatching.PreferredDate> preferredDates;
        private final String tmiContent;
        private final OneThingBudgetRange oneThingBudgetRange;

        @Builder
        public Request(String topic, List<OneThingDistrict> districts, List<UserOneThingMatching.PreferredDate> preferredDates, String tmiContent, OneThingBudgetRange oneThingBudgetRange) {
            this.topic = topic;
            this.districts = districts;
            this.preferredDates = preferredDates;
            this.tmiContent = tmiContent;
            this.oneThingBudgetRange = oneThingBudgetRange;
        }
    }

    @Getter
    public static class Response{
        private final UUID orderId;
        private final int amount;

        @Builder
        public Response(UUID orderId, int amount) {
            this.orderId = orderId;
            this.amount = amount;
        }
    }
}
