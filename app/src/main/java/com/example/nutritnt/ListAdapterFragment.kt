package com.example.nutritnt

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView

class ListAdapterFragment(private var itemList: List<ListaElementoActivity>, private val fragment: Fragment) :
    RecyclerView.Adapter<ListAdapterFragment.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(fragment.requireContext()).inflate(R.layout.activity_lista_elemento, parent, false)
        Log.i("enListAdapterFragment","ENTRAMOOOS")

        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindData(itemList[position])
    }

    override fun getItemCount(): Int {
        return itemList.size
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        init {
            itemView.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    itemClickListener?.onItemClick(itemList[position].nombre, itemList[position].fecha, itemList[position].estado)
                }
            }
        }

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

    interface OnItemClickListener {
        fun onItemClick(nombre: String, fecha: String, estado: String)
    }

    private var itemClickListener: OnItemClickListener? = null

    fun setOnItemClickListener(listener: OnItemClickListener) {
        itemClickListener = listener
    }
}
