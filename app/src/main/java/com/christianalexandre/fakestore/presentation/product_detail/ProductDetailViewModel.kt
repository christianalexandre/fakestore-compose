package com.christianalexandre.fakestore.presentation.product_detail

import androidx.lifecycle.ViewModel
import com.christianalexandre.fakestore.domain.use_case.get_product.GetProductUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ProductDetailViewModel
    @Inject constructor(
        getProductUseCase: GetProductUseCase
    ) : ViewModel() {

    }