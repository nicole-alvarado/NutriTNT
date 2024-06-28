package com.example.nutritnt.ui.encuesta

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.nutritnt.R
import com.example.nutritnt.database.entities.Encuesta
import com.example.nutritnt.database.entities.EncuestaAlimento
import com.example.nutritnt.database.entities.toMap
import com.example.nutritnt.databinding.FragmentEncuestaGeneralOpcionesBinding
import com.example.nutritnt.viewmodel.EncuestaAlimentoViewModel
import com.example.nutritnt.viewmodel.EncuestaViewModel
import com.google.firebase.firestore.FirebaseFirestore

class EncuestaGeneralOpcionesFragment : Fragment() {

    private lateinit var binding: FragmentEncuestaGeneralOpcionesBinding
    private val encuestaAlimentoViewModel: EncuestaAlimentoViewModel by viewModels()
    private val encuestaViewModel: EncuestaViewModel by viewModels()
    private lateinit var alimentosDeLaEncuesta: List<EncuestaAlimento>
    private lateinit var encuestaGeneral: Encuesta
    private val db = FirebaseFirestore.getInstance()

    // Inicializar la variable para manejar los argumentos utilizando navArgs()
    private val args: EncuestaGeneralOpcionesFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentEncuestaGeneralOpcionesBinding.inflate(inflater, container, false)
        binding.buttonSubirEncuesta.isEnabled = false
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Verificar en Firebase si la encuesta ya existe y su estado
        db.collection("encuestas")
            .whereEqualTo("encuestaId", args.encuestaId)
            .get()
            .addOnSuccessListener { documents ->
                if (documents.isEmpty) {
                    if (encuestaGeneral.estado == "FINALIZADA") {
                        // Si no existe la encuesta y está finalizada, habilitar el botón
                        binding.buttonSubirEncuesta.isEnabled = true
                    } else {
                        // Si la encuesta no está finalizada
                        binding.buttonSubirEncuesta.isEnabled = false
                        Toast.makeText(context, "La encuesta no ha sido finalizada", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    // Si ya existe la encuesta en la nube
                    binding.buttonSubirEncuesta.isEnabled = false
                    Toast.makeText(context, "La encuesta ya existe en la nube", Toast.LENGTH_SHORT).show()
                }
            }
            .addOnFailureListener { exception ->
                Log.w("EncuestaGeneralOpcionesFragment", "Error getting documents: ", exception)
                Toast.makeText(context, "Error al verificar la encuesta: ${exception.message}", Toast.LENGTH_SHORT).show()
            }


        // Obtener la encuesta general desde un principio
        encuestaViewModel.getEncuestaById(args.encuestaId).observe(viewLifecycleOwner, Observer { encuesta ->
            encuestaGeneral = encuesta
        })

        // Observa los cambios en los datos de encuestas alimentos
        encuestaAlimentoViewModel.getEncuestasAlimentosByEncuestaId(args.encuestaId).observe(viewLifecycleOwner, Observer { encuestas_alimentos ->
            encuestas_alimentos?.let {
                alimentosDeLaEncuesta = it
            }
        })

        binding.buttonVerListadoAlimentos.setOnClickListener {
            val action = EncuestaGeneralOpcionesFragmentDirections.actionEncuestaGeneralOpcionesFragmentToListEncuestasAlimentosFragment(args.encuestaId)
            findNavController().navigate(action)
        }

        binding.buttonSubirEncuesta.setOnClickListener {
                binding.buttonSubirEncuesta.isEnabled = false // Deshabilitar el botón después de presionarlo
                subirEncuesta()
        }

        binding.buttonBackWelcome.setOnClickListener {
            findNavController().navigate(R.id.action_encuestaGeneralOpcionesFragment_to_welcomeFragment)
        }

    }

    private fun subirEncuesta() {
        if (!::alimentosDeLaEncuesta.isInitialized || !::encuestaGeneral.isInitialized) {
            Toast.makeText(context, "No hay datos de encuesta para subir", Toast.LENGTH_SHORT).show()
            return
        }

        val encuestaData = encuestaGeneral.toMap().toMutableMap()
        encuestaData["encuestasAlimentos"] = alimentosDeLaEncuesta.map { it.toMap() }

        db.collection("encuestas")
            .add(encuestaData)
            .addOnSuccessListener { documentReference ->
                Toast.makeText(context, "¡Encuesta subida con éxito!", Toast.LENGTH_SHORT).show()
            }
            .addOnFailureListener { e ->
                Toast.makeText(context, "Error al subir la encuesta: ${e.message}", Toast.LENGTH_SHORT).show()
                binding.buttonSubirEncuesta.isEnabled = true // Rehabilitar el botón si falla
            }
    }
}
