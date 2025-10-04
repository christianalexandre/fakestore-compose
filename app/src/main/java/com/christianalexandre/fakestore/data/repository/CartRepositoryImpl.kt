package com.christianalexandre.fakestore.data.repository

import com.christianalexandre.fakestore.data.local.cart.dao.CartItemDao
import com.christianalexandre.fakestore.data.mappers.toDomain
import com.christianalexandre.fakestore.data.mappers.toEntity
import com.christianalexandre.fakestore.domain.model.CartItem
import com.christianalexandre.fakestore.domain.repository.CartRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class CartRepositoryImpl @Inject constructor(
    private val cartDao: CartItemDao
) : CartRepository {

    override fun getCartItems(): Flow<List<CartItem>> {
        return cartDao.getCartItems().map { entities ->
            entities.map { it.toDomain() }
        }
    }

    override suspend fun addItemToCart(item: CartItem) {
        cartDao.insertOrUpdateItem(item.toEntity())
    }

    override suspend fun removeItemFromCart(productId: Int) {
        cartDao.deleteItem(productId)
    }

    override suspend fun clearCart() {
        cartDao.clearCart()
    }
}