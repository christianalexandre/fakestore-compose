package com.christianalexandre.fakestore.domain.use_case.get_products

import com.christianalexandre.fakestore.domain.repository.ProductsRepository
import javax.inject.Inject

class GetProducts
    @Inject
    constructor(
        private val repository: ProductsRepository,
    ) {
        operator fun invoke() = repository.getProducts()
    }
