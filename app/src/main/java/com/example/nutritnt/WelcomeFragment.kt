package com.example.nutritnt

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.enableEdgeToEdge
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.navigation.fragment.findNavController
import com.example.nutritnt.databinding.FragmentWelcomeBinding

class WelcomeFragment : Fragment() {

        private lateinit var binding: FragmentWelcomeBinding

        override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
        ): View? {
            // Inflar el diseño del fragmento utilizando el FragmentWelcomeBinding
            binding = FragmentWelcomeBinding.inflate(inflater, container, false)
            val view = binding.root

            binding.buttonEncuestas.text = "Ver encuestas"
            binding.textBienvenida.text = "Bienvenido/a"

            binding.buttonEncuestas.setOnClickListener {
                Log.d("Botones", "Botón ver encuestas clickeado")

                findNavController().navigate(R.id.action_welcomeFragment_to_listEncuestasFragment)
                // Crear instancia de WelcomeFragment y reemplazar LoginFragment con ella
                //val transaction = requireActivity().supportFragmentManager.beginTransaction()
                // R.id.fragmentContainer es el id del contenedor de fragmentos en nuestra actividad
                //transaction.replace(R.id.fragmentContainer, ListEncuestasFragment())
                // Agregar a la pila de retroceso para volver atrás
                //transaction.addToBackStack(null)
                //transaction.commit()
            }

            return view
        }
    }
