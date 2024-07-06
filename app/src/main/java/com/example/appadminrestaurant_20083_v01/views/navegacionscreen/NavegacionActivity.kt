package com.example.appadminrestaurant_20083_v01.views.navegacionscreen

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.appadminrestaurant_20083_v01.R
import com.example.appadminrestaurant_20083_v01.databinding.ActivityNavegacionBinding
import com.example.appadminrestaurant_20083_v01.views.categoriasscreen.CategoriasActivity
import com.example.appadminrestaurant_20083_v01.views.platillosscreen.PlatillosActivity

class NavegacionActivity : AppCompatActivity() {

    private lateinit var bindig : ActivityNavegacionBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        bindig =  ActivityNavegacionBinding.inflate(layoutInflater)
        setContentView(bindig.root)

        supportActionBar?.hide()
        window.statusBarColor = resources.getColor(R.color.gris_oscuro)

        //Funcionalidad para la tarjeta categorias
        bindig.cvCategorias.setOnClickListener{
            var intent = Intent(this, CategoriasActivity::class.java)
            startActivity(intent)
        }

        //Funcionalidad para la tarjeta platillos
        bindig.cvPlatillos.setOnClickListener{
            var intent = Intent(this, PlatillosActivity::class.java)
            startActivity(intent)
        }

    }
}