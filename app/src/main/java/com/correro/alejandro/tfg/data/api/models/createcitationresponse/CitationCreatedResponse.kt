package com.correro.alejandro.tfg.data.api.models.createcitationresponse

import com.google.gson.annotations.SerializedName

data class CitationCreatedResponse(
    @SerializedName("status") val status: Int,
    @SerializedName("message") val message: String,
    @SerializedName("type") val type: Int,
    @SerializedName("econsultInfo") val data: String
)