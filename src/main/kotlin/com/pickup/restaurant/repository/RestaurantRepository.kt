package com.pickup.restaurant.repository

import com.pickup.restaurant.entity.Restaurant
import org.springframework.data.jpa.repository.JpaRepository

interface RestaurantRepository : JpaRepository<Restaurant, Long> {
    fun findByEmail(email: String): Restaurant?
    fun existsByEmail(email: String): Boolean
    fun existsByTel(tel: String): Boolean
    fun findByLocationSidoAndLocationSigugun(sido: String, sigugun: String): List<Restaurant>
}