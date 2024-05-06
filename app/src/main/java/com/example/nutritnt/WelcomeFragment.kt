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
        val args:WelcomeFragmentArgs by navArgs()

    // Método que se llama cuando ya tenemos la vista creada
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val name = args.name
        binding.textBienvenida.text = name
    }

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
        ): View? {
            // Inflar el diseño del fragmento utilizando el FragmentWelcomeBinding
            binding = FragmentWelcomeBinding.inflate(inflater, container, false)
            val view = binding.root

            binding.buttonEncuestas.text = "Ver encuestas"
            //binding.textBienvenida.text = "Bienvenido/a"

            binding.buttonEncuestas.setOnClickListener {
                Log.d("Botones", "Botón ver encuestas clickeado")

                findNavController().navigate(R.id.action_welcomeFragment_to_listEncuestasFragment)

            }

            return view
        }
    }
