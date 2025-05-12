package com.clip.order.exception;

public class OrderNotFoundException extends RuntimeException {
    public OrderNotFoundException() {
        super("주문서 번호에 해당하는 주문서를 찾을 수 없습니다");
    }
}
