package com.example.nutritnt.ui.encuesta

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.nutritnt.R
import com.example.nutritnt.databinding.FragmentEncuestaGeneralOpcionesBinding
import com.example.nutritnt.listado.ListadoEncuestasGeneralesFragmentDirections

class EncuestaGeneralOpcionesFragment : Fragment() {

    private lateinit var binding: FragmentEncuestaGeneralOpcionesBinding

    // Inicializar la variable para manejar los argumentos utilizando navArgs()
    private val args: EncuestaGeneralOpcionesFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentEncuestaGeneralOpcionesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.buttonVerListadoAlimentos.setOnClickListener {
            val action = EncuestaGeneralOpcionesFragmentDirections.actionEncuestaGeneralOpcionesFragmentToListEncuestasAlimentosFragment(args.encuestaId)
            findNavController().navigate(action)
        }

        binding.buttonSubirEncuesta.setOnClickListener {
            // Aquí puedes agregar la lógica para subir la encuesta a Firebase
        }
    }

}