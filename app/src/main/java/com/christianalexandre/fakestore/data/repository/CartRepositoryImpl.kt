package com.christianalexandre.fakestore.data.repository

import com.christianalexandre.fakestore.data.local.cart.dao.CartItemDao
import com.christianalexandre.fakestore.data.local.cart.model.CartEntity
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
        val existingItemEntity = cartDao.getItemById(item.productId)
        val entityToSave = if (existingItemEntity != null) {
            item.toEntity().copy(quantity = existingItemEntity.quantity + item.quantity)
        } else {
            item.toEntity()
        }
        cartDao.insertOrUpdateItem(entityToSave)
    }

    override suspend fun removeItemFromCart(productId: Int) {
        val existingItemEntity = cartDao.getItemById(productId)
        existingItemEntity?.let {
            if (it.quantity > 1) {
                cartDao.insertOrUpdateItem(it.copy(quantity = it.quantity - 1))
                return
            }
        }
        cartDao.deleteItem(productId)
    }

    override suspend fun clearCart() {
        cartDao.clearCart()
    }
}