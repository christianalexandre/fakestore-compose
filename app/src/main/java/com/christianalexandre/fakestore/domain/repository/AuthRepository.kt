package com.christianalexandre.fakestore.domain.repository

sealed interface LoginResult {
    data class LoggedIn(val token: String) : LoginResult
    data object OtpRequired : LoginResult
}

interface AuthRepository {
    suspend fun login(phone: String, uuid: String): LoginResult
    suspend fun confirm(phone: String, uuid: String, code: String): String
}
