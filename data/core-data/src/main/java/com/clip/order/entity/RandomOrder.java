package com.clip.order.entity;

import com.clip.common.entity.BaseEntity;
import com.clip.matching.entity.RandomMatching;
import com.clip.price.entity.RandomDiscount;
import com.clip.price.entity.RandomPrice;
import com.clip.toss.entity.TossPayment;
import com.clip.user.entity.User;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class RandomOrder extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private UUID orderId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id",foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
    private User user;

    @Enumerated(EnumType.STRING)
    @Column
    private RandomOrderStatus status;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "random_id")
    private RandomMatching randomMatching;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn
    private RandomPrice price;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn
    private RandomDiscount randomDiscount;

    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.PERSIST)
    @JoinColumn(name = "tosspayment",foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
    private List<TossPayment> tossPayment = new ArrayList<>();

    @Column
    private LocalDateTime expiredAt;

    @Builder
    public RandomOrder(User user, UUID orderId, RandomOrderStatus status, RandomMatching randomMatching, RandomPrice randomPrice, RandomDiscount randomDiscount, List<TossPayment> tossPayment
                      , LocalDateTime expiredAt) {
        this.user = user;
        this.orderId = orderId;
        this.status = status;
        this.randomMatching = randomMatching;
        this.tossPayment = tossPayment;
        this.price = randomPrice;
        this.randomDiscount = randomDiscount;
        this.expiredAt = expiredAt;
    }

    public void addTossPayment(TossPayment tossPayment) {
        if (Objects.isNull(this.tossPayment)) {
            this.tossPayment = new ArrayList<>();
        }
        this.tossPayment.add(tossPayment);
    }

    public void updateStatus(RandomOrderStatus randomOrderStatus) {
        this.status = randomOrderStatus;
    }

    @Transient
    public BigDecimal getDiscountedPrice() {
        if (Objects.isNull(randomDiscount)) {
            return price.getBasePrice();
        }
        switch (randomDiscount.getDiscountUnit()) {
            case AMOUNT -> {
                BigDecimal discountedPrice = price.getBasePrice().subtract(randomDiscount.getDiscountValue());
                return discountedPrice.compareTo(BigDecimal.ZERO) < 0 ? BigDecimal.ZERO : discountedPrice;
            }
            case PERCENTAGE -> {
                BigDecimal discountedPrice = price.getBasePrice()
                        .multiply(BigDecimal.ONE.subtract(randomDiscount.getDiscountValue().divide(BigDecimal.valueOf(100), 2, RoundingMode.HALF_UP)))
                        .setScale(0, RoundingMode.HALF_UP);

                return price.getBasePrice().subtract(discountedPrice).compareTo(BigDecimal.ZERO) < 0 ?
                        BigDecimal.ZERO :
                        discountedPrice;
            }
        }
        return BigDecimal.ZERO;
    }
}
