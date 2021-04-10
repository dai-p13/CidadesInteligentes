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
    @POST("myslim/api/users")
    fun login(
            @Field("username") username: String,
            @Field("password") password: String
    ): Call<Users>
}