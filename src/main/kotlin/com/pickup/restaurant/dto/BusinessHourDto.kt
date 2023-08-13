package com.pickup.restaurant.dto

import java.time.LocalTime

data class BusinessHourDTO(
    val dayOfWeek: DayOfWeek,
    val open: LocalTime,
    val close: LocalTime
)