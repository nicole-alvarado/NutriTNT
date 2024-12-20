package com.example.nutritnt.listado

import android.app.Application
import android.os.Bundle
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
import com.example.nutritnt.R
import com.example.nutritnt.adapter.EncuestaAdapter
import com.example.nutritnt.adapter.EncuestaAlimentoAdapter
import com.example.nutritnt.databinding.FragmentListEncuestasAlimentosBinding
import com.example.nutritnt.databinding.FragmentListEncuestasBinding
import com.example.nutritnt.viewmodel.AlimentoViewModel
import com.example.nutritnt.viewmodel.EncuestaAlimentoViewModel
import com.example.nutritnt.viewmodel.EncuestaViewModel

class ListEncuestasAlimentosFragment : Fragment() {
    private lateinit var binding: FragmentListEncuestasAlimentosBinding
    private lateinit var encuestaAlimentoViewModel: EncuestaAlimentoViewModel
    private lateinit var adapter: EncuestaAlimentoAdapter
    private val alimentoViewModel: AlimentoViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        // Inflar el diseño del fragmento utilizando el FragmentWelcomeBinding
        binding = FragmentListEncuestasAlimentosBinding.inflate(inflater, container, false)
        val view = binding.root

        binding.buttonInicio.setOnClickListener(){
            //findNavController().navigate(ListEncuestasAlimentosFragmentDirections.actionListEncuestasAlimentosFragmentToWelcomeFragment("Bienvenido/a"))
            findNavController().navigate(R.id.action_listEncuestasAlimentosFragment_to_welcomeFragment)
        }

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
        encuestaAlimentoViewModel = ViewModelProvider(this, ViewModelProvider.AndroidViewModelFactory.getInstance(
            requireContext().applicationContext as Application
        )).get(EncuestaAlimentoViewModel::class.java)

        // Observar los cambios en los datos de encuestas y actualizar el adaptador cuando los datos cambien
        encuestaAlimentoViewModel.todasLasEncuestasAlimento.observe(viewLifecycleOwner, Observer { encuestas_alimentos ->
            encuestas_alimentos?.let { adapter.setEncuestasAlimentos(it) }
        })

    }

    private fun initRecyclerView() {
        // Obtener la referencia al RecyclerView desde el binding
        val recyclerView = binding.recyclerViewEncuestaAlimento
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        // Inicializar el adaptador
        adapter = EncuestaAlimentoAdapter(alimentoViewModel)
        recyclerView.adapter = adapter


    }
}