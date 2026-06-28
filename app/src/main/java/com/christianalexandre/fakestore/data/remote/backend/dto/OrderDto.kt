package com.christianalexandre.fakestore.data.remote.backend.dto

data class OrderDto(
    val id: Long,
    val userId: Long,
    val status: String,
    val total: Double,
    val items: List<OrderItemDto>
)

data class OrderItemDto(
    val id: Long,
    val productId: Long,
    val productTitle: String,
    val unitPrice: Double,
    val quantity: Int,
    val subtotal: Double
)
