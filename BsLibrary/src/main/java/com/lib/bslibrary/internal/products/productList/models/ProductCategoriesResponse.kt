package com.lib.bslibrary.internal.products.productList.models

import com.google.gson.annotations.SerializedName

data class ProductCategoriesResponse(
    @SerializedName("data") val data: List<ProductCategoriesData>,
    @SerializedName("code") val code: Int?,
    @SerializedName("message") val message: String?,
    @SerializedName("success") val success: Boolean?
)

data class ProductCategoriesData(
    @SerializedName("categoryName") val categoryName: String,
    @SerializedName("categoryId") val categoryId: Int,
    @SerializedName("categoryIcon") val categoryIcon: String,
    @SerializedName("earnUpto") val earnUpto: String?,
    var isSelected: Boolean? = false
)
