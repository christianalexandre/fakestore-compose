package com.christianalexandre.fakestore.data.mappers

import com.christianalexandre.fakestore.data.local.cart.model.CartEntity
import com.christianalexandre.fakestore.domain.model.CartItem

fun CartEntity.toDomain(): CartItem {
    return CartItem(
        productId = productId,
        title = title,
        price = price,
        quantity = quantity,
        thumbnail = thumbnail
    )
}

fun CartItem.toEntity(): CartEntity {
    return CartEntity(
        productId = productId,
        title = title,
        price = price,
        quantity = quantity,
        thumbnail = thumbnail
    )
}