package com.christianalexandre.fakestore.data.remote.backend.dto

data class AddOrderItemRequestDto(val productId: Long, val quantity: Int)
data class UpdateOrderItemRequestDto(val quantity: Int)
