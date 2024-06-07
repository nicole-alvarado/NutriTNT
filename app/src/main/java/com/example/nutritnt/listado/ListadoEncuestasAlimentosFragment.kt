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
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.nutritnt.R
import com.example.nutritnt.adapter.EncuestaAlimentoAdapter
import com.example.nutritnt.databinding.FragmentListadoEncuestasAlimentosBinding
import com.example.nutritnt.viewmodel.AlimentoViewModel
import com.example.nutritnt.viewmodel.EncuestaAlimentoViewModel

class ListadoEncuestasAlimentosFragment : Fragment() {
    private lateinit var binding: FragmentListadoEncuestasAlimentosBinding
    private lateinit var encuestaAlimentoViewModel: EncuestaAlimentoViewModel
    private lateinit var adapter: EncuestaAlimentoAdapter
    private val alimentoViewModel: AlimentoViewModel by viewModels()

    // Inicializar la variable para manejar los argumentos utilizando navArgs()
    private val args: ListadoEncuestasAlimentosFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        // Inflar el diseño del fragmento utilizando el FragmentWelcomeBinding
        binding = FragmentListadoEncuestasAlimentosBinding.inflate(inflater, container, false)
        val view = binding.root

        binding.buttonInicio.setOnClickListener(){
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

        // Obtener el ID de la encuesta general a través de los argumentos
        Log.i("Muricion Encuesta General para listado", args.encuestaId.toString())
        val encuestaGeneralId = args.encuestaId

        // Observar los cambios en los datos de encuestas alimentos filtrndas por ID y actualizar el adaptador cuando los datos cambien
        encuestaAlimentoViewModel.getEncuestasAlimentosByEncuestaId(encuestaGeneralId).observe(viewLifecycleOwner, Observer { encuestas_alimentos ->
            encuestas_alimentos?.let {
                adapter.setEncuestasAlimentos(it)
                Log.i("Muricion - Encuesta alimento de la encuesta ", encuestaGeneralId.toString())
            }
        })

        // Observar los cambios en los datos de encuestas y actualizar el adaptador cuando los datos cambien
//        encuestaAlimentoViewModel.todasLasEncuestasAlimento.observe(viewLifecycleOwner, Observer { encuestas_alimentos ->
//            encuestas_alimentos?.let { adapter.setEncuestasAlimentos(it) }
//        })
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