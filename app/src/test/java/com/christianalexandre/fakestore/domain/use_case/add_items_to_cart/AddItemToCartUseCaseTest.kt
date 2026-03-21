package com.christianalexandre.fakestore.domain.use_case.add_items_to_cart

import com.christianalexandre.fakestore.data.repository.FakeCartRepository
import com.christianalexandre.fakestore.domain.model.CartItem
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class AddItemToCartUseCaseTest {

    private lateinit var addItemToCartUseCase: AddItemToCartUseCase
    private lateinit var fakeCartRepository: FakeCartRepository

    @Before
    fun setup() {
        fakeCartRepository = FakeCartRepository()
        addItemToCartUseCase = AddItemToCartUseCase(fakeCartRepository)
    }

    @Test
    fun `addItemToCart should add item to repository`() = runBlocking {
        val cartItem = CartItem(
            productId = 1,
            title = "Product 1",
            price = 10.0,
            quantity = 1,
            thumbnail = ""
        )

        addItemToCartUseCase(cartItem)

        val items = fakeCartRepository.getCartItems().first()
        assertEquals(1, items.size)
        assertEquals(cartItem, items[0])
    }
}
