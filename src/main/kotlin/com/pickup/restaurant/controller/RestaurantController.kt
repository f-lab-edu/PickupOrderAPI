package com.pickup.restaurant.controller

import com.pickup.restaurant.dto.ApprovalStatus
import com.pickup.restaurant.dto.RestaurantResponse
import com.pickup.restaurant.dto.RestaurantSignUpRequest
import com.pickup.restaurant.dto.RestaurantUpdateRequest
import com.pickup.restaurant.service.RestaurantService
import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.*
import javax.validation.Valid

@RestController
@RequestMapping("/restaurant")
@Validated
class RestaurantController(private val restaurantService: RestaurantService) {

    /**
     * 음식점 가입
     */
    @PostMapping("/signUp")
    fun signUpRestaurant(@Valid @RequestBody signUpRequest: RestaurantSignUpRequest): ResponseEntity<RestaurantResponse> {
        val newRestaurant = restaurantService.createRestaurant(signUpRequest)
        return ResponseEntity.ok(newRestaurant)
    }

    /**
     * 지역별 음식점 조회
     */
    @GetMapping("/location")
    fun getRestaurantsByLocation(
        @RequestParam sigugun: String,
        @RequestParam dongmyun: String
    ): ResponseEntity<List<RestaurantResponse>> {
        val restaurants = restaurantService.getRestaurantsByLocation(sigugun, dongmyun)
        return ResponseEntity.ok(restaurants)
    }

    /**
     * 음식점 상세 조회
     */
    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_RESTAURANT')")
    fun getRestaurantById(@PathVariable id: Long): ResponseEntity<RestaurantResponse> {
        val restaurant = restaurantService.getRestaurant(id)
        return ResponseEntity.ok(restaurant)
    }

    /**
     * 음식점 수정
     */
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_RESTAURANT')")
    fun updateRestaurant(
        @PathVariable id: Long,
        @RequestBody updateRequest: RestaurantUpdateRequest
    ): ResponseEntity<RestaurantResponse> {
        val updatedRestaurant = restaurantService.updateRestaurant(id, updateRequest)
        return ResponseEntity.ok(updatedRestaurant)
    }


    @PatchMapping("/{id}/status")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    fun updateRestaurantApprovalStatus(
        @PathVariable id: Long,
        @RequestBody status: ApprovalStatus
    ): ResponseEntity<RestaurantResponse> {
        val approvalStatus = restaurantService.updateApprovalStatus(id, status)
        return ResponseEntity.ok(approvalStatus)
    }
}