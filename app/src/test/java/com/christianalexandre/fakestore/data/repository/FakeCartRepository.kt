package com.christianalexandre.fakestore.data.repository

import com.christianalexandre.fakestore.domain.model.CartItem
import com.christianalexandre.fakestore.domain.repository.CartRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class FakeCartRepository : CartRepository {

    private val _cartItems = MutableStateFlow<List<CartItem>>(emptyList())

    override fun getCartItems(): Flow<List<CartItem>> {
        return _cartItems.asStateFlow()
    }

    override suspend fun addItemToCart(item: CartItem) {
        _cartItems.update { items ->
            val existingItem = items.find { it.productId == item.productId }
            if (existingItem != null) {
                items.map {
                    if (it.productId == item.productId) {
                        it.copy(quantity = it.quantity + item.quantity)
                    } else it
                }
            } else {
                items + item
            }
        }
    }

    override suspend fun removeItemFromCart(productId: Int) {
        _cartItems.update { items ->
            items.filterNot { it.productId == productId }
        }
    }

    override suspend fun clearCart() {
        _cartItems.value = emptyList()
    }
}
