package com.pickup.order.dto

data class OrderRequest(
    val userId: Long,
    val restaurantId: Long,
    val orderItems: List<OrderItemRequest>
)
