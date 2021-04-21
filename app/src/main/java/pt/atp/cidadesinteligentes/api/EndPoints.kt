package pt.atp.cidadesinteligentes.api

import retrofit2.Call
import retrofit2.http.*

interface EndPoints{
    @GET("/myslim/api/ocorrencias")
    fun getOcorrencias(): Call<List<Ocorrencia>>

    @GET("/myslim/api/users/{id}/ocorrencias")
    fun getOcorrUser(@Path("id") id: Int): Call<List<Ocorrencia>>

    @GET("/myslim/api/users")
    fun getUsers(): Call<List<Users>>

    @FormUrlEncoded
    @POST("myslim/api/users")
    fun login(
            @Field("username") username: String,
            @Field("password") password: String
    ): Call<Users>
}