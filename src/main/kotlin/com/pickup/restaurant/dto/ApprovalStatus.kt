package com.pickup.restaurant.dto

enum class ApprovalStatus {
    /**
     * 승인 대기중
     */
    PENDING,

    /**
     * 승인 완료
     */
    APPROVED,

    /**
     *  승인 거절
     */
    REJECTED
}