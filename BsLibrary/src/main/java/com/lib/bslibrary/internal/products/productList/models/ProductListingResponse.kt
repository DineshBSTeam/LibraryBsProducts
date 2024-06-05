package com.lib.bslibrary.internal.products.productList.models
import com.google.gson.annotations.SerializedName

data class ProductListingResponse(
    @SerializedName("data") val data: ProductListings?,
    @SerializedName("code") val code: Int?,
    @SerializedName("message") val message: String?,
    @SerializedName("success") val success: Boolean?
)

data class ProductListings(
    @SerializedName("products") val products: List<ProductListingData>,
    @SerializedName("creditCardBenefits") val creditCardBenefits: List<CreditCardBenefits>
)

data class ProductListingData(
    @SerializedName("productId") val productId: Int,
    @SerializedName("productCatId") val productCatId: Int,
    @SerializedName("productTitle") val productTitle: String,
    @SerializedName("productSubTitle") val productSubTitle: String,
    @SerializedName("productLogo") val productLogo: String,
    @SerializedName("status") val status: ProductStatus,
    @SerializedName("joiningFee") val joiningFee: String?,
    @SerializedName("annualFee") val annualFee: String?,
    @SerializedName("interestRate") val interestRate: String?,
    @SerializedName("processingFee") val processingFee: String?,
    @SerializedName("uptoLoan") val uptoLoan: String?,
    @SerializedName("features") val features: List<String>,
    @SerializedName("benefits") val benefits: List<Int>,
    @SerializedName("shareData") val shareData: ShareDataModel,
    @SerializedName("earnUpto") val earnUpto: String?
)

data class CreditCardBenefits(
    @SerializedName("id") val id: Int,
    @SerializedName("title") val title: String
)

data class ProductStatus(
    @SerializedName("id") val id: Int,
    @SerializedName("isActive") val isActive: Int,
    @SerializedName("text") val text: String,
    @SerializedName("color") val color: String
)

data class ShareDataModel(
    @SerializedName("languageId") val languageId: Int,
    @SerializedName("locale") val locale: String,
    @SerializedName("language") val language: String,
    @SerializedName("shareContent") val shareContent: String?,
    @SerializedName("shareImage") val shareImage: String,
    @SerializedName("shareLink") val shareLink: String
)
