package com.example.nutritnt

import android.app.Activity
import android.app.Application
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.nutritnt.listado.EncuestaListAdapter
import com.example.nutritnt.viewmodel.EncuestaViewModel
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.nutritnt.database.entities.Encuesta
import com.google.android.material.floatingactionbutton.FloatingActionButton

class EncuestaPrueba : Fragment() {

    private lateinit var encuestaViewModel: EncuestaViewModel
    private lateinit var adapter: EncuestaListAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {


        val view = inflater.inflate(R.layout.fragment_encuesta_prueba, container, false)

        val recyclerView = view.findViewById<RecyclerView>(R.id.recyclerview)
        adapter = EncuestaListAdapter(requireContext())
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(requireContext())


        encuestaViewModel = ViewModelProvider(this, ViewModelProvider.AndroidViewModelFactory.getInstance(
            requireContext().applicationContext as Application
        )).get(EncuestaViewModel::class.java)

        encuestaViewModel.todasLasEncuestas.observe(viewLifecycleOwner, Observer { encuestas ->
            encuestas?.let { adapter.setEncuestas(it) }
        })

        val fab = view.findViewById<FloatingActionButton>(R.id.boton_flotante)
        fab.setOnClickListener {
            // Navegar hacia NuevaEncuestaPruebaFragment utilizando la acción definida en el gráfico de navegación
            findNavController().navigate(R.id.action_encuestaPrueba_to_nuevaEncuestaPruebaFragment)
        }

        return view
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        // No es necesario manejar onActivityResult cuando se utiliza la navegación
    }

//    companion object {
//        private const val nuevaEncuestaFragmentRequestCode = 1
//    }
}