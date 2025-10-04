package com.christianalexandre.fakestore.domain.use_case.get_products

import com.christianalexandre.fakestore.data.repository.FakeProductsRepository
import org.junit.Before

class GetProductsTest {

    private lateinit var getProducts: GetProducts
    private lateinit var fakeProductsRepository: FakeProductsRepository

    @Before
    fun setup() {
        fakeProductsRepository = FakeProductsRepository()
        getProducts = GetProducts(fakeProductsRepository)
    }

}