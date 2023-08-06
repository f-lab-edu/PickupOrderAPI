package com.pickup.menuItem.repository

import com.pickup.menuItem.entity.MenuItem
import org.springframework.data.jpa.repository.JpaRepository

interface MenuItemRepository : JpaRepository<MenuItem, Long> {
    fun findByRestaurantIdAndId(restaurantId: Long, id: Long): MenuItem?
    fun findByRestaurantId(restaurantId: Long): List<MenuItem>
}
