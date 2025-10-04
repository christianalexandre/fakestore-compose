package com.christianalexandre.fakestore.domain.use_case.get_products

import androidx.paging.testing.asSnapshot
import com.christianalexandre.fakestore.data.repository.FakeProductsRepository
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

class GetProductsTest {

    private lateinit var getProducts: GetProducts
    private lateinit var fakeProductsRepository: FakeProductsRepository

    @Before
    fun setup() {
        fakeProductsRepository = FakeProductsRepository()
        getProducts = GetProducts(fakeProductsRepository)
    }

    @Test
    fun `getProducts should return a flow of PagingData`() = runTest {
        val qtyProducts = 100
        fakeProductsRepository.setProducts(qtyProducts)
        val products = getProducts().asSnapshot {
            scrollTo(100)
        }
        assert(products.size == qtyProducts)
    }
}