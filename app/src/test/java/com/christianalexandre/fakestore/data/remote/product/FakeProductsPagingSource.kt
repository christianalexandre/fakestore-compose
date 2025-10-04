package com.christianalexandre.fakestore.data.remote.product

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.christianalexandre.fakestore.domain.model.Product

class FakeProductsPagingSource(
    private val products: List<Product>
) : PagingSource<Int, Product>() {
    override fun getRefreshKey(state: PagingState<Int, Product>): Int? {
        return null
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Product> {
        return LoadResult.Page(
            data = products,
            prevKey = null,
            nextKey = null
        )
    }
}