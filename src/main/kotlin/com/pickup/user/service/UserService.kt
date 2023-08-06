package com.pickup.user.service

import com.pickup.exception.CustomException
import com.pickup.exception.ErrorCode
import com.pickup.exception.ErrorMessage
import com.pickup.user.dto.UserResponse
import com.pickup.user.dto.UserSignUpRequest
import com.pickup.user.dto.UserUpdateRequest
import com.pickup.user.entity.User
import com.pickup.user.repository.UserRepository
import org.modelmapper.ModelMapper
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class UserService(
    private val userRepository: UserRepository,
    private val modelMapper: ModelMapper,
    private val passwordEncoder: PasswordEncoder
) {

    @Transactional
    fun signUpUser(signUpRequest: UserSignUpRequest): UserResponse {

        // 이메일 중복 검사
        if (userRepository.existsByEmail(signUpRequest.email)) {
            throw CustomException(ErrorCode.DUPLICATE, ErrorMessage.DUPLICATE_EMAIL)
        }

        // 휴대폰번호 중복 검사
        if (userRepository.existsByPhoneNumber(signUpRequest.phoneNumber)) {
            throw CustomException(ErrorCode.DUPLICATE, ErrorMessage.DUPLICATE_PHONE_NUMBER)
        }

        val user = modelMapper.map(signUpRequest, User::class.java)

        user.password = passwordEncoder.encode(signUpRequest.password)

        val saveUser = userRepository.save(user)
        return modelMapper.map(saveUser, UserResponse::class.java)
    }

    @Transactional(readOnly = true)
    fun getUser(id: Long): UserResponse {
        val user = findById(id)
        return modelMapper.map(user, UserResponse::class.java)
    }

    @Transactional
    fun updateUser(userId: Long, updateRequest: UserUpdateRequest): UserResponse {
        val user = findById(userId)

        if (!updateRequest.password.isNullOrEmpty()) {
            user.password = passwordEncoder.encode(updateRequest.password)
        }

        val updateUser = modelMapper.map(updateRequest, User::class.java)
        updateUser.id = user.id
        return modelMapper.map(userRepository.save(updateUser), UserResponse::class.java)

    }

    @Transactional(readOnly = true)
    fun findById(userId: Long): User {
        return userRepository.findById(userId)
            .orElseThrow {
                CustomException(ErrorCode.NOT_FOUND, ErrorMessage.USER_NOT_FOUND)
            }
    }


}