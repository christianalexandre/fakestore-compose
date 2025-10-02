package com.christianalexandre.fakestore.data.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.christianalexandre.fakestore.data.mappers.toProduct
import com.christianalexandre.fakestore.data.remote.product.ProductsApi
import com.christianalexandre.fakestore.data.remote.product.ProductsPagingSource
import com.christianalexandre.fakestore.domain.exception.NetworkException
import com.christianalexandre.fakestore.domain.exception.UnknownException
import com.christianalexandre.fakestore.domain.model.Product
import com.christianalexandre.fakestore.domain.repository.ProductsRepository
import kotlinx.coroutines.flow.Flow
import retrofit2.HttpException
import javax.inject.Inject

class ProductsRepositoryImpl
    @Inject
    constructor(
        private val api: ProductsApi,
    ) : ProductsRepository {
        private suspend fun <T> execute(call: suspend () -> T): T =
            try {
                call()
            } catch (e: HttpException) {
                throw NetworkException()
            } catch (e: Exception) {
                throw UnknownException()
            }

        override fun getProducts(): Flow<PagingData<Product>> =
            Pager(
                config =
                    PagingConfig(
                        pageSize = 30,
                        enablePlaceholders = false,
                    ),
                pagingSourceFactory = { ProductsPagingSource(api) },
            ).flow

        override suspend fun getProductById(id: Int): Product =
            execute {
                api.getProductById(id).toProduct()
            }
    }
