package com.example.appadminrestaurant_20083_v01.network

import com.example.appadminrestaurant_20083_v01.models.Platillo
import com.example.appadminrestaurant_20083_v01.network.response.CategoriaResponse
import com.example.appadminrestaurant_20083_v01.network.response.PlatilloResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface WebService {

    @GET("/categorias")
    suspend fun obtenerCategorias(): Response<CategoriaResponse>

    @FormUrlEncoded
    @POST("/categorias/add")
    suspend fun agregarCategoria(
        @Field("nom_categoria") nomCategoria: String
    ): Response<CategoriaResponse>

    @DELETE("/categorias/delete/{nomCategoria}")
    suspend fun borrarCategoria(
        @Path("nomCategoria") nomCategoria: String
    ): Response<CategoriaResponse>

    @GET("/platillos")
    suspend fun obtenerPlatillos(): Response<PlatilloResponse>

    @POST("/platillos/add")
    suspend fun agregarPlatillo(
        @Body platillo: Platillo
    ): Response<PlatilloResponse>

    @PUT("/platillos/update/{nomPlatillo}")
    suspend fun actualizarPlatillo(
        @Path("nomPlatillo") nomPlatillo: String,
        @Body platillo: Platillo
    ): Response<PlatilloResponse>

    @DELETE("/platillos/delete/{nomPlatillo}")
    suspend fun borrarPlatillo(
     @Path("nomPlatillo") nomPlatillo: String
    ): Response<PlatilloResponse>
}