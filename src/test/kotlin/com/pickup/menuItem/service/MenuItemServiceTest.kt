package com.pickup.menuItem.service

import com.pickup.menuItem.dto.MenuItemRequest
import com.pickup.menuItem.dto.MenuItemResponse
import com.pickup.menuItem.entity.MenuItem
import com.pickup.menuItem.repository.MenuItemRepository
import com.pickup.restaurant.entity.Restaurant
import com.pickup.restaurant.repository.RestaurantRepository
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.mock
import org.mockito.Mockito.`when`
import org.mockito.junit.MockitoJUnitRunner
import org.modelmapper.ModelMapper
import org.springframework.boot.test.context.SpringBootTest
import java.math.BigDecimal
import java.util.*


@RunWith(MockitoJUnitRunner::class)
@SpringBootTest
internal class MenuItemServiceTest {
    @Mock
    private lateinit var menuItemRepository: MenuItemRepository

    @Mock
    private lateinit var modelMapper: ModelMapper

    @Mock
    private lateinit var restaurantRepository: RestaurantRepository

    private lateinit var menuItemService: MenuItemService

    @BeforeEach
    fun setup() {
        menuItemService = MenuItemService(menuItemRepository, modelMapper, restaurantRepository)
    }

    @Test
    fun createMenuItem() {
        // Given
        val restaurantId = 1L
        val restaurant = mock(Restaurant::class.java)
        `when`(restaurant.id).thenReturn(restaurantId)
        `when`(restaurantRepository.findById(restaurantId)).thenReturn(Optional.of(restaurant))

        val price = BigDecimal("10000")
        val menuItemDto = MenuItemRequest(
            restaurantId = restaurantId,
            name = "메뉴 TEST",
            price = price,
            description = "메뉴 설명",
            isSoldOut = false
        )
        val menuItem = MenuItem(
            id = 1L,
            name = "메뉴 TEST",
            price = price,
            isSoldOut = false,
            restaurant = restaurant,
            description = "메뉴 설명"
        )

        val menuItemResponse = MenuItemResponse(
            id = menuItem.id,
            name = menuItem.name,
            price = menuItem.price,
            description = menuItem.description,
            isSoldOut = menuItem.isSoldOut,
            restaurantId = restaurantId
        )

        `when`(modelMapper.map(menuItemDto, MenuItem::class.java)).thenReturn(menuItem)
        `when`(menuItemRepository.save(menuItem)).thenReturn(menuItem)
        `when`(modelMapper.map(menuItem, MenuItemResponse::class.java)).thenReturn(menuItemResponse)


        // When
        val result = menuItemService.createMenuItem(restaurantId, menuItemDto)

        // Then
        assertEquals(menuItemResponse.name, result.name)
        assertEquals(menuItemResponse.price, result.price)
        assertEquals(menuItemResponse.isSoldOut, result.isSoldOut)
    }

    @Test
    fun getMenuItemsByRestaurantId() {
    }

    @Test
    fun getMenuItem() {
    }

    @Test
    fun updateMenuItem() {
    }

    @Test
    fun deleteMenuItem() {
    }
}