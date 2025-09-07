package com.christianalexandre.fakestore.domain.use_case.get_product

import com.christianalexandre.fakestore.domain.exception.DomainException
import com.christianalexandre.fakestore.domain.exception.UnknownException
import com.christianalexandre.fakestore.domain.model.Product
import com.christianalexandre.fakestore.domain.repository.ProductsRepository
import com.christianalexandre.fakestore.domain.wrapper.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class GetProductUseCase
    @Inject
    constructor(
        private val repository: ProductsRepository,
    ) {
        suspend operator fun invoke(id: Int): Flow<Resource<Product>> =
            flow {
                try {
                    emit(Resource.Loading)
                    val product = repository.getProductById(id)
                    emit(Resource.Success(product))
                } catch (e: DomainException) {
                    emit(Resource.Error(e))
                } catch (e: Exception) {
                    emit(Resource.Error(UnknownException()))
                }
            }
    }
