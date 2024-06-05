import com.lib.bslibrary.internal.products.productList.models.ShareDataModel
import com.google.gson.annotations.SerializedName

data class ProductDetailResponse(
    @SerializedName("data") val data: ProductDetailData,
    @SerializedName("code") val code: Int?,
    @SerializedName("message") val message: String?,
    @SerializedName("success") val success: Boolean?
)

data class ProductDetailData(
    @SerializedName("productId") val productId: Int,
    @SerializedName("productTitle") val productTitle: String?,
    @SerializedName("productSubTitle") val productSubTitle: String?,
    @SerializedName("productLogo") val productLogo: String?,
    @SerializedName("bannerImage") val bannerImage: String?,
    @SerializedName("videoUrl") val videoUrl: String?,
    @SerializedName("shareData") val shareData: ShareDataModel,
    @SerializedName("tabs") val tabs: ProductDetailTabs,
    @SerializedName("status") val status: ProductDetailStatus,
    @SerializedName("earnUptoKey") val earnUptoKey: String?,
    @SerializedName("earnUptoValue") val earnUptoValue: String?
)

data class ProductDetailTabs(
    @SerializedName("details") val details: List<ProductDetailTabData>?,
    @SerializedName("marketing") val marketing: List<String>?
)

data class ProductDetailStatus(
    @SerializedName("id") val id: Int?,
    @SerializedName("text") val text: String?,
    @SerializedName("color") val color: String?,
    @SerializedName("enableShare") val enableShare: Boolean?
)

data class ProductDetailTabData(
    @SerializedName("tabName") val tabName: String,
    @SerializedName("content") val content: List<ProductDetailTabsContent>?
)

data class ProductDetailTabsContent(
    @SerializedName("title") val title: String?,
    @SerializedName("content") val content: String?,
    @SerializedName("video") val video: String?
)
