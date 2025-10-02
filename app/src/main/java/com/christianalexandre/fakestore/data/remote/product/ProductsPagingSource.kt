package com.christianalexandre.fakestore.data.remote.product

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.christianalexandre.fakestore.data.mappers.toProduct
import com.christianalexandre.fakestore.domain.model.Product
import retrofit2.HttpException
import java.io.IOException

class ProductsPagingSource(
    private val api: ProductsApi,
) : PagingSource<Int, Product>() {
    companion object {
        private const val STARTING_SKIP_INDEX = 0
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Product> {
        val position = params.key ?: STARTING_SKIP_INDEX

        val limit = params.loadSize

        return try {
            val response = api.getProducts(limit = limit, skip = position)
            val products = response.products.map { it.toProduct() }

            LoadResult.Page(
                data = products,
                prevKey = if (position == STARTING_SKIP_INDEX) null else position - params.loadSize,
                nextKey = if (products.isEmpty()) null else position + products.size,
            )
        } catch (e: IOException) {
            LoadResult.Error(e)
        } catch (e: HttpException) {
            LoadResult.Error(e)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, Product>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            state.closestPageToPosition(anchorPosition)?.prevKey
                ?: state.closestPageToPosition(anchorPosition)?.nextKey
        }
    }
}
