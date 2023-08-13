package com.pickup.menuItem.controller

import com.pickup.menuItem.dto.MenuItemRequest
import com.pickup.menuItem.dto.MenuItemResponse
import com.pickup.menuItem.dto.MenuItemUpdateRequest
import com.pickup.menuItem.service.MenuItemService
import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.*
import javax.validation.Valid

@RestController
@RequestMapping("/restaurants/{restaurantId}/menuItems")
class MenuItemController(private val menuItemService: MenuItemService) {

    /**
     * 메뉴 생성
     */
    @PostMapping
    @PreAuthorize("hasRole('ROLE_RESTAURANT')")
    fun createMenuItem(
        @PathVariable restaurantId: Long,
        @Valid @RequestBody request: MenuItemRequest,
    ): ResponseEntity<MenuItemResponse> {

        val menuItem = menuItemService.createMenuItem(restaurantId, request)
        return ResponseEntity.ok(menuItem)
    }

    /**
     * 음식점 메뉴 조회
     */
    @GetMapping
    fun getMenuItemsByRestaurantId(@PathVariable restaurantId: Long): ResponseEntity<List<MenuItemResponse>> {
        val menuItems = menuItemService.getMenuItemsByRestaurantId(restaurantId)
        return ResponseEntity.ok(menuItems)
    }

    /**
     * 특정 메뉴 정보 조회
     */
    @GetMapping("/{menuItemId}")
    fun getMenuItem(
        @PathVariable restaurantId: Long,
        @PathVariable menuItemId: Long
    ): ResponseEntity<MenuItemResponse> {
        val menuItem = menuItemService.getMenuItem(restaurantId, menuItemId)
        return ResponseEntity.ok(menuItem)
    }

    @PutMapping("/{menuItemId}")
    @PreAuthorize("hasRole('ROLE_RESTAURANT')")
    fun updateMenuItem(
        @PathVariable restaurantId: Long,
        @PathVariable menuItemId: Long,
        @RequestBody menuItemUpdateRequest: MenuItemUpdateRequest
    ): ResponseEntity<MenuItemResponse> {
        val updatedMenuItem = menuItemService.updateMenuItem(restaurantId, menuItemId, menuItemUpdateRequest)
        return ResponseEntity.ok(updatedMenuItem)
    }

    @DeleteMapping("/{menuItemId}")
    @PreAuthorize("hasRole('ROLE_RESTAURANT')")
    fun deleteMenuItem(@PathVariable restaurantId: Long, @PathVariable menuItemId: Long): ResponseEntity<Void> {
        menuItemService.deleteMenuItem(restaurantId, menuItemId)
        return ResponseEntity.noContent().build()
    }

}

