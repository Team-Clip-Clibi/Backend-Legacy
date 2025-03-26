package com.clip.auth.entity;

import com.clip.admin.entity.AdminUser;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class AdminToken {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @MapsId
    @OneToOne(targetEntity = AdminUser.class, fetch = FetchType.LAZY)
    @JoinColumn(foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
    private AdminUser adminUser;

    @Column
    private String refreshToken;

    @Builder
    public AdminToken(AdminUser adminUser, String refreshToken) {
        this.adminUser = adminUser;
        this.refreshToken = refreshToken;
    }
}
