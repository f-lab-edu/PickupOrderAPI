package com.pickup.restaurant.entity

import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id

@Entity
data class Location(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,
    /**
     * 특별시/광역시/도
     */
    val sido: String,
    /**
     * 일반시/구/군/면
     */
    val sigugun: String,
    /**
     * 동/면/리
     */
    val dongmyun: String,
    /**
     * 지번주소/도로명/개별주소
     */
    val rest: String
)