package com.christianalexandre.fakestore.data.repository

import com.christianalexandre.fakestore.data.local.SessionManager
import com.christianalexandre.fakestore.data.remote.auth.AuthApi
import com.christianalexandre.fakestore.data.remote.auth.dto.ConfirmRequestDto
import com.christianalexandre.fakestore.data.remote.auth.dto.LoginResponseDto
import com.christianalexandre.fakestore.data.remote.auth.dto.PhoneLoginRequestDto
import com.christianalexandre.fakestore.domain.repository.AuthRepository
import com.christianalexandre.fakestore.domain.repository.LoginResult
import com.google.gson.Gson
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor(
    private val api: AuthApi,
    private val sessionManager: SessionManager
) : AuthRepository {

    private val gson = Gson()

    override suspend fun login(phone: String, uuid: String): LoginResult {
        val response = api.login(PhoneLoginRequestDto(phone = phone, uuid = uuid))
        return if (response.code() == 200) {
            val loginResponse = gson.fromJson(response.body()!!.string(), LoginResponseDto::class.java)
            sessionManager.token = loginResponse.token
            sessionManager.userId = loginResponse.user.id
            LoginResult.LoggedIn(loginResponse.token)
        } else {
            LoginResult.OtpRequired
        }
    }

    override suspend fun confirm(phone: String, uuid: String, code: String): String {
        val response = api.confirm(ConfirmRequestDto(phone = phone, uuid = uuid, code = code))
        sessionManager.token = response.token
        sessionManager.userId = response.user.id
        return response.token
    }
}
