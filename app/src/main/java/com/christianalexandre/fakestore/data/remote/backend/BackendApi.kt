package com.christianalexandre.fakestore.data.remote.backend

import com.christianalexandre.fakestore.data.remote.backend.dto.AddOrderItemRequestDto
import com.christianalexandre.fakestore.data.remote.backend.dto.OrderDto
import com.christianalexandre.fakestore.data.remote.backend.dto.UpdateOrderItemRequestDto
import com.christianalexandre.fakestore.data.remote.backend.dto.UserProfileDto
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface BackendApi {
    @GET("users/{id}")
    suspend fun getUser(@Path("id") userId: Long): UserProfileDto

    @GET("orders")
    suspend fun getOrders(@Query("status") status: String? = null): List<OrderDto>

    @POST("orders")
    suspend fun createOrder(): OrderDto

    @POST("orders/{id}/items")
    suspend fun addItem(
        @Path("id") orderId: Long,
        @Body request: AddOrderItemRequestDto
    ): OrderDto

    @PATCH("orders/{id}/items/{productId}")
    suspend fun updateItem(
        @Path("id") orderId: Long,
        @Path("productId") productId: Long,
        @Body request: UpdateOrderItemRequestDto
    ): OrderDto

    @DELETE("orders/{id}/items/{productId}")
    suspend fun removeItem(
        @Path("id") orderId: Long,
        @Path("productId") productId: Long
    ): OrderDto

    @POST("orders/{id}/checkout")
    suspend fun checkout(@Path("id") orderId: Long): OrderDto

    @DELETE("orders/{id}")
    suspend fun cancelOrder(@Path("id") orderId: Long)
}
