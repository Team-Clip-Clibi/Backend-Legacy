package com.clip.api.matching.mapper;

import com.clip.api.matching.controller.dto.MatchingReviewDto;
import com.clip.matching.entity.OneThingMatching;
import com.clip.matching.entity.OneThingMatchingReview;
import com.clip.matching.entity.RandomMatching;
import com.clip.matching.entity.RandomMatchingReview;
import com.clip.user.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING, unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface MatchingReviewMapper {
    @Mapping(target = "isReviewPopupDismissed", constant = "false")
    OneThingMatchingReview toOneThingMatchingReview(final User user, final OneThingMatching oneThingMatching, final MatchingReviewDto matchingReviewDto);

    @Mapping(target = "isReviewPopupDismissed", constant = "false")
    RandomMatchingReview toRandomMatchingReview(final User user, final RandomMatching randomMatching, final MatchingReviewDto matchingReviewDto);
}
