package com.pickup.order.service

import com.pickup.exception.CustomException
import com.pickup.exception.ErrorCode
import com.pickup.exception.ErrorMessage
import com.pickup.menuItem.entity.MenuItem
import com.pickup.order.dto.OrderRequest
import com.pickup.order.entity.Order
import com.pickup.order.entity.OrderItem
import com.pickup.order.entity.OrderStatus
import com.pickup.order.entity.UserOrderCount
import com.pickup.order.repository.OrderRepository
import com.pickup.order.repository.UserOrderCountRepository
import com.pickup.restaurant.entity.Restaurant
import com.pickup.restaurant.repository.RestaurantRepository
import com.pickup.user.entity.User
import com.pickup.user.repository.UserRepository
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.mockito.ArgumentMatchers.any
import org.mockito.ArgumentMatchers.anyLong
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.Mockito.`when`
import org.mockito.MockitoAnnotations
import org.modelmapper.ModelMapper
import org.springframework.boot.test.context.SpringBootTest
import java.time.LocalDateTime
import java.util.*

@SpringBootTest
internal class OrderServiceTest {
    @Mock
    lateinit var userOrderCountRepository: UserOrderCountRepository

    @Mock
    lateinit var orderRepository: OrderRepository

    @Mock
    private lateinit var restaurantRepository: RestaurantRepository

    @Mock
    private lateinit var userRepository: UserRepository

    @InjectMocks
    lateinit var modelMapper: ModelMapper

    lateinit var orderService: OrderService

    val maxOrdersPerOneHourOfPickup = 10

    @BeforeEach
    fun setup() {
        MockitoAnnotations.openMocks(this)
        orderService = OrderService(orderRepository, userOrderCountRepository)
    }

    @Test
    fun 주문_성공() {

        val restaurantId = 1L
        val userId = 1L

        val user = Mockito.mock(User::class.java)
        val restaurant = Mockito.mock(Restaurant::class.java)
        val menuItem = Mockito.mock(MenuItem::class.java)
        val pickupTime = LocalDateTime.now().plusHours(1)

        `when`(user.id).thenReturn(userId)
        `when`(restaurant.id).thenReturn(restaurantId)

        val request = OrderRequest(user, restaurant, listOf(OrderItem(null, menuItem, 1)), pickupTime)

        val orderItem = Mockito.mock(OrderItem::class.java)

        val order = Order(
            id = 1,
            user = user,
            restaurant = restaurant,
            orderItems = listOf(orderItem).toMutableList(),
            pickupTime = pickupTime
        )

        `when`(userOrderCountRepository.findByUserIdAndRestaurantId(anyLong(), anyLong())).thenReturn(null)
        `when`(orderRepository.save(any())).thenReturn(order)

        val response = orderService.createOrder(request)

        Assertions.assertNotNull(response)
        Assertions.assertEquals(response.orderId, order.id)
    }


    @Test
    fun 주문_실패_동시주문() {
        val restaurantId = 2L
        val userId = 2L
        val count = 1

        val user = Mockito.mock(User::class.java)
        val restaurant = Mockito.mock(Restaurant::class.java)
        val menuItem = Mockito.mock(MenuItem::class.java)

        val userOrderCount = UserOrderCount(
            userId = userId,
            restaurantId = restaurantId,
            count = count,
            lastOrderTime = LocalDateTime.now()
        )

        val request = OrderRequest(
            user,
            restaurant,
            listOf(OrderItem(null, menuItem, 1)),
            pickupTime = LocalDateTime.now().plusHours(1)
        )


        `when`(user.id).thenReturn(userId)
        `when`(restaurant.id).thenReturn(restaurantId)

        `when`(userOrderCountRepository.findByUserIdAndRestaurantId(userId, restaurantId))
            .thenReturn(userOrderCount)


        assertThrows<CustomException> {
            orderService.createOrder(request)
        }.apply {
            Assertions.assertEquals(ErrorCode.LIMIT_EXCEEDED, this.errorCode)
            Assertions.assertEquals(ErrorMessage.LIMIT_EXCEEDED_ORDER, this.errorMessage)
        }

    }

    @Test
    fun 주문_실패_최대주문수량() {
        val restaurantId = 2L
        val userId = 2L
        val count = 1

        val user = Mockito.mock(User::class.java)
        val restaurant = Mockito.mock(Restaurant::class.java)
        val menuItem = Mockito.mock(MenuItem::class.java)
        val pickupTime = LocalDateTime.now().plusHours(1)

        val userOrderCount = UserOrderCount(
            userId = userId,
            restaurantId = restaurantId,
            count = count,
            lastOrderTime = LocalDateTime.now()
        )

        val request = OrderRequest(
            user,
            restaurant,
            listOf(OrderItem(null, menuItem, 1)),
            pickupTime = pickupTime
        )


        `when`(user.id).thenReturn(userId)
        `when`(restaurant.id).thenReturn(restaurantId)

        `when`(userOrderCountRepository.findByUserIdAndRestaurantId(userId, restaurantId))
            .thenReturn(userOrderCount)
        `when`(orderRepository.findOrdersWithinOneHourOfPickupTime(restaurantId, pickupTime))
            .thenReturn(maxOrdersPerOneHourOfPickup + 1)


        assertThrows<CustomException> {
            orderService.createOrder(request)
        }.apply {
            Assertions.assertEquals(ErrorCode.LIMIT_EXCEEDED, this.errorCode)
            Assertions.assertEquals(ErrorMessage.LIMIT_EXCEEDED_RESTAURANT, this.errorMessage)
        }

    }

    @Test
    fun 주문_상태_변경_성공() {

        val orderId = 1L

        val user = Mockito.mock(User::class.java)
        val restaurant = Mockito.mock(Restaurant::class.java)
        val pickupTime = LocalDateTime.now().plusHours(1)
        val orderItem = Mockito.mock(OrderItem::class.java)
        val rejected = OrderStatus.REJECTED

        val order = Order(
            id = orderId,
            user = user,
            restaurant = restaurant,
            orderItems = listOf(orderItem).toMutableList(),
            pickupTime = pickupTime,
            orderStatus = OrderStatus.ORDERED
        )

        val updateOrder = Order(
            id = orderId,
            user = user,
            restaurant = restaurant,
            orderItems = listOf(orderItem).toMutableList(),
            pickupTime = pickupTime,
            orderStatus = rejected
        )

        `when`(orderRepository.findById(any())).thenReturn(Optional.of(order))
        `when`(orderRepository.save(any())).thenReturn(updateOrder)

        val response = orderService.updateOrderStatus(orderId, rejected)

        Assertions.assertEquals(response.status, rejected)
    }

    @Test
    fun 주문_상태_변경_실패() {

        val orderId = 1L

        val user = Mockito.mock(User::class.java)
        val restaurant = Mockito.mock(Restaurant::class.java)
        val pickupTime = LocalDateTime.now().plusHours(1)
        val orderItem = Mockito.mock(OrderItem::class.java)
        val accepted = OrderStatus.ACCEPTED

        val order = Order(
            id = orderId,
            user = user,
            restaurant = restaurant,
            orderItems = listOf(orderItem).toMutableList(),
            pickupTime = pickupTime,
            orderStatus = OrderStatus.REJECTED
        )

        `when`(orderRepository.findById(any())).thenReturn(Optional.of(order))

        assertThrows<CustomException> {
            orderService.updateOrderStatus(orderId, accepted)
        }.apply {
            Assertions.assertEquals(ErrorCode.NOT_CHANGED, this.errorCode)
            Assertions.assertEquals(ErrorMessage.INVALID_STATUS_CHANGE, this.errorMessage)
        }
    }

}