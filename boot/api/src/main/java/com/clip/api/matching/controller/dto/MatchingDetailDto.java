package com.clip.api.matching.controller.dto;

import com.clip.matching.entity.MatchingStatus;
import com.clip.user.entity.JobCategory;
import com.clip.user.entity.RelationshipStatus;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@SuperBuilder
@JsonSubTypes({
        @JsonSubTypes.Type(value = OneThingMatchingDetailDto.class, name = "ONE_THING"),
        @JsonSubTypes.Type(value = RandomMatchingDetailDto.class, name = "RANDOM")
})
@Schema(
        description = "매칭 상세 정보",
        discriminatorProperty = "matchingType"
)
public abstract class MatchingDetailDto {
    private long matchingId;
    private LocalDateTime meetingTime;
    private MatchingStatus matchingStatus;
    private MatchingType matchingType;
    private String myOneThingContent;
    private ApplicationInfo applicationInfo;
    private MyMatchingInfo myMatchingInfo;
    private PaymentInfo paymentInfo;

    @Getter
    @SuperBuilder
    @JsonSubTypes(
            value = {
                    @JsonSubTypes.Type(value = OneThingMatchingDetailDto.ApplicationInfo.class, name = "ONE_THING"),
                    @JsonSubTypes.Type(value = RandomMatchingDetailDto.ApplicationInfo.class, name = "RANDOM")
            }
    )
    public abstract static class ApplicationInfo {
        private String district;
    }

    @Getter
    public static class MyMatchingInfo {
        private JobCategory job;
        private RelationshipStatus relationshipStatus;
        private String dietaryOption;
        private String language;

        @Builder
        public MyMatchingInfo(JobCategory job, RelationshipStatus relationshipStatus, String dietaryOption, String language) {
            this.job = job;
            this.relationshipStatus = relationshipStatus;
            this.dietaryOption = dietaryOption;
            this.language = language;
        }
    }

    @Getter
    public static class PaymentInfo {
        private int matchingPrice;
        private int paymentPrice;

        @Builder
        public PaymentInfo(int matchingPrice, int paymentPrice) {
            this.matchingPrice = matchingPrice;
            this.paymentPrice = paymentPrice;
        }
    }

}
