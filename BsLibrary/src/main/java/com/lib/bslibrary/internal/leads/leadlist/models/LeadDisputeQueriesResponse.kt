package com.lib.bslibrary.internal.leads.leadlist.models

data class LeadDisputeQueriesResponse(
    val statusCode: Int?,
    val message: String?,
    val success: Boolean?,
    val data: LeadQueryData?
)

data class DocMedia(
    val url: String?,
    val name: String?,
    val isDownloaded: Boolean?
)

data class Queries(
    val documentFile: List<DocMedia>?,
    val documentImage: List<DocMedia>?,
    val createdAt: String?,
    val document: Any?,
    val comment: String?,
    val adminId: Int?,
    val id: Int?,
    var shouldShowDate: Boolean = false
)

data class LeadQueryData(
    val queryType: String?,
    val status: Boolean?,
    val queries: List<Queries>?
)
