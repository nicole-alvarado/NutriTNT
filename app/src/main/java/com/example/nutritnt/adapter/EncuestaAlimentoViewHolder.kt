package com.example.nutritnt.adapter

import android.util.Log
import android.view.View
import androidx.lifecycle.LifecycleOwner
import androidx.recyclerview.widget.RecyclerView
import com.example.nutritnt.R
import com.example.nutritnt.database.entities.EncuestaAlimento
import com.example.nutritnt.databinding.ItemEncuestaAlimentoBinding
import com.example.nutritnt.viewmodel.AlimentoViewModel

class EncuestaAlimentoViewHolder(view: View, private val alimentoViewModel: AlimentoViewModel, private val navigateToDetail: (Int, Int) -> Unit) : RecyclerView.ViewHolder(view) {
    val binding = ItemEncuestaAlimentoBinding.bind(view)

    // Esta función se va a llamar por cada item del listado de encuestas
    fun render(encuestaAlimentoModel: EncuestaAlimento) {
        // Observar el LiveData para obtener los datos del alimento asociado a la encuesta de alimento
        alimentoViewModel.fetchAlimentoByEncuestaAlimento(encuestaAlimentoModel.encuestaAlimentoId).observe(itemView.context as LifecycleOwner) { alimento ->
            // Verificar si el subgrupo es null o está vacío y actualizar la vista en consecuencia
            val codigoYSubgrupo = if (alimento.subgrupo.isNullOrEmpty()) {
                alimento.codigo
            } else {
                "${alimento.codigo} - ${alimento.subgrupo}"
            }

            // Actualizar la vista con los datos del alimento
            binding.tvCodigoAlimento.text = codigoYSubgrupo
            binding.tvDescripcionAlimento.text = alimento?.descripcion ?: "Descripción no disponible"
        }

        binding.tvEstado.text = encuestaAlimentoModel.estado

        if (encuestaAlimentoModel.estado == "NO COMPLETADA") {
            binding.tvEstado.setTextColor(itemView.context.getColor(R.color.red))
        } else {
            binding.tvEstado.setTextColor(itemView.context.getColor(R.color.green))
        }

        itemView.setOnClickListener {
            navigateToDetail(encuestaAlimentoModel.encuestaId, encuestaAlimentoModel.encuestaAlimentoId)
        }
    }
}
