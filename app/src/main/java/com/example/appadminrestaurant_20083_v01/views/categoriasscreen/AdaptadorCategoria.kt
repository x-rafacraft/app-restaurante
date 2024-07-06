package com.example.appadminrestaurant_20083_v01.views.categoriasscreen

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.appadminrestaurant_20083_v01.R
import com.example.appadminrestaurant_20083_v01.models.Categoria
import com.example.appadminrestaurant_20083_v01.utils.Constantes

class AdaptadorCategoria (
    val context: Context,
    val listaCategorias: ArrayList<Categoria>,
    val onClick: OnItemClicked
): RecyclerView.Adapter<AdaptadorCategoria.ViewHolder>() {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): AdaptadorCategoria.ViewHolder {
        val vista = LayoutInflater.from(parent.context).inflate(R.layout.item_rv_categoria, parent, false)
        return ViewHolder(vista)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
       val categoria = listaCategorias[position]

        Glide
            .with(context)
            .load( "${Constantes.PATH_IMG_CATEGORIAS}${categoria.imagenCategoria}")
            .centerInside()
            .placeholder(R.drawable.icon_falta_foto)
            .into(holder.ivCategoria)

        holder.tvNomCategoria.text = categoria.nomCategoria.uppercase()

        holder.ibtnBorrar.setOnClickListener{
            onClick.borrarCategoria(categoria.nomCategoria)
        }
    }

    override fun getItemCount(): Int {
        return listaCategorias.size
    }

    class ViewHolder (itemView: View): RecyclerView.ViewHolder(itemView) {
        val ivCategoria =  itemView.findViewById(R.id.ivCategoria) as ImageView
        val ibtnBorrar  =  itemView.findViewById(R.id.ibtnBorrar) as ImageButton
        val tvNomCategoria =  itemView.findViewById(R.id.tvNomCategoria) as TextView
    }

    interface OnItemClicked{
        fun borrarCategoria(nomCategoria: String)
    }

}
