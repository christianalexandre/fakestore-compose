package com.christianalexandre.fakestore.domain.use_case.get_product

import com.christianalexandre.fakestore.data.repository.FakeProductsRepository
import com.christianalexandre.fakestore.domain.wrapper.Resource
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test

class GetProductTest {

    private lateinit var getProduct: GetProduct
    private lateinit var fakeProductsRepository: FakeProductsRepository

    @Before
    fun setup() {
        fakeProductsRepository = FakeProductsRepository()
        getProduct = GetProduct(fakeProductsRepository)
    }

    @Test
    fun `getProduct returns product when id exists`() = runBlocking {
        val qtyProducts = 30
        fakeProductsRepository.setProducts(qtyProducts)
        for (i in 1..qtyProducts) {
            val product = getProduct(i).toList()
            assert(product.first() is Resource.Loading)
            assert(product.last() is Resource.Success)
        }
    }

    @Test
    fun `getProduct returns error when id does not exist`() = runBlocking {
        val qtyProducts = 30
        fakeProductsRepository.setProducts(qtyProducts)
        val nonExistentId = qtyProducts + 1
        val result = getProduct(nonExistentId).toList()
        assert(result.first() is Resource.Loading)
        assert(result.last() is Resource.Error)
    }

}