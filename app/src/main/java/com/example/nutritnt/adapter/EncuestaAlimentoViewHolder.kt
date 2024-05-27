package com.example.nutritnt.adapter

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.example.nutritnt.database.entities.Encuesta_Alimento
import com.example.nutritnt.databinding.ItemEncuestaAlimentoBinding


class EncuestaAlimentoViewHolder(view:View): RecyclerView.ViewHolder(view){
    val binding = ItemEncuestaAlimentoBinding.bind(view)

    // Esta función se va a llamar por cada item del listado de encuestas
    fun render(encuestaAlimentoModel: Encuesta_Alimento){
        binding.tvEncuestaAlimento.text = encuestaAlimentoModel.encuestaAlimentoId.toString()
        binding.tvEncuesta.text = encuestaAlimentoModel.encuestaId.toString()
        binding.tvAlimento.text = encuestaAlimentoModel.alimentoId.toString()

    }
}