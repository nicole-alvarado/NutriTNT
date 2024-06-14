package com.example.nutritnt.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.nutritnt.R
import com.example.nutritnt.database.entities.EncuestaAlimento
import com.example.nutritnt.viewmodel.AlimentoViewModel

class EncuestaAlimentoAdapter(private val alimentoViewModel: AlimentoViewModel, private val navigateToDetail: (Int) -> Unit) : RecyclerView.Adapter<EncuestaAlimentoViewHolder>() {

    private var encuestas_alimentos = emptyList<EncuestaAlimento>() // Copia cache de las encuestas

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EncuestaAlimentoViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return EncuestaAlimentoViewHolder(layoutInflater.inflate(R.layout.item_encuesta_alimento, parent, false),alimentoViewModel,navigateToDetail)

    }

    override fun getItemCount(): Int {
        return encuestas_alimentos.size
    }

    override fun onBindViewHolder(holder: EncuestaAlimentoViewHolder, position: Int) {
        val item = encuestas_alimentos[position]
        holder.render(item)
    }

    // Actualizar la lista de encuestas_alimentos y notificar al adapter de los cambios
    internal fun setEncuestasAlimentos(encuestas_alimentos: List<EncuestaAlimento>) {
        this.encuestas_alimentos = encuestas_alimentos
        notifyDataSetChanged()
    }



}