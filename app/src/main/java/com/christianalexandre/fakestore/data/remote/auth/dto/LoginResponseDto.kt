package com.christianalexandre.fakestore.data.remote.auth.dto

data class LoginResponseDto(
    val token: String,
    val user: UserDto
)

data class UserDto(
    val id: Long,
    val name: String,
    val email: String?,
    val bio: String,
    val avatar: String
)
