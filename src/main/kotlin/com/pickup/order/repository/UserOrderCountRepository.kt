package com.pickup.order.repository

import com.pickup.order.entity.UserOrderCount
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface UserOrderCountRepository : JpaRepository<UserOrderCount, Long> {
    fun findByUserIdAndRestaurantId(userId: Long, restaurantId: Long): UserOrderCount?
}