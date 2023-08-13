package com.pickup.order.repository

import com.pickup.order.entity.Order
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import java.time.LocalDateTime

interface OrderRepository : JpaRepository<Order, Long> {

    @Query(
        "SELECT COUNT(o) FROM Order o " +
                "WHERE o.restaurant.id = :restaurantId " +
                "AND o.pickupTime BETWEEN :startTime AND :pickupTime"
    )
    fun findOrdersWithinOneHourOfPickupTime(
        restaurantId: Long, pickupTime: LocalDateTime,
        startTime: LocalDateTime = pickupTime.minusHours(1)
    ): Int

}