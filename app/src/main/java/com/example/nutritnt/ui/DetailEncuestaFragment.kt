package com.example.nutritnt.ui

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.navArgs
import com.example.nutritnt.databinding.FragmentDetailEncuestaBinding

class DetailEncuestaFragment : Fragment() {
    private lateinit var binding: FragmentDetailEncuestaBinding
    // Inicializar la variable para manejar los argumentos utilizando navArgs()
    val args:DetailEncuestaFragmentArgs by navArgs()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Obtener los datos como argumentos desde el fragmento anterior
        val idEncuesta = args.idEncuesta
        val fecha = args.fecha
        val estado = args.estado


        // Configurar los valores en las vistas
        binding.idEncuestaValorTextView.text = idEncuesta
        binding.fechaValorTextView.text = fecha
        binding.estadoValorTextView.text = estado
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        // Inflar el diseño del fragmento
        binding = FragmentDetailEncuestaBinding.inflate(inflater, container, false)
        val view = binding.root

        return view
    }

}