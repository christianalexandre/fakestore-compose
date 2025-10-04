package com.christianalexandre.fakestore.domain.repository

import com.christianalexandre.fakestore.domain.model.CartItem
import kotlinx.coroutines.flow.Flow

interface CartRepository {
    fun getCartItems(): Flow<List<CartItem>>
    suspend fun addItemToCart(item: CartItem)
    suspend fun removeItemFromCart(productId: Int)
    suspend fun clearCart()
}