package com.christianalexandre.fakestore.presentation.products_list

import com.christianalexandre.fakestore.domain.model.Product

data class ProductsListState(
    val isLoading: Boolean = false,
    val products: List<Product> = emptyList(),
    val error: String = "",
)
