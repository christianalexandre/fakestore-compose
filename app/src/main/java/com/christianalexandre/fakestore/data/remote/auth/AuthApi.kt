package com.christianalexandre.fakestore.data.remote.auth

import com.christianalexandre.fakestore.data.remote.auth.dto.ConfirmRequestDto
import com.christianalexandre.fakestore.data.remote.auth.dto.LoginResponseDto
import com.christianalexandre.fakestore.data.remote.auth.dto.PhoneLoginRequestDto
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface AuthApi {
    @POST("users/login")
    suspend fun login(@Body request: PhoneLoginRequestDto): Response<ResponseBody>

    @POST("users/confirm")
    suspend fun confirm(@Body request: ConfirmRequestDto): LoginResponseDto
}
