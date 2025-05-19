package com.clip.price.entity;

import com.clip.common.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class OneThingDiscount extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "discountValue", nullable = false, precision = 10, scale = 2)
    private BigDecimal discountValue;

    @Enumerated(EnumType.STRING)
    @Column
    private DiscountUnit discountUnit;

    @Enumerated(EnumType.STRING)
    @Column
    private DiscountType discountType;

    @Builder
    public OneThingDiscount(BigDecimal discountValue, DiscountUnit discountUnit, DiscountType discountType) {
        this.discountValue = discountValue;
        this.discountUnit = discountUnit;
        this.discountType = discountType;
    }
}
