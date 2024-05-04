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
import com.example.nutritnt.databinding.ActivityWelcomeBinding
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
                val context = requireContext()
                val intent = Intent(context, ListadoEncuestasActivity::class.java)
                context.startActivity(intent)
            }

            // Aplicar los insets del sistema a la vista
            ViewCompat.setOnApplyWindowInsetsListener(view) { v, insets ->
                val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
                v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
                insets
            }

            return view
        }
    }
