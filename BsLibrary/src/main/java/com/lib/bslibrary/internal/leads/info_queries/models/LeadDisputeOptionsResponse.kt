package com.lib.bslibrary.internal.leads.info_queries.models

data class LeadDisputeOptionsResponse(
    val statusCode: Int?,
    val message: String?,
    val success: Boolean?,
    val data: List<QueryType>?
)
class QueryType(query: String?) {
    var queryType: String? = query
    override fun toString(): String {
        return queryType!! // What to display in the Spinner list.
    }
}
