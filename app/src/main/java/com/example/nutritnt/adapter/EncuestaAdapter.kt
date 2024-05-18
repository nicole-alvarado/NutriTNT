package com.example.nutritnt.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.nutritnt.R
import com.example.nutritnt.models.Encuest

class EncuestaAdapter(private val encuestaList:List<Encuest>) : RecyclerView.Adapter<EncuestaViewHolder>(){


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EncuestaViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return EncuestaViewHolder(layoutInflater.inflate(R.layout.item_encuesta, parent, false))
    }

    // Pasa por cada uno de los items y va a llamar al render pasandole ese item
    override fun onBindViewHolder(holder: EncuestaViewHolder, position: Int) {
        val item = encuestaList[position]
        holder.render(item)
    }

    override fun getItemCount(): Int {
        return encuestaList.size
    }
}