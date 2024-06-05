package com.lib.bslibrary.internal.leads.leadDetail.models

import com.google.gson.annotations.SerializedName

data class LeadDetailResponse(
    val code: Int?,
    val message: String?,
    val success: Boolean?,
    val data: LeadDetailData?
)

data class LeadDetailData(
    val productId: Int?,
    val productLogo: String?,
    val productStatus: String?,
    val productTitle: String?,
    @SerializedName("costomer_name")
    val customerName: String?,
    @SerializedName("created_at")
    val createdAt: String?,
    val leadId: Int?,
    val leadRemark: String?,
    val leadStatus: String?,
    val leadStatusId: Int?,
    val leadTitle: String?,
    @SerializedName("mobile_no")
    val mobileNo: Number?,
    @SerializedName("updated_at")
    val updatedAt: String?,
    val journey: List<JourneyObj>?,
    val isQuery: Boolean?,
    val isQueryActive: Boolean?,
    val isDisputeVisible: Boolean?
)

data class JourneyObj(
    @SerializedName("lead_status_title")
    val leadStatusTitle: String?,
    @SerializedName("lead_sub_status")
    val leadSubStatus: String?,
    @SerializedName("lead_sub_remark")
    val leadSubRemark: String?,
    @SerializedName("updated_at")
    val updatedAt: String?
)
