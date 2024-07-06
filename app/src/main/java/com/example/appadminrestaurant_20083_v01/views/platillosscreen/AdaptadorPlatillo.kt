package com.example.appadminrestaurant_20083_v01.views.platillosscreen

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.appadminrestaurant_20083_v01.R
import com.example.appadminrestaurant_20083_v01.models.Platillo


class AdaptadorPlatillo (
        val listaPlatillo: ArrayList<Platillo>,
        val onClick:  OnItemClicked
): RecyclerView.Adapter<AdaptadorPlatillo.ViewHolder>() {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        val vista = LayoutInflater.from(parent.context).inflate(R.layout.item_rv_platillo, parent, false)
        return ViewHolder(vista)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val platillo = listaPlatillo[position]

        holder.tvNomPlatillo.text = platillo.nombrePlatillo.uppercase()
        holder.tvCategoria.text = "Categoria: ${platillo.categoria.uppercase()}"

        if (platillo.descripcionPlatillo.isNullOrEmpty()){
            holder.tvDescripcion.visibility = View.GONE
        } else {
            holder.tvDescripcion.visibility = View.VISIBLE
            holder.tvDescripcion.text = "Descripcion: ${platillo.descripcionPlatillo}"
        }

        holder.precio.text = "S/ ${platillo.precio}"

        holder.ivEditar.setOnClickListener{
            onClick.editarPlatillo(platillo)
        }

        holder.ivBorrar.setOnClickListener {
            onClick.borrarPlatillo(platillo.nombrePlatillo)
        }
    }

    override fun getItemCount(): Int {
        return listaPlatillo.size
    }

    class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        val tvNomPlatillo = itemView.findViewById<TextView>(R.id.tvNombrePlatillo)
        val tvCategoria = itemView.findViewById<TextView>(R.id.tvCategoriaPlatillo)
        val tvDescripcion = itemView.findViewById<TextView>(R.id.tvDescripcionPlatillo)
        val precio = itemView.findViewById<TextView>(R.id.tvPrecio)
        val ivEditar = itemView.findViewById<ImageView>(R.id.ivEditar)
        val ivBorrar = itemView.findViewById<ImageView>(R.id.ivBorrar)
    }

    interface OnItemClicked{
        fun editarPlatillo(platillo: Platillo)
        fun borrarPlatillo(nomPlatillo: String)
    }
}