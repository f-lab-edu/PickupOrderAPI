package com.pickup.menuItem.service

import com.pickup.exception.CustomException
import com.pickup.exception.ErrorCode
import com.pickup.exception.ErrorMessage
import com.pickup.menuItem.dto.MenuItemRequest
import com.pickup.menuItem.dto.MenuItemResponse
import com.pickup.menuItem.dto.MenuItemUpdateRequest
import com.pickup.menuItem.entity.MenuItem
import com.pickup.menuItem.repository.MenuItemRepository
import com.pickup.restaurant.repository.RestaurantRepository
import org.modelmapper.ModelMapper
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class MenuItemService(
    private val menuItemRepository: MenuItemRepository,
    private val modelMapper: ModelMapper,
    private val restaurantRepository: RestaurantRepository
) {

    @Transactional
    fun createMenuItem(restaurantId: Long, request: MenuItemRequest): MenuItemResponse {
        val restaurant = restaurantRepository.findById(restaurantId)
            .orElseThrow { CustomException(ErrorCode.NOT_FOUND, ErrorMessage.RESTAURANT_NOT_FOUND) }

        val menuItem = modelMapper.map(request, MenuItem::class.java).apply { this.restaurant = restaurant }
        val savedMenuItem = menuItemRepository.save(menuItem)
        return modelMapper.map(savedMenuItem, MenuItemResponse::class.java)
    }

    /**
     *  해당 매장 메뉴 목록 조회
     */
    @Transactional(readOnly = true)
    fun getMenuItemsByRestaurantId(restaurantId: Long): List<MenuItemResponse> {
        val menuItems = menuItemRepository.findByRestaurantId(restaurantId)
        return menuItems.map { modelMapper.map(it, MenuItemResponse::class.java) }
    }

    @Transactional(readOnly = true)
    fun getMenuItem(restaurantId: Long, menuItemId: Long): MenuItemResponse {
        val menuItem = findByRestaurantIdAndId(restaurantId, menuItemId)

        return modelMapper.map(menuItem, MenuItemResponse::class.java)
    }

    @Transactional
    fun updateMenuItem(
        restaurantId: Long,
        menuItemId: Long,
        menuItemUpdateRequest: MenuItemUpdateRequest
    ): MenuItemResponse {
        val menuItem = findByRestaurantIdAndId(restaurantId, menuItemId)

        modelMapper.map(menuItemUpdateRequest, menuItem)
        val updatedMenuItem = menuItemRepository.save(menuItem)

        return modelMapper.map(updatedMenuItem, MenuItemResponse::class.java)
    }

    fun deleteMenuItem(restaurantId: Long, menuItemId: Long) {
        val menuItem = menuItemRepository.findByRestaurantIdAndId(restaurantId, menuItemId)
            ?: throw CustomException(ErrorCode.NOT_FOUND, ErrorMessage.MENU_NOT_FOUND)

        menuItemRepository.delete(menuItem)
    }

    @Transactional(readOnly = true)
    fun findByRestaurantIdAndId(restaurantId: Long, menuItemId: Long): MenuItem {
        return menuItemRepository.findByRestaurantIdAndId(restaurantId, menuItemId)
            ?: throw CustomException(ErrorCode.NOT_FOUND, ErrorMessage.MENU_NOT_FOUND)
    }
}
