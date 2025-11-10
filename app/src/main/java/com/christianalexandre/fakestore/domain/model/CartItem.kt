package com.christianalexandre.fakestore.domain.model

data class CartItem(
    val productId: Int = -1,
    val title: String = "",
    val price: Double = 0.0,
    val quantity: Int = 0,
    val thumbnail: String = ""
)
