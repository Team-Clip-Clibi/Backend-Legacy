package com.clip.api.user.mapper;

import com.clip.api.user.controller.dto.Language;
import com.clip.api.user.controller.dto.LanguageDto;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.ReportingPolicy;

import java.util.Objects;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING, unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface LanguageMapper {
    default String toStringLanguage(Language language) {
        return language.getValue();
    }

    default LanguageDto toLanguageDto(String language) {
        if (Objects.isNull(language)) {
            return LanguageDto.builder()
                    .language(null)
                    .build();
        }
        for (Language lang : Language.values()) {
            if (lang.getValue().equals(language)) {
                return LanguageDto.builder()
                        .language(lang)
                        .build();
            }
        }
        throw new IllegalArgumentException("다음 언어는 DTO로 변환할 수 없는 언어입니다: " + language);
    }
}
