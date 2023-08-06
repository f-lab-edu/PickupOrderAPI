package com.pickup.order.dto

import com.pickup.order.entity.OrderStatus

data class OrderStatusResponse(
    val orderId: Long,
    val status: OrderStatus
)