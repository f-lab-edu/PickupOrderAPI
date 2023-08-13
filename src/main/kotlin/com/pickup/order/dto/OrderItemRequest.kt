package com.pickup.order.dto

import com.pickup.menuItem.entity.MenuItem

data class OrderItemRequest(
    val menuItem: MenuItem,
    val quantity: Int
)

