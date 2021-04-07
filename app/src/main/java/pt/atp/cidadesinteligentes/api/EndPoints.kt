package pt.atp.cidadesinteligentes.api

import retrofit2.Call
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.POST

interface EndPoints{
    @GET("/myslim/api/ocorrencias")
    fun getOcorrencias(): Call<List<Ocorrencia>>

    @GET("/myslim/api/users")
    fun getUsers(): Call<List<Users>>

    @GET("/myslim/api/tipo")
    fun getTipos(): Call<List<Tipo>>

    @GET("/myslim/api/ocorrencias/tipoAcid")
    fun getAcidentes(): Call<List<Ocorrencia>>

    @GET("/myslim/api/ocorrencias/tipoObras")
    fun getObras(): Call<List<Ocorrencia>>

    @GET("/myslim/api/ocorrencias/tipoSane")
    fun getSaneamento(): Call<List<Ocorrencia>>

    @FormUrlEncoded
    @POST("/myslim/api/ocorrencias")
    fun addOcorrencias(@Field("titulo") first: String?, @Field("descricao") second: String?,
                       @Field("latitude") third: String?, @Field("longitude") fourth: String?,
                       @Field("foto") fifth: String?, @Field("users_id") sixth: Int?,
                       @Field("tipo_id") seventh: Int?): Call<OutputPost>
}