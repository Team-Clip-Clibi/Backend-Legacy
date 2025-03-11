package com.clip.api.user.mapper;

import com.clip.api.user.controller.dto.SignupDto;
import com.clip.user.entity.TermsAcceptance;
import com.clip.user.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING, unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface TermsAcceptanceMapper {
    TermsAcceptance toTermsAcceptance(SignupDto signupDto, User user);
}
