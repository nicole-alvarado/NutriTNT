package com.example.nutritnt.listado

import android.app.Application
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.nutritnt.adapter.EncuestaAdapter
import com.example.nutritnt.database.entities.Encuesta
import com.example.nutritnt.databinding.FragmentListadoEncuestasGeneralesBinding
import com.example.nutritnt.viewmodel.EncuestaAlimentoViewModel
import com.example.nutritnt.viewmodel.EncuestaViewModel

class ListadoEncuestasGeneralesFragment : Fragment() {
    private lateinit var binding: FragmentListadoEncuestasGeneralesBinding
    private lateinit var encuestaViewModel: EncuestaViewModel
    private val encuestaAlimentoViewModel: EncuestaAlimentoViewModel by viewModels()
    private lateinit var adapter: EncuestaAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentListadoEncuestasGeneralesBinding.inflate(inflater, container, false)
        val view = binding.root

        ViewCompat.setOnApplyWindowInsetsListener(view) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initRecyclerView()

        encuestaViewModel = ViewModelProvider(this, ViewModelProvider.AndroidViewModelFactory.getInstance(
            requireContext().applicationContext as Application
        )).get(EncuestaViewModel::class.java)

        encuestaViewModel.todasLasEncuestas.observe(viewLifecycleOwner, Observer { encuestas ->
            encuestas?.let {
                adapter.setEncuestas(it)
                //verificarEstados(it)
            }
        })
    }

    // Verificar y actualizar el estado de las encuestas
    private fun verificarEstados(encuestas: List<Encuesta>) {
        encuestas.forEach { encuesta ->
            encuestaAlimentoViewModel.getEncuestasAlimentosByEncuestaId(encuesta.encuestaId).observe(viewLifecycleOwner, Observer { encuestasAlimentos ->
                if (encuestasAlimentos.all { it.estado == "COMPLETADA" }) {
                    Log.i("Prueba",encuesta.toString())
                    updateEncuestaEstado(encuesta)
                }
            })
        }
    }

    // Actualizar el estado de la encuesta
    private fun updateEncuestaEstado(encuesta: Encuesta) {
        encuesta.estado = "FINALIZADA"
        encuestaViewModel.update(encuesta)
    }

    private fun initRecyclerView() {
        val recyclerView = binding.recyclerViewEncuesta
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        adapter = EncuestaAdapter { encuestaId ->
            val action = ListadoEncuestasGeneralesFragmentDirections.actionListEncuestasFragmentToEncuestaGeneralOpcionesFragment(encuestaId)
            findNavController().navigate(action)
        }
        recyclerView.adapter = adapter
    }
}
