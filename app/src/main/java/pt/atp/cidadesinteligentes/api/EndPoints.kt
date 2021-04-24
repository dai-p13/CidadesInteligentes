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

    @FormUrlEncoded
    @POST("/myslim/api/editaOcorrencia")
    fun editaOcorrencia(@Field("titulo") first: String?, @Field("descricao") second: String?, @Field("id") third: Int?): Call<Ocorrencia>

    @FormUrlEncoded
    @POST("/myslim/api/eliminaOcorrencia")
    fun eliminaOcorrencia(@Field("id") first: Int?): Call<Ocorrencia>
}