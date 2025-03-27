package com.clip.report.entity;

import com.clip.common.entity.BaseEntity;
import com.clip.user.entity.User;
import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Report extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id",foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
    User user;

    @Size(max = 500)
    @Column(length = 1500)
    private String content;

    @Enumerated(EnumType.STRING)
    @Column
    private ReportCategory reportCategory;

    @Column
    private boolean isConfirmed;

    @Builder
    public Report(User user, String content, ReportCategory reportCategory) {
        this.user = user;
        this.content = content;
        this.reportCategory = reportCategory;
    }
}
