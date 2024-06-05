package com.lib.bslibrary.internal.products.productList.models

data class ProductListingRequest(
    val categoryId: Int,
    val searchString: String
)