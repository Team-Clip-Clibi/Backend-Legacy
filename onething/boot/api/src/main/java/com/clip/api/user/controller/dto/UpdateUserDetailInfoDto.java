package com.clip.api.user.controller.dto;

import com.clip.user.entity.City;
import com.clip.user.entity.County;
import com.clip.user.entity.Gender;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Getter
@NoArgsConstructor
public class UpdateUserDetailInfoDto {
    private Gender gender;
    private LocalDate birth;
    private City city;
    private County county;

    @Builder
    public UpdateUserDetailInfoDto(Gender gender, LocalDate birth, City city, County county) {
        this.gender = gender;
        this.birth = birth;
        this.city = city;
        this.county = county;
    }
}
