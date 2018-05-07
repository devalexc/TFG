package com.correro.alejandro.tfg.data.api

import android.net.Uri
import com.correro.alejandro.tfg.data.api.models.LoginResponse
import com.correro.alejandro.tfg.data.api.models.adjuntotest.AdjuntoResponse
import com.correro.alejandro.tfg.data.api.models.chronicresponse.ChronicResponse
import com.correro.alejandro.tfg.data.api.models.citationresponse.CitationResponse
import com.correro.alejandro.tfg.data.api.models.citattionsmedicresponse.CitationMedicResponse
import com.correro.alejandro.tfg.data.api.models.historialresponse.HistoricalResponse
import com.correro.alejandro.tfg.data.api.models.medichoraryresponse.MedicHoraryResponse
import com.correro.alejandro.tfg.data.api.models.reciperesponse.RecipesResponse
import com.correro.alejandro.tfg.data.api.models.userresponse.UserResponse
import io.reactivex.Observable
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.http.*
import java.io.File


interface ApiService {
    @FormUrlEncoded
    @POST("oauth/v2/token")
    fun login(@Field("username") username: String, @Field("password") password: String, @Field("grant_type") type: String, @Field("client_id") clientid: String, @Field("client_secret") clientSecret: String): Observable<LoginResponse>

    @GET("api/patient/user")
    fun getUser(@Header("Authorization") token: String): Observable<UserResponse>

    @GET("api/patient/historical")
    fun getHistorical(@Header("Authorization") token: String): Observable<HistoricalResponse>

    @GET("api/patient/chronic")
    fun getChronics(@Header("Authorization") token: String): Observable<ChronicResponse>

    @GET("api/patient/recipeshistorical/{id}")
    fun getRecipesHistorical(@Header("Authorization") token: String, @Path("id") id: Int): Observable<RecipesResponse>

    @GET("api/patient/recipes")
    fun getRecipes(@Header("Authorization") token: String): Observable<RecipesResponse>

    @GET("api/patient/citations")
    fun getCitationsPatient(@Header("Authorization") token: String): Observable<CitationResponse>

    @GET("api/patient/medichorary")
    fun getHoraryMedic(@Header("Authorization") token: String): Observable<MedicHoraryResponse>

    @GET("api/patient/citationsmedic")
    fun getCitationsMedicUsed(@Header("Authorization") token: String): Observable<CitationMedicResponse>

    @Multipart
    @POST("api/patient/adjunto/new")
    fun postTest(@Header("Authorization") token: String, @Part file: MultipartBody.Part): Observable<AdjuntoResponse>
}

