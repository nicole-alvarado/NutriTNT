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

        override fun onCreate(savedInstanceState: Bundle?) {
            super.onCreate(savedInstanceState)

            //setContentView(R.layout.activity_welcome)

            binding = FragmentWelcomeBinding.inflate(layoutInflater)
            val view = binding.root


            binding.buttonEncuestas.text = "Ver encuestas"
            binding.textBienvenida.text = "Binvenido/a"

            binding.buttonEncuestas.setOnClickListener(){
                Log.d("Botones", "Botón ver encuentas clickeado")
                val context = binding.root.context
                val intent = Intent(context, ListadoEncuestasActivity::class.java)
                context.startActivity(intent) // Iniciar la actividad con el intent
            }
            ViewCompat.setOnApplyWindowInsetsListener(view) { v, insets ->
                val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
                v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
                insets
            }

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_welcome, container, false)
    }


}