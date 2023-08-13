package com.pickup.order.dto

import com.pickup.order.entity.OrderItem
import com.pickup.restaurant.entity.Restaurant
import com.pickup.user.entity.User
import java.time.LocalDateTime

data class OrderRequest(
    val user: User,
    val restaurant: Restaurant,
    val orderItems: List<OrderItem>,
    val pickupTime: LocalDateTime
)
