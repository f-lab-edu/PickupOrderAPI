package com.pickup.restaurant.dto

import com.pickup.menuItem.dto.MenuItemRequest

data class RestaurantRequest(
    var email: String,
    var name: String,
    var businessNumber: String,
    var location: LocationDTO,
    var tel: String,
    var approvalStatus: ApprovalStatus,
    var businessHours: List<BusinessHourDTO>,
    var menuItems: MutableList<MenuItemRequest>
)



