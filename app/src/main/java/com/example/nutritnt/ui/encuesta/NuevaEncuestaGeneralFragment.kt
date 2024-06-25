package com.example.nutritnt.ui.encuesta

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.nutritnt.R
import com.example.nutritnt.database.entities.Alimento
import com.example.nutritnt.database.entities.Encuesta
import com.example.nutritnt.database.entities.EncuestaAlimento
import com.example.nutritnt.databinding.FragmentNuevaEncuestaGeneralBinding
import com.example.nutritnt.viewmodel.AlimentoViewModel
import com.example.nutritnt.viewmodel.EncuestaAlimentoViewModel
import com.example.nutritnt.viewmodel.EncuestaViewModel
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import kotlin.random.Random

class NuevaEncuestaGeneralFragment : Fragment() {

    private lateinit var binding: FragmentNuevaEncuestaGeneralBinding
    private val encuestaViewModel: EncuestaViewModel by viewModels()
    private val encuestaAlimentoViewModel: EncuestaAlimentoViewModel by viewModels()
    private val alimentoViewModel: AlimentoViewModel by viewModels()
    val alimentosList = mutableListOf<Alimento>() // Lista para almacenar los alimentos
    private lateinit var encuesta: Encuesta

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentNuevaEncuestaGeneralBinding.inflate(layoutInflater)
        val view = binding.root

        // Observar los alimentos y guardarlos en la lista
        alimentoViewModel.todosLosAlimentos.observe(viewLifecycleOwner, Observer { alimentos ->
            alimentosList.addAll(alimentos)
        })

        // Deshabilitar el botón al inicio
        binding.buttonComenzarEncuesta.isEnabled = false

        // Añadir TextWatcher al campo de texto del código de participante
        binding.textInputCodigoParticipante.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                // Habilitar o deshabilitar el botón en función de si el campo está vacío o no
                binding.buttonComenzarEncuesta.isEnabled = !s.isNullOrEmpty()
            }

            override fun afterTextChanged(s: Editable?) {}
        })

        binding.buttonBackWelcome.setOnClickListener {
            findNavController().navigate(R.id.action_nuevaEncuestaFragment_to_welcomeFragment)
        }


        binding.buttonComenzarEncuesta.setOnClickListener {
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

            lifecycleScope.launch {
                // Insertar la nueva encuesta y obtener el ID generado
                encuestaViewModel.insert(nuevaEncuesta)

                // Navega al NuevaEncuestaFragment (encuesta alimento) utilizando la acción generada por Safe Args y pasa codigoParticipante como argumento
                // findNavController().navigate(NuevaEncuestaGeneralFragmentDirections.actionNuevaEncuestaFragmentToUbicacionConsumidorFragment())
                // Observar el LiveData después de la inserción
                encuestaViewModel.getEncuestaByCodigoParticipante(codigoParticipante).observe(viewLifecycleOwner, Observer { encuesta ->
                    if (encuesta != null) {
                        this@NuevaEncuestaGeneralFragment.encuesta = encuesta
                        val encuestasAlimentosList = alimentosList.map { alimento ->
                            //Log.i("Muricion Alimentos", alimento.descripcion)
                            EncuestaAlimento(
                                portion = "",
                                period = "",
                                frecuency = 0,
                                encuestaId = encuesta.encuestaId,
                                alimentoId = alimento.alimentoId,
                                estado = "NO COMPLETADA"
                            )
                        }

                        encuestasAlimentosList.forEach { encuestaAlimento ->
                            encuestaAlimentoViewModel.insert(encuestaAlimento)
                        }
                        findNavController().navigate(NuevaEncuestaGeneralFragmentDirections.actionNuevaEncuestaFragmentToUbicacionConsumidorFragment(codigoParticipante))
                        // findNavController().navigate(NuevaEncuestaGeneralFragmentDirections.actionNuevaEncuestaFragmentToListEncuestasAlimentosFragment(encuesta.encuestaId))
                        //findNavController().navigate(NuevaEncuestaGeneralFragmentDirections.actionNuevaEncuestaFragmentToNewEncuestaFragment(codigoParticipante))
                    } else {
                        Log.e("Error", "Encuesta no encontrada")
                    }
                })
            }
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
