package com.christianalexandre.fakestore.data.remote.product.dto

data class Review(
    val comment: String,
    val date: String,
    val rating: Int,
    val reviewerEmail: String,
    val reviewerName: String
)