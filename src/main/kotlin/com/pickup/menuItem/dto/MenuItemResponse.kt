package com.pickup.menuItem.dto

import java.math.BigDecimal

data class MenuItemResponse(
    val id: Long,
    val restaurantId: Long,
    val name: String,
    val price: BigDecimal,
    val description: String?,
    val isSoldOut: Boolean
)