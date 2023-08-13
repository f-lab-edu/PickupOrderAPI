package com.pickup.restaurant.dto

import com.pickup.menuItem.entity.MenuItem
import com.pickup.restaurant.entity.BusinessHour

data class RestaurantUpdateRequest(
    var password: String? = null,

    val businessHours: List<BusinessHour>,

    val menuItems: MutableList<MenuItem>
)
