package com.example.nutritnt.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.nutritnt.R
import com.example.nutritnt.database.entities.Encuesta

class EncuestaAdapter() : RecyclerView.Adapter<EncuestaViewHolder>(){

    private var encuestas = emptyList<Encuesta>() // Copia cache de las encuestas

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EncuestaViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return EncuestaViewHolder(layoutInflater.inflate(R.layout.item_encuesta, parent, false))
    }

    // Pasa por cada uno de los items y va a llamar al render pasandole ese item
    override fun onBindViewHolder(holder: EncuestaViewHolder, position: Int) {
        val item = encuestas[position]
        holder.render(item)
    }

    override fun getItemCount(): Int {
        return encuestas.size
    }

    // Actualizar la lista de encuestas y notificar al adapter de los cambios
    internal fun setEncuestas(encuestas: List<Encuesta>) {
        this.encuestas = encuestas
        notifyDataSetChanged()
    }

}