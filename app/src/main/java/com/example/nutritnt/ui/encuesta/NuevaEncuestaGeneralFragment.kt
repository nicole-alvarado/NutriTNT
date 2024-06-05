package com.example.nutritnt.ui.encuesta

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.nutritnt.database.entities.Encuesta
import com.example.nutritnt.databinding.FragmentNuevaEncuestaGeneralBinding
import com.example.nutritnt.viewmodel.EncuestaViewModel
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import kotlin.random.Random

class NuevaEncuestaGeneralFragment : Fragment() {

    private lateinit var binding: FragmentNuevaEncuestaGeneralBinding
    private val encuestaViewModel: EncuestaViewModel by viewModels()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentNuevaEncuestaGeneralBinding.inflate(layoutInflater)
        val view = binding.root


        binding.buttonComenzarCopiaEncuesta.setOnClickListener{

            findNavController().navigate(NuevaEncuestaFragmentDirections.actionNuevaEncuestaFragmentToCopiaNuevaEncuestaAlimentoFragment())


        }

        binding.buttonComenzarEncuesta.setOnClickListener{
            val nombreRandom = generateRandomNombre()
            val fecha = obtenerFechaActual()
            val codigoParticipante = binding.textInputCodigoParticipante.text.toString()

            // Crear objeto Encuesta
            val nuevaEncuesta = Encuesta(
                nombre = nombreRandom,
                fecha = fecha,
                estado = "INICIADA",
                codigoParticipante = codigoParticipante,
                latitud = "latitud",
                longitud = "longitud",
                zona = "zona"
            )

            // Insertar nueva encuesta en la base de datos a través del ViewModel
            encuestaViewModel.insert(nuevaEncuesta)

            // Navega al NuevaEncuestaFragment (encuesta alimento) utilizando la acción generada por Safe Args y pasa codigoParticipante como argumento
            findNavController().navigate(NuevaEncuestaGeneralFragmentDirections.actionNuevaEncuestaFragmentToNewEncuestaFragment(codigoParticipante))

        }
        // Inflate the layout for this fragment
        return view
    }

    private fun generateRandomNombre(): String {
        return "ID-${Random.nextInt(1000)}"
    }

    private fun obtenerFechaActual(): String {
        val currentDate = Date()
        val formatter = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault())
        return formatter.format(currentDate)
    }

}