package com.example.nutritnt.listado

import android.app.Application
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.nutritnt.adapter.EncuestaAdapter
import com.example.nutritnt.databinding.FragmentListadoEncuestasGeneralesBinding
import com.example.nutritnt.viewmodel.EncuestaViewModel

class ListadoEncuestasGeneralesFragment : Fragment() {
    private lateinit var binding: FragmentListadoEncuestasGeneralesBinding
    private lateinit var encuestaViewModel: EncuestaViewModel
    private lateinit var adapter: EncuestaAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        // Inflar el diseño del fragmento utilizando el FragmentWelcomeBinding
        binding = FragmentListadoEncuestasGeneralesBinding.inflate(inflater, container, false)
        val view = binding.root

        ViewCompat.setOnApplyWindowInsetsListener(view) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        return view
    }

    // Función que se llama cuando la vista ha sido creada
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // Inicializar el RecyclerView
        initRecyclerView()

        // Inicializar el ViewModel
        encuestaViewModel = ViewModelProvider(this, ViewModelProvider.AndroidViewModelFactory.getInstance(
            requireContext().applicationContext as Application
        )).get(EncuestaViewModel::class.java)

        // Observar los cambios en los datos de encuestas y actualizar el adaptador cuando los datos cambien
        encuestaViewModel.todasLasEncuestas.observe(viewLifecycleOwner, Observer { encuestas ->
            encuestas?.let { adapter.setEncuestas(it) }
        })

    }

    private fun initRecyclerView() {
        // Obtener la referencia al RecyclerView desde el binding
        val recyclerView = binding.recyclerViewEncuesta
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        // Inicializar el adaptador
        adapter = EncuestaAdapter { encuestaId ->
            val action = ListadoEncuestasGeneralesFragmentDirections.actionListEncuestasFragmentToListEncuestasAlimentosFragment(encuestaId)
            findNavController().navigate(action)
        }
        recyclerView.adapter = adapter


    }

}