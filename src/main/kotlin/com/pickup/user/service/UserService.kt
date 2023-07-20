package com.pickup.user.service

import com.pickup.exception.UserAlreadyExistsException
import com.pickup.user.dto.UserResponse
import com.pickup.user.dto.UserSignUpRequest
import com.pickup.user.dto.UserUpdateRequest
import com.pickup.user.entity.User
import com.pickup.user.repository.UserRepository
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class UserService(
    private val userRepository: UserRepository,
    private val passwordEncoder: PasswordEncoder
) {

    @Transactional
    fun signUpUser(userDto: UserSignUpRequest): UserResponse {

        // 이메일 중복 검사
        if (userRepository.existsByEmail(userDto.email)) {
            throw UserAlreadyExistsException("이미 존재하는 이메일입니다.")
        }

        // 휴대폰번호 중복 검사
        if (userRepository.existsByPhoneNumber(userDto.phoneNumber)) {
            throw UserAlreadyExistsException("이미 존재하는 휴대폰 번호입니다.")
        }

        val user = User.fromDto(userDto, passwordEncoder)
        // 사용자 등록
        userRepository.save(user)
        return UserResponse.fromUser(user)
    }

    @Transactional
    fun updateUser(userId: Long, updateDto: UserUpdateRequest): User {
        val user = getUser(userId)
        user.updateFromDto(updateDto, passwordEncoder)
        return user
    }

    @Transactional(readOnly = true)
    fun getUser(userId: Long): User {
        return userRepository.findById(userId)
            .orElseThrow { IllegalArgumentException("존재하지 않는 아이디 입니다.") }
    }
}