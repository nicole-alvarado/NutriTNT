package com.example.nutritnt

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.nutritnt.databinding.FragmentWelcomeBinding

class WelcomeFragment : Fragment() {

        private lateinit var binding: FragmentWelcomeBinding
        // Inicializar la variable para manejar los argumentos utilizando navArgs()
        val args:WelcomeFragmentArgs by navArgs()

    // Método que se llama cuando la vista ya ha sido creada
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // Obtener el nombre pasado como argumento desde el fragmento anterior
        val name = args.name
        // Asignar el nombre al TextView en el diseño del fragmento
     //   binding.textBienvenida.text = name
    }

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
        ): View? {
            // Inflar el diseño del fragmento utilizando el FragmentWelcomeBinding
            binding = FragmentWelcomeBinding.inflate(inflater, container, false)
            val view = binding.root

            binding.buttonEncuestas.text = "Ver encuestas"
            binding.buttonNewEncuesta.text = "Nueva encuesta"
            //binding.textBienvenida.text = "Bienvenido/a"

            binding.buttonEncuestas.setOnClickListener {
                Log.d("Botones", "Botón ver encuestas clickeado")

                findNavController().navigate(R.id.action_welcomeFragment_to_listEncuestasFragment)
            }

            binding.buttonNewEncuesta.setOnClickListener{
                Log.d("enWelcomeFragment","Boton encuesta prueba")
                findNavController().navigate(R.id.action_welcomeFragment_to_newEncuestaFragment)
            }

            return view
        }
    }
