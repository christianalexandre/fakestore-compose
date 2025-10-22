package com.christianalexandre.fakestore.domain.use_case.remove_item_from_cart

import com.christianalexandre.fakestore.domain.model.CartItem
import com.christianalexandre.fakestore.domain.repository.CartRepository
import javax.inject.Inject

class RemoveItemFromCartUseCase @Inject constructor(
    private val cartRepository: CartRepository
) {
    suspend operator fun invoke(item: CartItem) {
        cartRepository.removeItemFromCart(item.productId)
    }
}