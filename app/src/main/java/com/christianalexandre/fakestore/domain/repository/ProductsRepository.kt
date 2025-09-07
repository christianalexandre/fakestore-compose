package com.christianalexandre.fakestore.domain.repository

import androidx.paging.PagingData
import com.christianalexandre.fakestore.domain.model.Product
import kotlinx.coroutines.flow.Flow

interface ProductsRepository {
    fun getProducts(): Flow<PagingData<Product>>

    suspend fun getProductById(id: Int): Product
}
