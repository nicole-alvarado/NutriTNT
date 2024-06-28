package com.example.nutritnt.ui.listado

import android.app.Application
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.nutritnt.R
import com.example.nutritnt.adapter.EncuestaAdapter
import com.example.nutritnt.database.entities.Encuesta
import com.example.nutritnt.databinding.FragmentListadoEncuestasGeneralesBinding
import com.example.nutritnt.viewmodel.EncuestaAlimentoViewModel
import com.example.nutritnt.viewmodel.EncuestaViewModel

class ListadoEncuestasGeneralesFragment : Fragment() {
    private lateinit var binding: FragmentListadoEncuestasGeneralesBinding
    private lateinit var encuestaViewModel: EncuestaViewModel
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

        setupSpinners()

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
                val encuestasInvetidas = it.reversed()
                adapter.setEncuestas(encuestasInvetidas)
            }
        })

        binding.buttonVolver.setOnClickListener(){
            findNavController().navigate(R.id.action_listEncuestasFragment_to_welcomeFragment)
        }
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

    private fun setupSpinners() {
        val estados = arrayOf("Todos", "INICIADA", "FINALIZADA")
        val zonas = arrayOf("Todos", "Zona 1", "Zona 2", "Zona 3", "Zona 4")

        val estadoAdapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, estados)
        val zonaAdapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, zonas)

        binding.spinnerEstado.adapter = estadoAdapter
        binding.spinnerZona.adapter = zonaAdapter

        binding.spinnerEstado.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
                filterEncuestas()
            }

            override fun onNothingSelected(parent: AdapterView<*>) {}
        }

        binding.spinnerZona.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
                filterEncuestas()
            }

            override fun onNothingSelected(parent: AdapterView<*>) {}
        }
    }

    private fun filterEncuestas() {
        val estado = binding.spinnerEstado.selectedItem.toString()
        val zona = binding.spinnerZona.selectedItem.toString()
        adapter.filterEncuestas(estado, zona)
    }
}
