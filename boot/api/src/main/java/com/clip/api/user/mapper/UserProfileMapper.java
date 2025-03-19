package com.clip.api.user.mapper;

import com.clip.api.user.controller.dto.RetrieveUserProfileInfo;
import com.clip.user.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING, unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface UserProfileMapper {
    RetrieveUserProfileInfo toRetrieveUserProfileInfo(User user);
}
