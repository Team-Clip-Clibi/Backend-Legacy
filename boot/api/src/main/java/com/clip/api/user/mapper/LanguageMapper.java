package com.clip.api.user.mapper;

import com.clip.api.user.controller.dto.Language;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING, unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface LanguageMapper {
    default String toStringLanguage(Language language) {
        return language.getValue();
    }
}
