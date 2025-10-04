package com.christianalexandre.fakestore.domain.model

data class CartItem(
    val productId: Int,
    val title: String,
    val price: Double,
    val quantity: Int,
    val thumbnail: String
)
