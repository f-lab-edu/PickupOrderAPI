package com.pickup.order.entity

enum class OrderStatus {
    /**
     *  주문 접수
     */
    ORDERED,

    /**
     * 주문 승인
     */
    ACCEPTED,

    /**
     *  주문 거절
     */
    REJECTED,

    /**
     *  픽업 대기
     */
    PICKUP_READY,

    /**
     * 픽업 완료
     */
    PICKED_UP

}