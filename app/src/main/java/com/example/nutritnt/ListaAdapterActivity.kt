package com.example.nutritnt

import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class ListaAdapterActivity(private var itemList: List<ListaElementoActivity>, private val context: Context) :
    RecyclerView.Adapter<ListaAdapterActivity.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.activity_lista_elemento, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindData(itemList[position])
    }

    override fun getItemCount(): Int {
        return itemList.size
    }

    fun setItems(items: List<ListaElementoActivity>) {
        itemList = items
        notifyDataSetChanged()
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val iconImage: ImageView = itemView.findViewById(R.id.iconImageView)
        private val nombre: TextView = itemView.findViewById(R.id.nombreTextView)
        private val fecha: TextView = itemView.findViewById(R.id.fechaTextView)
        private val estado: TextView = itemView.findViewById(R.id.estadoTextView)

        fun bindData(item: ListaElementoActivity) {
            nombre.text = item.nombre
            fecha.text = item.fecha
            estado.text = item.estado
        }
    }
}
