package com.christianalexandre.fakestore.domain.use_case.remove_item_from_cart

import com.christianalexandre.fakestore.data.repository.FakeCartRepository
import com.christianalexandre.fakestore.domain.model.CartItem
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class RemoveItemFromCartUseCaseTest {

    private lateinit var removeItemFromCartUseCase: RemoveItemFromCartUseCase
    private lateinit var fakeCartRepository: FakeCartRepository

    @Before
    fun setup() {
        fakeCartRepository = FakeCartRepository()
        removeItemFromCartUseCase = RemoveItemFromCartUseCase(fakeCartRepository)
    }

    @Test
    fun `removeItemFromCart should remove item from repository`() = runBlocking {
        val cartItem = CartItem(
            productId = 1,
            title = "Product 1",
            price = 10.0,
            quantity = 1,
            thumbnail = ""
        )
        fakeCartRepository.addItemToCart(cartItem)

        removeItemFromCartUseCase(cartItem)

        val items = fakeCartRepository.getCartItems().first()
        assertTrue(items.isEmpty())
    }
}
