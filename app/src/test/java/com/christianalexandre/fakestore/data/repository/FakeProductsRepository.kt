package com.christianalexandre.fakestore.data.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.christianalexandre.fakestore.data.remote.product.FakeProductsPagingSource
import com.christianalexandre.fakestore.data.remote.product.dto.Dimensions
import com.christianalexandre.fakestore.data.remote.product.dto.Meta
import com.christianalexandre.fakestore.data.remote.product.dto.Review
import com.christianalexandre.fakestore.domain.model.Product
import com.christianalexandre.fakestore.domain.repository.ProductsRepository
import kotlinx.coroutines.flow.Flow

class FakeProductsRepository : ProductsRepository {
    private var products = emptyList<Product>()

    override fun getProducts(): Flow<PagingData<Product>> {
        return Pager(
            config = PagingConfig(pageSize = 20),
            pagingSourceFactory = { FakeProductsPagingSource(products) }
        ).flow
    }

    override suspend fun getProductById(id: Int): Product {
        return products.find { it.id == id } ?: throw Exception("Product not found")
    }

    fun setProducts(count: Int) {
        products = createFakeProductList(count)
    }
}

private fun createFakeProductList(count: Int): List<Product> {
    val products = mutableListOf<Product>()
    for (i in 1..count) {
        products.add(
            Product(
                id = i,
                title = "Product Title $i",
                description = "This is a detailed description for product $i.",
                category = "Category ${i % 3}", // Cria 3 categorias diferentes
                price = 19.99 * i,
                discountPercentage = 10.0 + i,
                rating = (i % 5).toDouble().coerceIn(1.0, 5.0), // Garante rating de 1 a 5
                stock = 5 * i,
                brand = if (i % 2 == 0) "BrandA" else "BrandB", // Alterna a marca
                sku = "SKU-00$i",
                weight = 100 * i,
                dimensions = Dimensions(width = 1.0 * i, height = 2.0 * i, depth = 0.5 * i),
                warrantyInformation = "$i year warranty",
                shippingInformation = "Ships in ${i % 3 + 1} days",
                availabilityStatus = if (i < 8) "In Stock" else "Low Stock",
                returnPolicy = "30-day return policy",
                minimumOrderQuantity = 1,
                meta = Meta(
                    createdAt = "2025-09-${(10 + i).toString().padStart(2, '0')}T10:00:00Z",
                    updatedAt = "2025-09-${(10 + i).toString().padStart(2, '0')}T18:30:00Z",
                    barcode = "8801234567${i.toString().padStart(3, '0')}",
                    qrCode = "https://my-app.com/p/?id=${i * 99}"
                ),
                images = listOf("https://example.com/image/$i/1.jpg", "https://example.com/image/$i/2.jpg"),
                thumbnail = "https://example.com/thumbnail/$i.jpg",
                tags = listOf("tag$i", "sale"),
                reviews = listOf(
                    Review(
                        rating = i % 5 + 1,
                        comment = "Good comment for product $i",
                        date = "2025-10-04T10:0${i}:00Z",
                        reviewerName = "Reviewer Name $i",
                        reviewerEmail = "reviewer$i@example.com"
                    ),
                    Review(
                        rating = (i % 4) + 1,
                        comment = "Another review for product $i",
                        date = "2025-10-05T11:1${i}:00Z",
                        reviewerName = "User No. ${i + 100}",
                        reviewerEmail = "user${i + 100}@example.com"
                    )
                )
            )
        )
    }
    return products
}
