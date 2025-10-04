package com.christianalexandre.fakestore.domain.mappers

import com.christianalexandre.fakestore.domain.model.CartItem
import com.christianalexandre.fakestore.domain.model.Product

fun Product.toCartItem(): CartItem = CartItem(
    productId = id, title = title, price = price, quantity = 1, thumbnail = thumbnail
)