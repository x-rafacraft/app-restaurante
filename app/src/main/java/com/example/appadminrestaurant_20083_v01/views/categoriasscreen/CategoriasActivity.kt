package com.example.appadminrestaurant_20083_v01.views.categoriasscreen

import android.app.Instrumentation.ActivityResult
import android.content.Intent
import android.graphics.drawable.ColorDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.GridLayoutManager
import com.example.appadminrestaurant_20083_v01.R
import com.example.appadminrestaurant_20083_v01.databinding.ActivityCategoriasBinding
import com.example.appadminrestaurant_20083_v01.models.Categoria
import com.example.appadminrestaurant_20083_v01.network.RetrofitClient
import es.dmoral.toasty.Toasty
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class CategoriasActivity : AppCompatActivity(), AdaptadorCategoria.OnItemClicked {

    private lateinit var binding :  ActivityCategoriasBinding
    lateinit var listaCategorias : ArrayList<Categoria>
    lateinit var adaptadorCategoria : AdaptadorCategoria



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCategoriasBinding.inflate(layoutInflater)
        setContentView(binding.root)


               window.statusBarColor =  resources.getColor(R.color.gris_oscuro)
               supportActionBar?.setDisplayHomeAsUpEnabled(true)

               supportActionBar?.setBackgroundDrawable(ColorDrawable(resources.getColor(R.color.valle_dorado)))
               supportActionBar?.title = "ADMINISTRAR CATEGORÍAS"


        obtenerCategorias()

        binding.fab.setOnClickListener {
            formularioAdd()
        }
    }

    private fun formularioAdd() {
        val builder = AlertDialog.Builder(this)
        val inflate = this.layoutInflater
        var vista = inflate.inflate(R.layout.alert_dialog_add_categoria, null)
        builder.setView(vista)

        var etNomCategoria = vista.findViewById(R.id.etNomCategoria) as EditText

        builder.setPositiveButton("ACEPTAR") { dialog, id ->
            if (!(etNomCategoria.text.toString().trim().isNullOrEmpty())){
                agregarCategoria(etNomCategoria.text.toString().trim())
            } else {
                Toasty.error(this@CategoriasActivity,
                    "Se deben de llenar los campos obligatorios",
                    Toasty.LENGTH_SHORT, true).show()
            }
        }

        builder.setNegativeButton("CANCELAR") { dialog, id ->

        }

        builder.create()
        builder.setCancelable(false)
        builder.show()

    }

    private fun agregarCategoria(nomCategoria: String){
        CoroutineScope(Dispatchers.IO).launch {
            val call =  RetrofitClient.webService.agregarCategoria(nomCategoria)
            runOnUiThread {
                if (call.isSuccessful) {
                    if (call.body()!!.codigo == "200") {
                        Toasty.success(this@CategoriasActivity, call.body()!!.mensaje,
                            Toasty.LENGTH_SHORT, true).show()
                        obtenerCategorias()
                    } else {
                        Toasty.error(this@CategoriasActivity, call.body()!!.mensaje,
                            Toasty.LENGTH_SHORT, true).show()
                    }
                } else {
                    Toasty.error(this@CategoriasActivity, "ERROR EN LA CONSULTA",
                        Toasty.LENGTH_SHORT, true).show()
                }
            }
        }
    }

    private fun obtenerCategorias() {

        CoroutineScope(Dispatchers.IO).launch {
            val call = RetrofitClient.webService.obtenerCategorias()
            runOnUiThread {
                if (call.isSuccessful) {
                    if (call.body()!!.codigo == "200") {
                        listaCategorias = call.body()!!.datos
                        setupRecyclerView()
                    } else {
                        Toasty.error(this@CategoriasActivity, call.body()!!.mensaje, Toasty.LENGTH_SHORT).show()
                    }
                } else {
                    Toasty.error(this@CategoriasActivity, "ERROR EN LA CONSULTA", Toasty.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun setupRecyclerView(){
        val layoutManager =  GridLayoutManager(this, 2)
        binding.rvCategorias.layoutManager = layoutManager
        adaptadorCategoria =  AdaptadorCategoria(this, listaCategorias, this)
        binding.rvCategorias.adapter = adaptadorCategoria
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home){
            finish()
        }

        return super.onOptionsItemSelected(item)
    }

    override fun borrarCategoria(nomCategoria: String) {
        CoroutineScope(Dispatchers.IO).launch {
            val call = RetrofitClient.webService.borrarCategoria(nomCategoria)
            runOnUiThread{
                if (call.isSuccessful){
                    if (call.body()!!.codigo == "200"){
                        Toasty.success(this@CategoriasActivity, call.body()!!.mensaje,
                            Toasty.LENGTH_SHORT, true).show()
                        obtenerCategorias()
                    } else {
                        Toasty.error(this@CategoriasActivity, call.body()!!.mensaje,
                            Toasty.LENGTH_SHORT, true).show()
                    }
                } else {
                    Toasty.error(this@CategoriasActivity, "ERROR EN LA CONSULTA",
                        Toasty.LENGTH_SHORT, true).show()
                }
            }
        }
    }
}