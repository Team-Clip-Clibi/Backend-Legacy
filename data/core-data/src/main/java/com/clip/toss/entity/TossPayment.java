package com.clip.toss.entity;

import com.clip.common.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.OffsetDateTime;
import java.util.UUID;

@Getter
@Entity
@NoArgsConstructor
public class TossPayment extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String paymentId;

    @Column
    private UUID orderId;

    @Column
    private Integer amount;

    @Column
    private Integer refundAmount;

    @Enumerated(EnumType.STRING)
    @Column
    private TossPaymentStatus tossPaymentStatus;

    @Column
    private String currency;

    @Column
    private String method;

    @Column
    private OffsetDateTime requested_at;

    @Column
    private OffsetDateTime approvedAt;

    @Column
    private String receipt_url;

    @Column(length = 7000)
    private String jsonResponsePayload;

    @Builder
    public TossPayment(String paymentId, UUID orderId, Integer amount, Integer refundAmount, TossPaymentStatus tossPaymentStatus, String currency, String method, OffsetDateTime requested_at, OffsetDateTime approvedAt, String receipt_url, String jsonResponsePayload) {
        this.paymentId = paymentId;
        this.orderId = orderId;
        this.amount = amount;
        this.refundAmount = refundAmount;
        this.tossPaymentStatus = tossPaymentStatus;
        this.currency = currency;
        this.method = method;
        this.requested_at = requested_at;
        this.approvedAt = approvedAt;
        this.receipt_url = receipt_url;
        this.jsonResponsePayload = jsonResponsePayload;
    }
}
