package com.correro.alejandro.tfg.data.api.models.citattionsmedicresponse

import com.google.gson.annotations.SerializedName

data class CitationMedicResponse(
    @SerializedName("status") val status: Int,
    @SerializedName("message") val message: String,
    @SerializedName("type") val type: Int,
    @SerializedName("data") val citationMedicUsed: ArrayList<CitationMedicUsed>
)