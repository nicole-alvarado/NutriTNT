package com.example.nutritnt.adapter

import android.util.Log
import android.view.View
import androidx.lifecycle.LifecycleOwner
import androidx.recyclerview.widget.RecyclerView
import com.example.nutritnt.database.entities.EncuestaAlimento
import com.example.nutritnt.databinding.ItemEncuestaAlimentoBinding
import com.example.nutritnt.viewmodel.AlimentoViewModel


class EncuestaAlimentoViewHolder(view:View, private val alimentoViewModel: AlimentoViewModel, private val navigateToDetail: (Int, Int) -> Unit): RecyclerView.ViewHolder(view){
    val binding = ItemEncuestaAlimentoBinding.bind(view)

    // Esta función se va a llamar por cada item del listado de encuestas
     fun render(encuestaAlimentoModel: EncuestaAlimento){
        binding.tvEncuestaAlimento.text = encuestaAlimentoModel.encuestaAlimentoId.toString()

        // Observar el LiveData para obtener los datos del alimento asociado a la encuesta de alimento
        alimentoViewModel.fetchAlimentoByEncuestaAlimento(encuestaAlimentoModel.encuestaAlimentoId).observe(itemView.context as LifecycleOwner) { alimento ->
            // Actualizar la vista con los datos del alimento
            binding.tvDescripcionAlimento.text = alimento?.descripcion ?: "Descripción no disponible"
            Log.i("Alimentoooo", alimento?.descripcion ?: "Descripción no disponible")
        }
        binding.tvEstado.text = encuestaAlimentoModel.estado

        itemView.setOnClickListener{
            navigateToDetail(encuestaAlimentoModel.encuestaId,encuestaAlimentoModel.encuestaAlimentoId)
        }

    }
}