package com.pickup.menuItem.entity

import com.pickup.restaurant.entity.Restaurant
import java.math.BigDecimal
import javax.persistence.*

@Entity
data class MenuItem(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long = 0,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "restaurant_id")
    var restaurant: Restaurant,

    @Column(nullable = false)
    val name: String,

    @Column(nullable = false)
    val price: BigDecimal,

    val description: String?,

    @Column(name = "sold_out", nullable = false)
    var isSoldOut: Boolean = false
)