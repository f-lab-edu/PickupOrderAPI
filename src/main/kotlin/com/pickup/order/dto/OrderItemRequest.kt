package com.pickup.order.dto

data class OrderItemRequest(
    val menuItemId: Long,
    val quantity: Int
)

