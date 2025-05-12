package com.clip.api.payment.feign.dto;

import lombok.Builder;
import lombok.Data;

import java.time.OffsetDateTime;
import java.util.UUID;

@Builder
@Data
public class PaymentObject {
    private String mId;
    private String lastTransactionKey;
    private String paymentKey;
    private UUID orderId;
    private String orderName;
    private Integer taxExemptionAmount;
    private String status;
    private OffsetDateTime requestedAt;
    private OffsetDateTime approvedAt;
    private Boolean useEscrow;
    private Boolean cultureExpense;
    private Card card;
    private EasyPay easyPay;
    private String country;
    private Boolean isPartialCancelable;
    private Receipt receipt;
    private Checkout checkout;
    private String currency;
    private Integer totalAmount;
    private Integer balanceAmount;
    private Integer suppliedAmount;
    private Integer vat;
    private Integer taxFreeAmount;
    private String method;
    private String version;

    @Data
    public static class Card {
        private String issuerCode;
        private String acquirerCode;
        private String number;
        private Integer installmentPlanMonths;
        private Boolean isInterestFree;
        private String interestPayer;
        private String approveNo;
        private Boolean useCardPoint;
        private String cardType;
        private String ownerType;
        private String acquireStatus;
        private Integer amount;
    }

    @Data
    public static class EasyPay {
        private String provider;
        private Integer amount;
        private Integer discountAmount;
    }

    @Data
    public static class Receipt {
        private String url;
    }

    @Data
    public static class Checkout {
        private String url;
    }
}
