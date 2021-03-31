package pt.atp.cidadesinteligentes.api

import retrofit2.Call
import retrofit2.http.GET

interface EndPoints{
    @GET("/api/ocorrencias")
    fun getOcorrencias(): Call<List<Ocorrencia>>
}