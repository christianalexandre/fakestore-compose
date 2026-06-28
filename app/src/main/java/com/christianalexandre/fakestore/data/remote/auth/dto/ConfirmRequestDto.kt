package com.christianalexandre.fakestore.data.remote.auth.dto

data class ConfirmRequestDto(
    val phone: String,
    val uuid: String,
    val code: String
)
