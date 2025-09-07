package com.christianalexandre.fakestore.data.remote.product

import com.christianalexandre.fakestore.data.remote.product.dto.ProductDto
import com.christianalexandre.fakestore.data.remote.product.dto.ProductsPaginationDto
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

// https://dummyjson.com/docs/products#products-limit_skip

interface ProductsApi {
    @GET("/products")
    suspend fun getProducts(
        @Query("limit") limit: Int,
        @Query("skip") skip: Int
    ): ProductsPaginationDto

    @GET("/products/{id}")
    suspend fun getProductById(
        @Path("id") id: Int,
    ): ProductDto
}
