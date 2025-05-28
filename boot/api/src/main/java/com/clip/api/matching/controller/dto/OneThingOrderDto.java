package com.clip.api.matching.controller.dto;

import com.clip.matching.entity.OneThingCategory;
import com.clip.matching.entity.OneThingDistrict;
import com.clip.matching.entity.OneThingBudgetRange;
import com.clip.matching.entity.UserOneThingMatching;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.UUID;

@NoArgsConstructor
public class OneThingOrderDto {

    @Getter
    @Schema(name = "OneThingOrderRequest")
    public static class Request{
        private final String topic;
        private final OneThingDistrict district;
        @Size(max = 3)
        private final List<UserOneThingMatching.PreferredDate> preferredDates;
        private final String tmiContent;
        private final OneThingBudgetRange oneThingBudgetRange;
        private final OneThingCategory oneThingCategory;

        @Builder
        public Request(String topic, OneThingDistrict district, List<UserOneThingMatching.PreferredDate> preferredDates, String tmiContent, OneThingBudgetRange oneThingBudgetRange, OneThingCategory oneThingCategory) {
            this.topic = topic;
            this.district = district;
            this.preferredDates = preferredDates;
            this.tmiContent = tmiContent;
            this.oneThingBudgetRange = oneThingBudgetRange;
            this.oneThingCategory = oneThingCategory;
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
