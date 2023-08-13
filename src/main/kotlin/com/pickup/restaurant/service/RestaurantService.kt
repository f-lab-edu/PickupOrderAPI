package com.pickup.restaurant.service

import com.pickup.exception.CustomException
import com.pickup.exception.ErrorCode
import com.pickup.exception.ErrorMessage
import com.pickup.restaurant.dto.ApprovalStatus
import com.pickup.restaurant.dto.RestaurantResponse
import com.pickup.restaurant.dto.RestaurantSignUpRequest
import com.pickup.restaurant.dto.RestaurantUpdateRequest
import com.pickup.restaurant.entity.Restaurant
import com.pickup.restaurant.repository.RestaurantRepository
import org.modelmapper.ModelMapper
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class RestaurantService(
    val restaurantRepository: RestaurantRepository,
    val modelMapper: ModelMapper,
    private val passwordEncoder: PasswordEncoder
) {

    @Transactional
    fun createRestaurant(signUpRequest: RestaurantSignUpRequest): RestaurantResponse {
        // 이메일 중복 검사
        if (restaurantRepository.existsByEmail(signUpRequest.email)) {
            throw CustomException(ErrorCode.DUPLICATE, ErrorMessage.DUPLICATE_EMAIL)
        }

        // 매장 전화 번호 중복 검사
        if (restaurantRepository.existsByTel(signUpRequest.tel)) {
            throw CustomException(ErrorCode.DUPLICATE, ErrorMessage.DUPLICATE_TEL)
        }
        // 사업자 등록 번호 중복 검사
        if (restaurantRepository.existsByTel(signUpRequest.tel)) {
            throw CustomException(ErrorCode.DUPLICATE, ErrorMessage.DUPLICATE_BUSINESS_NUMBER)
        }

        val restaurant = modelMapper.map(signUpRequest, Restaurant::class.java)

        restaurant.password = passwordEncoder.encode(signUpRequest.password)

        val saveRestaurant = restaurantRepository.save(restaurant)
        return modelMapper.map(saveRestaurant, RestaurantResponse::class.java)
    }

    @Transactional(readOnly = true)
    fun getRestaurant(id: Long): RestaurantResponse {
        val restaurant = findById(id)
        return modelMapper.map(restaurant, RestaurantResponse::class.java)
    }

    @Transactional
    fun updateRestaurant(id: Long, updateRequest: RestaurantUpdateRequest): RestaurantResponse {
        val restaurant = findById(id)

        if (!updateRequest.password.isNullOrEmpty()) {
            restaurant.password = passwordEncoder.encode(updateRequest.password)
        }

        val updatedRestaurant = modelMapper.map(updateRequest, Restaurant::class.java)
        updatedRestaurant.id = restaurant.id
        return modelMapper.map(restaurantRepository.save(updatedRestaurant), RestaurantResponse::class.java)
    }

    @Transactional(readOnly = true)
    fun getRestaurantsByLocation(sido: String, sigugun: String): List<RestaurantResponse> {
        return restaurantRepository.findByLocationSidoAndLocationSigugun(sido, sigugun)
            .map { restaurant -> modelMapper.map(restaurant, RestaurantResponse::class.java) }
    }


    @Transactional(readOnly = true)
    fun findById(id: Long): Restaurant {
        return restaurantRepository.findById(id)
            .orElseThrow { CustomException(ErrorCode.NOT_FOUND, ErrorMessage.RESTAURANT_NOT_FOUND) }
    }

    @Transactional
    fun updateApprovalStatus(id: Long, status: ApprovalStatus): RestaurantResponse {
        val restaurant = findById(id)
        restaurant.updateApprovalStatus(status)
        val saveRestaurant = restaurantRepository.save(restaurant)
        return modelMapper.map(saveRestaurant, RestaurantResponse::class.java)
    }

}
