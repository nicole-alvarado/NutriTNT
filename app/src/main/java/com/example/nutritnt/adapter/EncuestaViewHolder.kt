package com.example.nutritnt.adapter

import android.graphics.Color
import android.util.Log
import android.view.View
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.nutritnt.R
import com.example.nutritnt.database.entities.Encuesta
import com.example.nutritnt.databinding.ItemEncuestaGeneralBinding

class EncuestaViewHolder(view: View, private val navigateToDetail: (Int) -> Unit) : RecyclerView.ViewHolder(view) {

    val binding = ItemEncuestaGeneralBinding.bind(view)

    // Esta función se va a llamar por cada item del listado de encuestas
    fun render(encuestaModel: Encuesta, isUltimaEncuesta: Boolean){
        binding.tvCodigoParticipante.text = encuestaModel.codigoParticipante
        binding.tvFecha.text = encuestaModel.fecha
        binding.tvZona.text = encuestaModel.zona
        binding.tvEstado.text = encuestaModel.estado

        val encuestaGeneralId = encuestaModel.encuestaId

        // Resaltar si es la última encuesta contestada
//        if (isUltimaEncuesta) {
//            binding.linearLayoutGrande.background = ContextCompat.getDrawable(itemView.context, R.drawable.border_ultima_encuesta)
////            itemView.setBackgroundColor(Color.YELLOW) // Puedes cambiar esto a cualquier estilo que desees
//        } else {
//            itemView.setBackgroundColor(Color.TRANSPARENT)
//        }

        if (encuestaModel.estado == "INICIADA") {
            binding.tvEstado.setTextColor(itemView.context.getColor(R.color.orange))
        } else if (encuestaModel.estado == "FINALIZADA"){
            binding.tvEstado.setTextColor(itemView.context.getColor(R.color.green))
        }

        itemView.setOnClickListener{
            navigateToDetail(encuestaGeneralId)
        }
    }
}