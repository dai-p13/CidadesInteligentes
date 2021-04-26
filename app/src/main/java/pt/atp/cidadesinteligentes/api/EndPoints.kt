package pt.atp.cidadesinteligentes.api

import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.*

interface EndPoints{
    @GET("/myslim/api/ocorrencias")
    fun getOcorrencias(): Call<List<Ocorrencia>>

    @GET("/myslim/api/users")
    fun getUsers(): Call<List<Users>>

    @GET("/myslim/api/ocorrencias/tipoAcid")
    fun getAcidentes(): Call<List<Ocorrencia>>

    @GET("/myslim/api/ocorrencias/tipoObras")
    fun getObras(): Call<List<Ocorrencia>>

    @GET("/myslim/api/ocorrencias/tipoSane")
    fun getSaneamento(): Call<List<Ocorrencia>>

    @Multipart
    @POST("/myslim/api/ocorrencias")
    fun addOcorrencias(@Part("title") title: RequestBody,
                       @Part("description") description: RequestBody,
                       @Part("lat") lat: RequestBody,
                       @Part("lon") lon: RequestBody,
                       @Part image: MultipartBody.Part,
                       //@Part("username") username: RequestBody,
                       @Part("tipo") tipo: RequestBody,
                       ): Call<OutputPost>
}