package com.example.nutritnt.adapter

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.nutritnt.R
import com.example.nutritnt.models.Encuest

class EncuestaViewHolder(view: View) : RecyclerView.ViewHolder(view) {

    val nombreEncuesta = view.findViewById<TextView>(R.id.tvNombre)
    val fechaEncuesta = view.findViewById<TextView>(R.id.tvFecha)
    val estadoEncuesta = view.findViewById<TextView>(R.id.tvEstado)
    val imagenEncuesta = view.findViewById<ImageView>(R.id.iconImageView)

    // Esta función se va a llamar por cada item del listado de encuestas
    fun render(encuestaModel: Encuest){
        nombreEncuesta.text = encuestaModel.nombre
        fechaEncuesta.text = encuestaModel.fecha
        estadoEncuesta.text = encuestaModel.estado

    }
}