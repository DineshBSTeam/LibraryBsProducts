package com.lib.bslibrary.internal.leads.leadlist.models

data class LeadListingRequest(
    val agentMobile: String,
    val page: Int,
    val lead_status_parent_id: String?,
)
