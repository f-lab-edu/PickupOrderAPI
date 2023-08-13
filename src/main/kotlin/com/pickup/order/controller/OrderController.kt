package com.pickup.order.controller

import com.pickup.order.dto.OrderRequest
import com.pickup.order.dto.OrderResponse
import com.pickup.order.dto.OrderStatusResponse
import com.pickup.order.entity.OrderStatus
import com.pickup.order.service.OrderService
import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/orders")
@Validated
class OrderController(
    private val orderService: OrderService
) {
    @PostMapping
    @PreAuthorize("hasRole('ROLE_USER')")
    fun createOrder(@RequestBody request: OrderRequest): ResponseEntity<OrderResponse> {
        val order = orderService.createOrder(request)
        return ResponseEntity.ok(order)
    }

    @GetMapping("/{orderId}")
    fun getOrder(@PathVariable orderId: Long): ResponseEntity<OrderResponse> {
        val order = orderService.getOrder(orderId)
        return ResponseEntity.ok(order)
    }

    @PatchMapping("/{orderId}/status")
    @PreAuthorize("hasRole('ROLE_RESTAURANT')")
    fun updateOrderStatus(
        @PathVariable orderId: Long,
        @RequestBody status: OrderStatus
    ): ResponseEntity<OrderStatusResponse> {
        val orderStatusResponse = orderService.updateOrderStatus(orderId, status)
        return ResponseEntity.ok(orderStatusResponse)
    }

}