package com.pickup.order.entity

import java.time.LocalDateTime
import javax.persistence.Entity
import javax.persistence.Id
import javax.persistence.Version

@Entity
data class UserOrderCount(
    @Id
    val userId: Long,
    val restaurantId: Long,
    var count: Int,
    var lastOrderTime: LocalDateTime,

    @Version
    var version: Int? = null
)
