package com.clip.meta.entity;

import jakarta.persistence.*;
import lombok.Getter;

@Getter
@Entity
public class AppVersion {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String version;
    @Enumerated(EnumType.STRING)
    private OSType type;
}
