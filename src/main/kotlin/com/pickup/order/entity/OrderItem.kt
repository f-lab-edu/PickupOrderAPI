package com.pickup.order.entity

import com.pickup.menuItem.entity.MenuItem
import javax.persistence.*

@Entity
data class OrderItem(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "menu_item_id")
    val menuItem: MenuItem,

    val quantity: Int
)