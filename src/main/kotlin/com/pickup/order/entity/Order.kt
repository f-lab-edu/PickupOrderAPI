package com.pickup.order.entity

import com.pickup.exception.CustomException
import com.pickup.exception.ErrorCode
import com.pickup.exception.ErrorMessage
import com.pickup.restaurant.entity.Restaurant
import com.pickup.user.entity.User
import java.time.LocalDateTime
import javax.persistence.*

@Entity
data class Order(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    val user: User,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "restaurant_id")
    val restaurant: Restaurant,

    @OneToMany(cascade = [CascadeType.ALL], fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id")
    val orderItems: MutableList<OrderItem> = mutableListOf(),

    @Column(nullable = false)
    val orderTime: LocalDateTime = LocalDateTime.now(),

    @Column(nullable = false)
    val pickupTime: LocalDateTime,

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    var orderStatus: OrderStatus = OrderStatus.ORDERED,

    ) {
    // 주문 상태 유효성 검사
    fun isValidStatusChange(newStatus: OrderStatus): Boolean {
        return when (orderStatus) {
            OrderStatus.ORDERED -> newStatus in listOf(OrderStatus.ACCEPTED, OrderStatus.REJECTED)
            OrderStatus.ACCEPTED -> newStatus == OrderStatus.PICKUP_READY
            OrderStatus.REJECTED -> false
            OrderStatus.PICKUP_READY -> newStatus == OrderStatus.PICKED_UP
            OrderStatus.PICKED_UP -> false
        }
    }

    // 주문 상태 업데이트
    fun updateStatus(newStatus: OrderStatus) {
        if (!isValidStatusChange(newStatus)) {
            throw CustomException(ErrorCode.NOT_CHANGED, ErrorMessage.INVALID_STATUS_CHANGE)
        }
        this.orderStatus = newStatus
    }

}