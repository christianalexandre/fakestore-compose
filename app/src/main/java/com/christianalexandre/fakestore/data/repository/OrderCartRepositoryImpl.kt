package com.christianalexandre.fakestore.data.repository

import com.christianalexandre.fakestore.data.remote.backend.BackendApi
import com.christianalexandre.fakestore.data.remote.backend.dto.AddOrderItemRequestDto
import com.christianalexandre.fakestore.data.remote.backend.dto.OrderDto
import com.christianalexandre.fakestore.data.remote.backend.dto.UpdateOrderItemRequestDto
import com.christianalexandre.fakestore.domain.model.CartItem
import com.christianalexandre.fakestore.domain.repository.CartRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class OrderCartRepositoryImpl @Inject constructor(
    private val api: BackendApi
) : CartRepository {

    private val _items = MutableStateFlow<List<CartItem>>(emptyList())
    private var currentOrderId: Long? = null
    private val thumbnailCache = mutableMapOf<Int, String>()

    override fun getCartItems(): Flow<List<CartItem>> = flow {
        refreshCart()
        emitAll(_items)
    }

    override suspend fun addItemToCart(item: CartItem) {
        thumbnailCache[item.productId] = item.thumbnail
        val orderId = getOrCreateOrder()
        val existing = _items.value.find { it.productId == item.productId }
        val order = if (existing != null) {
            api.updateItem(orderId, item.productId.toLong(), UpdateOrderItemRequestDto(existing.quantity + item.quantity))
        } else {
            api.addItem(orderId, AddOrderItemRequestDto(item.productId.toLong(), item.quantity))
        }
        _items.value = order.toCartItems()
    }

    override suspend fun removeItemFromCart(productId: Int) {
        val orderId = currentOrderId ?: return
        val existing = _items.value.find { it.productId == productId } ?: return
        val order = if (existing.quantity > 1) {
            api.updateItem(orderId, productId.toLong(), UpdateOrderItemRequestDto(existing.quantity - 1))
        } else {
            api.removeItem(orderId, productId.toLong())
        }
        _items.value = order.toCartItems()
    }

    override suspend fun clearCart() {
        val orderId = currentOrderId ?: return
        api.cancelOrder(orderId)
        currentOrderId = null
        _items.value = emptyList()
    }

    override suspend fun checkout() {
        val orderId = currentOrderId ?: return
        api.checkout(orderId)
        currentOrderId = null
        _items.value = emptyList()
    }

    private suspend fun refreshCart() {
        try {
            val open = api.getOrders(status = "OPEN").firstOrNull()
            currentOrderId = open?.id
            _items.value = open?.toCartItems() ?: emptyList()
        } catch (e: Exception) {
            _items.value = emptyList()
        }
    }

    private suspend fun getOrCreateOrder(): Long {
        currentOrderId?.let { return it }
        val order = api.createOrder()
        currentOrderId = order.id
        return order.id
    }

    private fun OrderDto.toCartItems(): List<CartItem> = items.map { item ->
        CartItem(
            productId = item.productId.toInt(),
            title = item.productTitle,
            price = item.unitPrice,
            quantity = item.quantity,
            thumbnail = thumbnailCache[item.productId.toInt()] ?: ""
        )
    }
}
