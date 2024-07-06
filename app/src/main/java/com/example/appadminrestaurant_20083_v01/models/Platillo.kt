package com.example.appadminrestaurant_20083_v01.models

import com.google.gson.annotations.SerializedName

data class Platillo(
    @SerializedName("nom_platillo")
    var nombrePlatillo: String,
    @SerializedName("descripcion_platillo")
    var descripcionPlatillo: String,
    @SerializedName("precio")
    var precio: Double,
    @SerializedName("nom_categoria")
    var categoria: String
)
