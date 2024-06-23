package com.example.nutritnt.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.recyclerview.widget.RecyclerView
import com.example.nutritnt.R
import com.example.nutritnt.database.entities.Encuesta

class EncuestaAdapter(private val navigateToDetail: (Int) -> Unit) : RecyclerView.Adapter<EncuestaViewHolder>(),
    Filterable {
    private var encuestas = emptyList<Encuesta>() // Copia cache de las encuestas
    private var encuestasFiltradas = emptyList<Encuesta>()
    private var ultimaEncuestaId: Int? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EncuestaViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return EncuestaViewHolder(layoutInflater.inflate(R.layout.item_encuesta_general, parent, false), navigateToDetail)
    }

    override fun onBindViewHolder(holder: EncuestaViewHolder, position: Int) {
        val item = encuestasFiltradas[position]
        val isUltimaEncuesta = item.encuestaId == ultimaEncuestaId
        holder.render(item, isUltimaEncuesta)
    }

    override fun getItemCount(): Int {
        return encuestasFiltradas.size
    }

    internal fun setEncuestas(encuestas: List<Encuesta>) {
        this.encuestas = encuestas
        this.encuestasFiltradas = encuestas
        ultimaEncuestaId = encuestas.maxByOrNull { it.encuestaId }?.encuestaId
        notifyDataSetChanged()
    }

    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(constraint: CharSequence?): FilterResults {
                val query = constraint?.toString()?.lowercase()?.trim()
                val filteredList = if (query.isNullOrEmpty()) {
                    encuestas
                } else {
                    encuestas.filter {
                        it.codigoParticipante.lowercase().contains(query) ||
                                it.fecha.contains(query) ||
                                it.zona.lowercase().contains(query) ||
                                it.estado.lowercase().contains(query)
                    }
                }
                val filterResults = FilterResults()
                filterResults.values = filteredList
                return filterResults
            }

            override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
                encuestasFiltradas = results?.values as List<Encuesta>
                notifyDataSetChanged()
            }
        }
    }

    fun filterEncuestas(estado: String, zona: String) {
        val filteredList = encuestas.filter {
            (estado == "Todos" || it.estado == estado) &&
                    (zona == "Todos" || it.zona == zona)
        }
        encuestasFiltradas = filteredList
        notifyDataSetChanged()
    }
}
