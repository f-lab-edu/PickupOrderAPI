package com.pickup.jwt.service

import com.pickup.restaurant.repository.RestaurantRepository
import com.pickup.user.repository.UserRepository
import com.pickup.jwt.dto.CustomUserDetails
import com.pickup.util.Role
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.stereotype.Service

@Service
class CustomUserDetailsService(
    private val userRepository: UserRepository,
    private val restaurantRepository: RestaurantRepository
) : UserDetailsService {

    override fun loadUserByUsername(email: String): UserDetails {
        val customer = userRepository.findByEmail(email)
        val restaurant = restaurantRepository.findByEmail(email)

        return when {
            customer != null -> CustomUserDetails(customer.email, customer.password, Role.USER)
            restaurant != null -> CustomUserDetails(restaurant.email, restaurant.password, Role.RESTAURANT)
            else -> throw UsernameNotFoundException(email)
        }
    }
}