package com.pickup.restaurant.dto

import com.pickup.menuItem.dto.MenuItemResponse

data class RestaurantResponse(
    val id: Long,
    var email: String,
    var name: String,
    var businessNumber: String,
    var location: LocationDTO,
    var tel: String,
    var approvalStatus: ApprovalStatus,
    var businessHours: List<BusinessHourDTO>,
    var menuItems: MutableList<MenuItemResponse>
)

