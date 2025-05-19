package com.clip.order.entity;

import com.clip.common.entity.BaseEntity;
import com.clip.price.entity.OneThingDiscount;
import com.clip.price.entity.OneThingPrice;
import com.clip.toss.entity.TossPayment;
import com.clip.user.entity.User;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class OneThingOrder extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private UUID orderId;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn
    private OneThingPrice price;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id",foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
    private User user;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn
    private OneThingDiscount oneThingDiscount;

    @Enumerated(EnumType.STRING)
    @Column
    private OneThingOrderStatus status;

    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.PERSIST)
    @JoinColumn(name = "tosspayment",foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
    private List<TossPayment> tossPayment = new ArrayList<>();

    @Builder
    public OneThingOrder(User user, UUID orderId, OneThingPrice oneThingPrice, OneThingDiscount oneThingDiscount, OneThingOrderStatus status, List<TossPayment> tossPayment) {
        this.user = user;
        this.orderId = orderId;
        this.price = oneThingPrice;
        this.oneThingDiscount = oneThingDiscount;
        this.status = status;
        this.tossPayment = tossPayment;
    }

    public void addTossPayment(TossPayment tossPayment) {
        if (Objects.isNull(this.tossPayment)) {
            this.tossPayment = new ArrayList<>();
        }
        this.tossPayment.add(tossPayment);
    }

    public void updateStatus(OneThingOrderStatus status) {
        this.status = status;
    }

    @Transient
    public BigDecimal getDiscountedPrice() {
        if (Objects.isNull(oneThingDiscount)) {
            return price.getBasePrice();
        }
        switch (oneThingDiscount.getDiscountUnit()) {
            case AMOUNT -> {
                BigDecimal discountedPrice = price.getBasePrice().subtract(oneThingDiscount.getDiscountValue());
                return discountedPrice.compareTo(BigDecimal.ZERO) < 0 ? BigDecimal.ZERO : discountedPrice;
            }
            case PERCENTAGE -> {
                BigDecimal discountedPrice = price.getBasePrice()
                        .multiply(BigDecimal.ONE.subtract(oneThingDiscount.getDiscountValue().divide(BigDecimal.valueOf(100), 2, RoundingMode.HALF_UP)))
                        .setScale(0, RoundingMode.HALF_UP);

                return price.getBasePrice().subtract(discountedPrice).compareTo(BigDecimal.ZERO) < 0 ?
                        BigDecimal.ZERO :
                        discountedPrice;
            }
        }
        return BigDecimal.ZERO;
    }
}
