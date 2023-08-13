package com.pickup.order.service

import com.pickup.exception.CustomException
import com.pickup.exception.ErrorCode
import com.pickup.exception.ErrorMessage
import com.pickup.order.dto.OrderRequest
import com.pickup.order.dto.OrderResponse
import com.pickup.order.dto.OrderStatusResponse
import com.pickup.order.entity.Order
import com.pickup.order.entity.OrderStatus
import com.pickup.order.entity.UserOrderCount
import com.pickup.order.repository.OrderRepository
import com.pickup.order.repository.UserOrderCountRepository
import org.springframework.orm.ObjectOptimisticLockingFailureException
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDateTime

@Service
class OrderService(
    private val orderRepository: OrderRepository,
    private val userOrderCountRepository: UserOrderCountRepository
) {
    private val maxOrdersPerMinutePerUser = 2
    private val maxOrdersPerOneHourOfPickup = 10

    @Transactional
    fun createOrder(request: OrderRequest): OrderResponse {
        val recentOrdersCount =
            orderRepository.findOrdersWithinOneHourOfPickupTime(request.restaurant.id!!, request.pickupTime)

        // 매장 최대 주문수량
        if (recentOrdersCount > maxOrdersPerOneHourOfPickup) {
            throw CustomException(ErrorCode.LIMIT_EXCEEDED, ErrorMessage.LIMIT_EXCEEDED_RESTAURANT)
        }

        val orderCount =
            userOrderCountRepository.findByUserIdAndRestaurantId(request.user.id!!, request.restaurant.id!!)
                ?: UserOrderCount(request.user.id!!, request.restaurant.id!!, 0, LocalDateTime.now())

        // 1분 이내인지 확인
        if (orderCount.lastOrderTime.plusMinutes(1).isBefore(LocalDateTime.now())) {
            orderCount.count = 0
        }

        orderCount.count++
        orderCount.lastOrderTime = LocalDateTime.now()

        // 최대 주문회수 이내인지
        if (orderCount.count >= maxOrdersPerMinutePerUser) {
            throw CustomException(ErrorCode.LIMIT_EXCEEDED, ErrorMessage.LIMIT_EXCEEDED_ORDER)

        }

        try {
            userOrderCountRepository.save(orderCount)
        } catch (e: ObjectOptimisticLockingFailureException) {
            throw CustomException(ErrorCode.DUPLICATE, ErrorMessage.CONCURRENT_ORDER)
        }

        val order = mapToOrder(request)

        val saveOrder = orderRepository.save(order)
        return mapToOrderResponse(saveOrder)
    }

    @Transactional(readOnly = true)
    fun getOrder(orderId: Long): OrderResponse {
        val order = findById(orderId)
        return mapToOrderResponse(order)
    }

    @Transactional
    fun updateOrderStatus(orderId: Long, status: OrderStatus): OrderStatusResponse {
        val order = findById(orderId)
        order.updateStatus(status)
        orderRepository.save(order)
        return OrderStatusResponse(order.id!!, status)
    }

    @Transactional(readOnly = true)
    fun findById(orderId: Long): Order {
        return orderRepository.findById(orderId).orElseThrow {
            CustomException(ErrorCode.NOT_FOUND, ErrorMessage.ORDER_NOT_FOUND)
        }
    }

    fun mapToOrder(request: OrderRequest): Order {
        return Order(
            user = request.user,
            restaurant = request.restaurant,
            orderItems = request.orderItems.toMutableList(),
            pickupTime = request.pickupTime
        )
    }

    fun mapToOrderResponse(order: Order): OrderResponse {
        return OrderResponse(
            orderId = order.id!!,
            user = order.user,
            restaurantId = order.restaurant.id!!,
            orderItems = order.orderItems,
            orderTime = order.orderTime,
            orderStatus = order.orderStatus
        )
    }
}