package com.clip.api.matching.mapper;

import com.clip.api.matching.controller.dto.*;
import com.clip.matching.entity.OneThingMatchingStatus;
import com.clip.matching.entity.RandomMatchingStatus;
import com.clip.matching.entity.UserOneThingMatching;
import com.clip.matching.entity.UserRandomMatching;
import com.clip.matching.repository.projection.MatchingProjectionDto;
import com.clip.toss.entity.TossPayment;
import com.clip.toss.entity.TossPaymentStatus;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.ReportingPolicy;

import java.time.LocalDate;
import java.util.List;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING, unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface MatchingMapper {


    default OnethingMatchingSummaryDto toDto(final UserOneThingMatching matchingInfo){
        return OnethingMatchingSummaryDto.builder()
                .matchingId(matchingInfo.getOneThingMatching().getId())
                .daysUntilMeeting(matchingInfo.getOneThingMatching().getMeetingTime().toLocalDate().toEpochDay() - LocalDate.now().toEpochDay())
                .meetingTime(matchingInfo.getOneThingMatching().getMeetingTime())
                .meetingPlace(matchingInfo.getOneThingMatching().getLocation())
                .build();
    }

    default RandomMatchingSummaryDto toDto(final UserRandomMatching userRandomMatching){
        return RandomMatchingSummaryDto.builder()
                .matchingId(userRandomMatching.getRandomMatching().getId())
                .daysUntilMeeting(userRandomMatching.getRandomMatching().getMeetingTime().toLocalDate().toEpochDay() - LocalDate.now().toEpochDay())
                .meetingTime(userRandomMatching.getRandomMatching().getMeetingTime())
                .meetingPlace(userRandomMatching.getRandomMatching().getLocation())
                .build();
    }

    default OneThingMatchingDetailDto toOneThingMatchingDetailDto(final UserOneThingMatching matchingInfo, final UserOneThingMatching paymentInfo) {

        boolean isCanceled = matchingInfo.getMatchingStatus().equals(OneThingMatchingStatus.CANCELED);

        TossPayment payment = paymentInfo.getOneThingOrder().getTossPayment().stream()
                .filter(tossPayment -> tossPayment.getTossPaymentStatus().equals(
                        isCanceled ? TossPaymentStatus.CANCELED : TossPaymentStatus.DONE))
                .findFirst()
                .orElse(null);

        return OneThingMatchingDetailDto.builder()
                .matchingId(matchingInfo.getOneThingMatching().getId())
                .meetingTime(matchingInfo.getOneThingMatching().getMeetingTime())
                .matchingStatus(matchingInfo.getMatchingStatus())
                .matchingType(MatchingType.ONE_THING)
                .myOneThingContent(matchingInfo.getMyOneThingContent())
                .applicationInfo(OneThingMatchingDetailDto.ApplicationInfo.builder()
                        .district(matchingInfo.getOneThingDistrict().name())
                        .preferredDates(matchingInfo.getPreferredDates())
                        .oneThingBudgetRange(matchingInfo.getOneThingBudgetRange())
                        .oneThingCategory(matchingInfo.getOneThingCategory())
                        .build())
                .myMatchingInfo(MatchingDetailDto.MyMatchingInfo.builder()
                        .job(matchingInfo.getUser().getJob().getJobCategory())
                        .relationshipStatus(matchingInfo.getUser().getRelationshipStatus())
                        .dietaryOption(matchingInfo.getUser().getDietaryOption())
                        .language(matchingInfo.getUser().getLanguage())
                        .build())
                .paymentInfo(OneThingMatchingDetailDto.PaymentInfo.builder()
                        .matchingPrice(paymentInfo.getOneThingOrder().getPrice().getBasePrice().intValue())
                        .paymentPrice(paymentInfo.getOneThingOrder().getDiscountedPrice().intValue())
                        .refundPrice(isCanceled && payment != null && payment.getRefundAmount() != null
                                ? payment.getRefundAmount()
                                : null)
                        .requestedAt(payment != null && payment.getRequested_at() != null
                                ? payment.getRequested_at().toLocalDateTime()
                                : null)
                        .approvedAt(payment != null && payment.getApprovedAt() != null
                                ? payment.getApprovedAt().toLocalDateTime()
                                : null)
                        .build())
                .build();
    }

    default RandomMatchingDetailDto toRandomMatchingDetailDto(final UserRandomMatching userRandomMatching) {
        boolean isCanceled = userRandomMatching.getMatchingStatus().equals(RandomMatchingStatus.CANCELED);

        TossPayment payment = userRandomMatching.getRandomOrder().getTossPayment().stream()
                .filter(tossPayment -> tossPayment.getTossPaymentStatus().equals(
                        isCanceled ? TossPaymentStatus.CANCELED : TossPaymentStatus.DONE))
                .findFirst()
                .orElse(null);

        return RandomMatchingDetailDto.builder()
                .matchingId(userRandomMatching.getRandomMatching().getId())
                .meetingTime(userRandomMatching.getRandomMatching().getMeetingTime())
                .matchingStatus(userRandomMatching.getMatchingStatus())
                .matchingType(MatchingType.RANDOM)
                .myOneThingContent(userRandomMatching.getMyOneThingContent())
                .applicationInfo(RandomMatchingDetailDto.ApplicationInfo.builder()
                    .district(userRandomMatching.getRandomMatching().getRandomDistrict().name())
                    .build())
                .myMatchingInfo(MatchingDetailDto.MyMatchingInfo.builder()
                        .job(userRandomMatching.getUser().getJob().getJobCategory())
                        .relationshipStatus(userRandomMatching.getUser().getRelationshipStatus())
                        .dietaryOption(userRandomMatching.getUser().getDietaryOption())
                        .language(userRandomMatching.getUser().getLanguage())
                        .build())
                .paymentInfo(RandomMatchingDetailDto.PaymentInfo.builder()
                        .matchingPrice(userRandomMatching.getRandomOrder().getPrice().getBasePrice().intValue())
                        .paymentPrice(userRandomMatching.getRandomOrder().getDiscountedPrice().intValue())
                        .refundPrice(isCanceled && payment != null && payment.getRefundAmount() != null
                                ? payment.getRefundAmount()
                                : null)
                        .requestedAt(payment != null && payment.getRequested_at() != null
                                ? payment.getRequested_at().toLocalDateTime()
                                : null)
                        .approvedAt(payment != null && payment.getApprovedAt() != null
                                ? payment.getApprovedAt().toLocalDateTime()
                                : null)
                        .build())
                .build();
    }

    List<MatchingDto> toDto(List<MatchingProjectionDto> matchingProjectionDto);
}
