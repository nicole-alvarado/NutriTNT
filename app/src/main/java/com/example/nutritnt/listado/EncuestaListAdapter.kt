package com.example.nutritnt.listado

import android.content.Context
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.nutritnt.database.entities.Encuesta
import android.widget.TextView
import com.example.nutritnt.R

class EncuestaListAdapter internal constructor(
    context: Context
) : RecyclerView.Adapter<EncuestaListAdapter.EncuestaViewHolder>() {

    private val inflater: LayoutInflater = LayoutInflater.from(context)
    private var encuestas = emptyList<Encuesta>() // Copia cache de los partidos

    inner class EncuestaViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val encuestaItemView: TextView = itemView.findViewById(R.id.textView_nombre)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EncuestaViewHolder {
        val itemView = inflater.inflate(R.layout.recyclerview_item, parent, false)
        return EncuestaViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: EncuestaViewHolder, position: Int) {
        val encuesta = encuestas[position]
        val text = "${encuesta.nombre} ${encuesta.fecha} - ${encuesta.estado}"
        holder.encuestaItemView.text = text
    }

    internal fun setEncuestas(encuestas: List<Encuesta>) {
        this.encuestas = encuestas
        notifyDataSetChanged()
    }

    override fun getItemCount() = encuestas.size
}