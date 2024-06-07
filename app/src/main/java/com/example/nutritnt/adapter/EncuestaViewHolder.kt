package com.example.nutritnt.adapter

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.example.nutritnt.database.entities.Encuesta
import com.example.nutritnt.databinding.ItemEncuestaGeneralBinding

class EncuestaViewHolder(view: View, private val navigateToDetail: (Int) -> Unit) : RecyclerView.ViewHolder(view) {

    val binding = ItemEncuestaGeneralBinding.bind(view)

    // Esta función se va a llamar por cada item del listado de encuestas
    fun render(encuestaModel: Encuesta){
        binding.tvNombre.text = encuestaModel.nombre
        binding.tvFecha.text = encuestaModel.fecha
        binding.tvZona.text = encuestaModel.zona
        binding.tvEstado.text = encuestaModel.estado

        val encuestaGeneralId = encuestaModel.encuestaId

        itemView.setOnClickListener{
            navigateToDetail(encuestaGeneralId)
        }
    }
}