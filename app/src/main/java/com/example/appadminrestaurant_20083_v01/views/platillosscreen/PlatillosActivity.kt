package com.example.appadminrestaurant_20083_v01.views.platillosscreen

import android.graphics.drawable.ColorDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.appadminrestaurant_20083_v01.R
import com.example.appadminrestaurant_20083_v01.databinding.ActivityPlatillosBinding
import com.example.appadminrestaurant_20083_v01.models.Platillo
import com.example.appadminrestaurant_20083_v01.network.RetrofitClient
import es.dmoral.toasty.Toasty
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class PlatillosActivity : AppCompatActivity(), AdaptadorPlatillo.OnItemClicked {

    private lateinit var binding: ActivityPlatillosBinding
    lateinit var listaPlatillos : ArrayList<Platillo>
    lateinit var adaptadorPlatillo: AdaptadorPlatillo
    var listaSpinnerCategorias = ArrayList<String>()
    var platillo = Platillo("", "", 0.0, "")
    var isEditando = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPlatillosBinding.inflate(layoutInflater)
        setContentView(binding.root)

        window.statusBarColor =  resources.getColor(R.color.gris_oscuro)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        supportActionBar?.setBackgroundDrawable(ColorDrawable(resources.getColor(R.color.valle_dorado)))
        supportActionBar?.title = "ADMINISTRAR PLATILLOS"

        obtenerPlatillos()
        obtenerListaCategorias()

        binding.fab.setOnClickListener{
            formAddUpdate()
        }
    }

    private fun obtenerListaCategorias(){
        CoroutineScope(Dispatchers.IO).launch {
            val call = RetrofitClient.webService.obtenerCategorias()
            runOnUiThread {
                if (call.isSuccessful){
                    if (call.body()!!.codigo == "200"){
                        listaSpinnerCategorias.add("* Selecciona una categoria")
                        for (categoria in call.body()!!.datos){
                           listaSpinnerCategorias.add(categoria.nomCategoria)
                        }
                    } else {
                        Toasty.error(this@PlatillosActivity,  call.body()!!.mensaje,
                            Toasty.LENGTH_SHORT, true).show()
                    }
                } else {
                    Toasty.error(this@PlatillosActivity, "ERROR EN LA CONSULTA",
                        Toasty.LENGTH_SHORT, true).show()
                }
            }
        }
    }

    private fun formAddUpdate(){
        val builder = AlertDialog.Builder(this)
        val inflater = this.layoutInflater
        var vista = inflater.inflate(R.layout.alert_dialog_add_update_platillo, null)
        builder.setView(vista)

        var tvAvisoAlert = vista.findViewById(R.id.tvTitulo) as TextView
        var etNomPlatillo = vista.findViewById(R.id.etNomPlatillo) as EditText
        var etDescripcion = vista.findViewById(R.id.etDescripcionPlatillo) as EditText
        var etPrecio =  vista.findViewById(R.id.etPrecio) as EditText
        var spiCategorias = vista.findViewById(R.id.spiCategorias) as Spinner

        var arrayAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, listaSpinnerCategorias)
        spiCategorias.adapter = arrayAdapter

        if (isEditando){
            tvAvisoAlert.setText("ACTUALIZAR PLATILLO")
            etNomPlatillo.setText(platillo.nombrePlatillo)
            etNomPlatillo.isEnabled = false
            etDescripcion.setText(platillo.descripcionPlatillo)
            etPrecio.setText(platillo.precio.toString())
            spiCategorias.setSelection(arrayAdapter.getPosition(platillo.categoria))
        }

        builder.setPositiveButton("ACEPTAR") {dialog, id ->
            if (validarCampos(etNomPlatillo.text.toString().trim(), spiCategorias.selectedItem.toString(), etPrecio.text.toString().trim())){
                platillo.nombrePlatillo = etNomPlatillo.text.toString().trim()
                platillo.descripcionPlatillo = etDescripcion.text.toString().trim()
                platillo.precio = etPrecio.text.toString().toDouble()
                platillo.categoria = spiCategorias.selectedItem.toString()

                if(isEditando){
                    actualizarPlatillo(platillo)
                } else {
                    agregarPlatillo(platillo)
                }

                isEditando = false
            } else {
                Toasty.error(this@PlatillosActivity, "Se deben llenar los campos obligatorios",
                    Toasty.LENGTH_SHORT, true).show()
            }
        }

        builder.setNegativeButton("CANCELAR") { dialog, id ->
            isEditando = false
        }

        builder.create()
        builder.setCancelable(false)
        builder.show()
    }

    private fun agregarPlatillo(platillo: Platillo) {
        CoroutineScope(Dispatchers.IO).launch {
            val call = RetrofitClient.webService.agregarPlatillo(platillo)
            runOnUiThread{
                if(call.isSuccessful){
                    if (call.body()!!.codigo == "200"){
                        Toasty.success(this@PlatillosActivity, call.body()!!.mensaje, Toasty.LENGTH_SHORT, true).show()
                        obtenerPlatillos()
                    } else {
                        Toasty.error(this@PlatillosActivity, call.body()!!.mensaje, Toasty.LENGTH_SHORT, true).show()
                    }
                } else {
                    Toasty.error(this@PlatillosActivity, "ERROR EN LA CONSULTA", Toasty.LENGTH_SHORT, true).show()
                }
            }
        }
    }


    private fun actualizarPlatillo(platillo: Platillo){
        CoroutineScope(Dispatchers.IO).launch {
            val call = RetrofitClient.webService.actualizarPlatillo(platillo.nombrePlatillo, platillo)
            runOnUiThread{
                if (call.isSuccessful){
                    if(call.body()!!.codigo == "200"){
                        Toasty.success(this@PlatillosActivity, call.body()!!.mensaje, Toasty.LENGTH_SHORT, true).show()
                        obtenerPlatillos()
                    } else {
                        Toasty.error(this@PlatillosActivity, call.body()!!.mensaje, Toasty.LENGTH_SHORT, true). show()
                    }
                } else {
                    Toasty.error(this@PlatillosActivity, "ERROR EN LA CONSULTA", Toasty.LENGTH_SHORT, true).show()
                }
            }
        }
    }

    private fun validarCampos(nomPlatillo: String, nomCategoria: String, precio: String): Boolean {
        if (nomPlatillo.isNullOrEmpty() || nomCategoria.contains("*") || precio.isNullOrEmpty()){
            return false
        } else {
            return true
        }
    }


    private fun  obtenerPlatillos(){
        CoroutineScope(Dispatchers.IO).launch {
            val call = RetrofitClient.webService.obtenerPlatillos()
            runOnUiThread{
                if (call.isSuccessful){
                    if (call.body()!!.codigo == "200"){
                        listaPlatillos = call.body()!!.datos
                        setupRecyclerView()
                    } else {
                        Toasty.error(
                            this@PlatillosActivity, call.body()!!.mensaje,
                            Toasty.LENGTH_SHORT, true).show()
                    }
                    } else {
                        Toasty.error(this@PlatillosActivity, "ERROR EN LA CONSULTA",
                            Toasty.LENGTH_SHORT, true).show()
                    }
                }
            }
        }


    private fun setupRecyclerView(){
        val layoutManager = LinearLayoutManager(this)
        binding.rvPlatillos.layoutManager =  layoutManager
        adaptadorPlatillo = AdaptadorPlatillo(listaPlatillos, this)
        binding.rvPlatillos.adapter = adaptadorPlatillo
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home){
            finish()
        }

        return super.onOptionsItemSelected(item)
    }

    override fun editarPlatillo(platillo: Platillo) {
        isEditando = true
        this.platillo = platillo
        formAddUpdate()
    }

    override fun borrarPlatillo(nomPlatillo: String) {
        CoroutineScope(Dispatchers.IO).launch {
            val call = RetrofitClient.webService.borrarPlatillo(nomPlatillo)
            runOnUiThread {
                if(call.isSuccessful){
                    if(call.body()!!.codigo == "200"){
                        Toasty.success(this@PlatillosActivity, call.body()!!.mensaje, Toasty.LENGTH_SHORT, true).show()
                        obtenerPlatillos()
                    } else {
                        Toasty.error(this@PlatillosActivity, call.body()!!.mensaje, Toasty.LENGTH_SHORT, true).show()
                    }
                } else {
                    Toasty.success(this@PlatillosActivity, "ERROR EN LA CONSULTA", Toasty.LENGTH_SHORT, true).show()
                }
            }
        }
    }

}