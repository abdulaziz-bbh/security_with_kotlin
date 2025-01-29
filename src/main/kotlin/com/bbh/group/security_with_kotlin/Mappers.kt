package com.bbh.group.security_with_kotlin

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.stereotype.Component

@Component
class UserMapper(
    private val passwordEncoder: BCryptPasswordEncoder
){
    fun toEntity(request: UserCreateRequest): User {
        return User(
            email = request.email,
            password = passwordEncoder.encode(request.password),
            firstName = request.firstName,
            lastName = request.lastName
        )
    }
    fun toDto(user: User): UserResponse {
        return UserResponse(
            id = user.id!!,
            email = user.email,
            firstName = user.firstName,
            lastName = user.lastName
        )
    }
}