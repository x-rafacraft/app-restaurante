package com.example.appadminrestaurant_20083_v01.network.response

import com.example.appadminrestaurant_20083_v01.models.Categoria
import com.google.gson.annotations.SerializedName

data class CategoriaResponse(
    @SerializedName("codigo")
    val codigo: String,
    @SerializedName("mensaje")
    val mensaje: String,
    @SerializedName("datos")
    val datos: ArrayList<Categoria>

)
