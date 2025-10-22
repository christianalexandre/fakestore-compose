package com.christianalexandre.fakestore.presentation.products_list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.christianalexandre.fakestore.domain.model.Product
import com.christianalexandre.fakestore.domain.use_case.get_products.GetProducts
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

@HiltViewModel
class ProductsListViewModel
    @Inject
    constructor(
        getProducts: GetProducts,
    ) : ViewModel() {
        val productsFlow: Flow<PagingData<Product>> =
            getProducts()
                .cachedIn(viewModelScope)
    }
