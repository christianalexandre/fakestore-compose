package com.christianalexandre.fakestore.domain.use_case.get_cart_items

import com.christianalexandre.fakestore.domain.model.CartItem
import com.christianalexandre.fakestore.domain.repository.CartRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetCartItemsUseCase @Inject constructor(
    private val cartRepository: CartRepository
) {
    operator fun invoke(): Flow<List<CartItem>> {
        return cartRepository.getCartItems()
    }
}