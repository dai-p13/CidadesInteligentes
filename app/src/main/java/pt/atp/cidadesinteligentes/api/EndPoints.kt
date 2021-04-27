package pt.atp.cidadesinteligentes.api

import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.*

interface EndPoints{
    @GET("/myslim/api/ocorrencias")
    fun getOcorrencias(): Call<List<Ocorrencia>>

    @GET("/myslim/api/users/{id}/ocorrencias")
    fun getOcorrUser(@Path("id") id: Int): Call<List<Ocorrencia>>

    @GET("/myslim/api/users")
    fun getUsers(): Call<List<Users>>

    @FormUrlEncoded
    @POST("/myslim/api/editaOcorrencia")
    fun editaOcorrencia(@Field("titulo") first: String?, @Field("descricao") second: String?, @Field("id") third: Int?): Call<Ocorrencia>

    @FormUrlEncoded
    @POST("/myslim/api/eliminaOcorrencia")
    fun eliminaOcorrencia(@Field("id") first: Int?): Call<Ocorrencia>

    @GET("/myslim/api/ocorrencias/tipoAcid")
    fun getAcidentes(): Call<List<Ocorrencia>>

    @GET("/myslim/api/ocorrencias/tipoObras")
    fun getObras(): Call<List<Ocorrencia>>

    @GET("/myslim/api/ocorrencias/tipoSane")
    fun getSaneamento(): Call<List<Ocorrencia>>

    @Multipart
    @POST("/myslim/api/ocorrencias")
    fun addOcorrencias(@Part("titulo") titulo: RequestBody,
                       @Part("descricao") descricao: RequestBody,
                       @Part("latitude") latitude: RequestBody,
                       @Part("longitude") longitude: RequestBody,
                       @Part foto: MultipartBody.Part,
                       @Part("users_id") users_id: RequestBody,
                       @Part("tipo_id") tipo_id: RequestBody,
                       ): Call<Ocorrencia>

    @FormUrlEncoded
    @POST("myslim/api/users")
    fun login(
            @Field("username") username: String,
            @Field("password") password: String
    ): Call<Users>
}