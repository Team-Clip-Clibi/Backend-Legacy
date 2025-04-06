package com.clip.notice.entity;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Getter
@NoArgsConstructor
public class Banner {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String exposureLocation;
    private String head;
    private String sub;
    private String imageUrl;
    private LocalDate exposureDate;
    private boolean isExposure;

    @Builder
    public Banner(String exposureLocation, String head, String sub, String imageUrl, LocalDate exposureDate, boolean isExposure) {
        this.exposureLocation = exposureLocation;
        this.head = head;
        this.sub = sub;
        this.imageUrl = imageUrl;
        this.exposureDate = exposureDate;
        this.isExposure = isExposure;
    }
}
