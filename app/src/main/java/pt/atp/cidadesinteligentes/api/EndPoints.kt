package pt.atp.cidadesinteligentes.api

import retrofit2.Call
import retrofit2.http.GET

interface EndPoints{
    @GET("/myslim/api/ocorrencias")
    fun getOcorrencias(): Call<List<Ocorrencia>>

    @GET("/myslim/api/users")
    fun getUsers(): Call<List<Users>>
}