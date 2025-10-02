package com.christianalexandre.fakestore.data.remote.product.dto

data class ProductsPaginationDto(
    val products: List<ProductDto>,
    val total: Int,
    val skip: Int,
    val limit: Int,
)
