package com.christianalexandre.fakestore.data.remote.product

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.christianalexandre.fakestore.domain.model.Product

class FakeProductsPagingSource(
    private val products: List<Product>
) : PagingSource<Int, Product>() {
    override fun getRefreshKey(state: PagingState<Int, Product>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            state.closestPageToPosition(anchorPosition)?.prevKey
                ?: state.closestPageToPosition(anchorPosition)?.nextKey
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Product> {
        val currentPage = params.key ?: 0
        val startIndex = currentPage * params.loadSize
        val endIndex = (startIndex + 20).coerceAtMost(products.size)
        val data = products.subList(startIndex, endIndex)

        val nextKey = if (endIndex >= products.size) {
            null
        } else {
            currentPage + 1
        }

        return LoadResult.Page(
            data = data,
            prevKey = if (currentPage == 0) null else currentPage - 1,
            nextKey = nextKey
        )
    }
}