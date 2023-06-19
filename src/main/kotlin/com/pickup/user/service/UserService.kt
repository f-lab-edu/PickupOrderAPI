package com.pickup.user.service

import com.pickup.user.entity.User
import com.pickup.user.repository.UserRepository
import org.springframework.stereotype.Service
import javax.transaction.Transactional

@Service
class UserService(private val userRepository: UserRepository) {

    @Transactional
    fun registerUser(user: User): User {
        // 이메일 중복 검사
        userRepository.findByEmail(user.email).ifPresent {
            throw IllegalArgumentException("이미 존재하는 이메일입니다.")
        }

        // 휴대폰번호 중복 검사
        userRepository.findByPhoneNumber(user.phoneNumber).ifPresent {
            throw IllegalArgumentException("이미 존재하는 휴대폰 번호입니다.")
        }
        // 사용자 등록
        return userRepository.save(user)
    }
}