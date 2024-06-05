package com.lib.bslibrary.internal.leads.leadlist.models

import com.google.gson.annotations.SerializedName

data class LeadListingResponse(
    val data: LeadListingData?,
    val code: Int?,
    val message: String?,
    val success: Boolean?
)

data class LeadListingData(
    val totalLeads: Int?,
    val inprocessLeads: Int?,
    val actionrequiredLeads: Int?,
    val rejectedLeads: Int?,
    val completedLeads: Int?,
    val leads: Leads?,
    val filterProducts: List<FilterProduct>?
)

data class FilterProduct(
    val categoryName: String?,
    val catId: Int?,
    val products: List<Product>?,
    @SerializedName("isSelected")
    val isSelected: Boolean?,
    @SerializedName("showCancelFor")
    val showCancelFor: Boolean?
)

data class TempFilterProduct(
    val categoryName: String?,
    val catId: Int?,
    val products: List<TempProduct>?,
    @SerializedName("isSelected")
    val isSelected: Boolean?,
    @SerializedName("showCancelFor")
    val showCancelFor: Boolean?,
    val index: Int?,
    @SerializedName("showSelectAll")
    val showSelectAll: Boolean?
)

data class TempProduct(
    val index: Int?,
    val id: Int?,
    val productName: String?,
    @SerializedName("isSelected")
    val isSelected: Boolean?
)

data class Product(
    val id: Int?,
    val productName: String?,
    @SerializedName("isSelected")
    val isSelected: Boolean?
)

data class Leads(
    val data: List<Lead>?,
    val current_page: Int?,
    val first_page_url: String?,
    val from: Int?,
    val last_page: Int?,
    val last_page_url: String?,
    val links: List<Link>?,
    val next_page_url: String?,
    val path: String?,
    val per_page: Int?,
    val prev_page_url: Any?,
    val to: Int?,
    val total: Int?
)

data class Link(
    val active: Boolean?,
    val label: String?,
    val url: String?
)

data class InfoButton(
    val createdDate: String?,
    val createdDateSubTitle: String?
)

data class Lead(
    val leadId: Int?,
    val productTitle: String?,
    val productLogo: String?,
    val leadRemark: String?,
    val leadStatus: String?,
    val leadStatusId: Int?,
    val leadStatusParentId: Int?,
    val customerName: String?,
    val mobileNo: Number?,
    val createdAt: String?,
    val updatedAt: String?,
    val lastUpdatedDate: String?,
    val lastUpdatedDateSubTitle: String?,
    val createdDateSubTitle: String?,
    val nextUpdate: String?,
    val nextUpdateSubTitle: String?,
    val sectionId: Int?,
    val expectedPayout: String?,
    val isQuery: Boolean = false,
    val isQueryActive: Boolean = false,
    val isDisputeVisible: Boolean = false
)
