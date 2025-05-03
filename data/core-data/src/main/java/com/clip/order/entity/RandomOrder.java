package com.clip.order.entity;

import com.clip.common.entity.BaseEntity;
import com.clip.matching.entity.RandomMatching;
import com.clip.toss.entity.TossPayment;
import com.clip.user.entity.User;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Getter
@Entity
@Table(uniqueConstraints = {@UniqueConstraint(columnNames = {"random_id", "user_id"})})
@NoArgsConstructor
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

    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.PERSIST)
    @JoinColumn(name = "tosspayment",foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
    private List<TossPayment> tossPayment = new ArrayList<>();

    @Builder
    public RandomOrder(User user, UUID orderId, RandomOrderStatus status, RandomMatching randomMatching, List<TossPayment> tossPayment) {
        this.user = user;
        this.orderId = orderId;
        this.status = status;
        this.randomMatching = randomMatching;
        this.tossPayment = tossPayment;
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
}
