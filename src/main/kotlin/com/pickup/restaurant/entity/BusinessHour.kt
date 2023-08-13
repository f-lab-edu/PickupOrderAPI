package com.pickup.restaurant.entity

import com.pickup.restaurant.dto.DayOfWeek
import java.time.LocalTime
import javax.persistence.*

@Entity
class BusinessHour(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "restaurant_id")
    var restaurant: Restaurant,
    @Enumerated(EnumType.STRING)
    val dayOfWeek: DayOfWeek,
    val open: LocalTime,
    val close: LocalTime
)