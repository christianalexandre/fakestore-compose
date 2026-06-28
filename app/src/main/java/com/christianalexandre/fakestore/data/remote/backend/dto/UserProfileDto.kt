package com.christianalexandre.fakestore.data.remote.backend.dto

data class UserProfileDto(
    val id: Long,
    val email: String?,
    val name: String,
    val bio: String,
    val avatar: String
)
