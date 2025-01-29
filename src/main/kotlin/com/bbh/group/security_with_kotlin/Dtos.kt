package com.bbh.group.security_with_kotlin

import jakarta.validation.constraints.NotNull
data class BaseErrorMessage(
    val errorCode: Int,
    val errorMessage: String?
)
data class UserCreateRequest(
    @field:NotNull val firstName: String,
    @field:NotNull val lastName: String,
    @field:NotNull val email: String,
    @field:NotNull val password: String,
    @field:NotNull val confirmPassword: String
)
data class UserUpdateRequest(
    val firstName: String,
    val lastName: String,
    val email: String,
    val password: String,
)
data class UserResponse(
    val id: Long,
    val email: String,
    val firstName: String,
    val lastName: String
)