package com.christianalexandre.fakestore.data.mappers

import com.christianalexandre.fakestore.data.remote.product.dto.ProductDto
import com.christianalexandre.fakestore.domain.model.Product

fun ProductDto.toProduct() =
    Product(
        availabilityStatus = availabilityStatus,
        brand = brand,
        category = category,
        description = description,
        dimensions = dimensions,
        discountPercentage = discountPercentage,
        id = id,
        images = images,
        meta = meta,
        minimumOrderQuantity = minimumOrderQuantity,
        price = price,
        rating = rating,
        returnPolicy = returnPolicy,
        reviews = reviews,
        shippingInformation = shippingInformation,
        sku = sku,
        stock = stock,
        tags = tags,
        thumbnail = thumbnail,
        title = title,
        warrantyInformation = warrantyInformation,
        weight = weight,
    )
