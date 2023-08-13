package com.pickup.order.service

import com.pickup.exception.CustomException
import com.pickup.exception.ErrorCode
import com.pickup.exception.ErrorMessage
import com.pickup.order.dto.OrderRequest
import com.pickup.order.dto.OrderResponse
import com.pickup.order.dto.OrderStatusResponse
import com.pickup.order.entity.Order
import com.pickup.order.entity.OrderStatus
import com.pickup.order.repository.OrderRepository
import org.modelmapper.ModelMapper
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class OrderService(
    private val orderRepository: OrderRepository,
    private val modelMapper: ModelMapper
) {
    @Transactional
    fun createOrder(request: OrderRequest): OrderResponse {
        val order = modelMapper.map(request, Order::class.java)
        val saveOrder = orderRepository.save(order)
        return modelMapper.map(saveOrder, OrderResponse::class.java);
    }

    @Transactional(readOnly = true)
    fun getOrder(orderId: Long): OrderResponse {
        val order = findById(orderId)
        return modelMapper.map(order, OrderResponse::class.java)
    }

    @Transactional
    fun updateOrderStatus(orderId: Long, status: OrderStatus): OrderStatusResponse {
        val order = findById(orderId)
        order.updateStatus(status)
        orderRepository.save(order)
        return OrderStatusResponse(order.id!!, status)
    }

    @Transactional
    fun cancelOrder(orderId: Long): OrderResponse {
        val order = findById(orderId)
        orderRepository.delete(order)
        return modelMapper.map(order, OrderResponse::class.java)
    }

    @Transactional(readOnly = true)
    fun findById(orderId: Long): Order {
        return orderRepository.findById(orderId).orElseThrow {
            CustomException(ErrorCode.NOT_FOUND, ErrorMessage.ORDER_NOT_FOUND)
        }
    }
}