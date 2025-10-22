package com.christianalexandre.fakestore.domain.use_case.add_items_to_cart

import com.christianalexandre.fakestore.domain.model.CartItem
import com.christianalexandre.fakestore.domain.repository.CartRepository
import javax.inject.Inject

class AddItemToCartUseCase @Inject constructor(
    private val cartRepository: CartRepository
) {
    suspend operator fun invoke(item: CartItem) {
        cartRepository.addItemToCart(item)
    }
}