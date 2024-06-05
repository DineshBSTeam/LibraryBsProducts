import com.lib.bslibrary.internal.helper.RetrofitService
import com.lib.bslibrary.internal.leads.info_queries.models.LeadDisputeOptionsResponse
import com.lib.bslibrary.internal.leads.leadDetail.models.LeadDetailRequest
import com.lib.bslibrary.internal.leads.leadDetail.models.LeadDetailResponse
import com.lib.bslibrary.internal.leads.leadlist.models.*
import com.lib.bslibrary.internal.products.productDetail.models.ProductDetailRequest
import com.lib.bslibrary.internal.products.productList.models.ProductCategoriesResponse
import com.lib.bslibrary.internal.products.productList.models.ProductListingRequest
import com.lib.bslibrary.internal.products.productList.models.ProductListingResponse

class ApiRepository constructor(private val retrofitService: RetrofitService) {

    suspend fun getProductCategories(): NetworkState<ProductCategoriesResponse> {
        val response = retrofitService.getProductCategories()
        return if (response.isSuccessful) {
            val responseBody = response.body()
            if (responseBody != null) {
                NetworkState.Success(responseBody)
            } else {
                NetworkState.Error(response)
            }
        } else {
            NetworkState.Error(response)
        }
    }

    suspend fun getProductDetail(requestBody: ProductDetailRequest): NetworkState<ProductDetailResponse> {
        val response = retrofitService.getProductDetail(requestBody)
        return if (response.isSuccessful) {
            val responseBody = response.body()
            if (responseBody != null) {
                NetworkState.Success(responseBody)
            } else {
                NetworkState.Error(response)
            }
        } else {
            NetworkState.Error(response)
        }
    }

    suspend fun getProductListing(
        requestBody: ProductListingRequest
    ): NetworkState<ProductListingResponse> {
        val response = retrofitService.getProductListing(requestBody)
        return if (response.isSuccessful) {
            val responseBody = response.body()
            if (responseBody != null) {
                NetworkState.Success(responseBody)
            } else {
                NetworkState.Error(response)
            }
        } else {
            NetworkState.Error(response)
        }
    }

    suspend fun getLeadsListing(
        requestBody: LeadListingRequest
    ): NetworkState<LeadListingResponse> {
        val response = retrofitService.getLeadsListing(requestBody)
        return if (response.isSuccessful) {
            val responseBody = response.body()
            if (responseBody != null) {
                NetworkState.Success(responseBody)
            } else {
                NetworkState.Error(response)
            }
        } else {
            NetworkState.Error(response)
        }
    }

    suspend fun getLeadDetail(
        requestBody: LeadDetailRequest
    ): NetworkState<LeadDetailResponse> {
        val response = retrofitService.getLeadDetail(requestBody)
        return if (response.isSuccessful) {
            val responseBody = response.body()
            if (responseBody != null) {
                NetworkState.Success(responseBody)
            } else {
                NetworkState.Error(response)
            }
        } else {
            NetworkState.Error(response)
        }
    }

    suspend fun getLeadDisputeOptions(): NetworkState<LeadDisputeOptionsResponse> {
        val response = retrofitService.getLeadDisputeOptions()
        return if (response.isSuccessful) {
            val responseBody = response.body()
            if (responseBody != null) {
                NetworkState.Success(responseBody)
            } else {
                NetworkState.Error(response)
            }
        } else {
            NetworkState.Error(response)
        }
    }


    suspend fun getChatListData(
        requestBody: LeadDisputeQueriesRequest
    ): NetworkState<LeadDisputeQueriesResponse> {
        val response = retrofitService.getLeadDisputeQueries(requestBody)
        return if (response.isSuccessful) {
            val responseBody = response.body()
            if (responseBody != null) {
                NetworkState.Success(responseBody)
            } else {
                NetworkState.Error(response)
            }
        } else {
            NetworkState.Error(response)
        }
    }

    suspend fun submitQueryData(
        comment: String,
        queryType: String?,
        leadId: Int,
    ): NetworkState<LeadDisputeQueriesResponse> {
        val response = retrofitService.submitLeadDisputeQuery(comment, queryType, leadId, emptyList())
        return if (response.isSuccessful) {
            val responseBody = response.body()
            if (responseBody != null) {
                NetworkState.Success(responseBody)
            } else {
                NetworkState.Error(response)
            }
        } else {
            NetworkState.Error(response)
        }
    }
}
