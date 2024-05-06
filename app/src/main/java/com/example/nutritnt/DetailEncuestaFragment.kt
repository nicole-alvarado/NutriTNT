package com.example.nutritnt

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.nutritnt.databinding.FragmentDetailEncuestaBinding

class DetailEncuestaFragment : Fragment() {
    private lateinit var binding: FragmentDetailEncuestaBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        // Inflar el diseño del fragmento utilizando el FragmentWelcomeBinding
        binding = FragmentDetailEncuestaBinding.inflate(inflater, container, false)
        val view = binding.root

        val idEncuesta = arguments?.getString("ENCUESTA_ID").toString()
        val fecha = arguments?.getString("FECHA").toString()
        val estado = arguments?.getString("ESTADO").toString()

        Log.i("enDetailEncuestaFragment", "ID de Encuesta: $idEncuesta")

        binding.idEncuestaValorTextView.text = idEncuesta
        binding.fechaValorTextView.text = fecha
        binding.estadoValorTextView.text = estado

        // Aplicar los insets del sistema a la vista
        ViewCompat.setOnApplyWindowInsetsListener(view) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        Log.i("enDetailEncuestaFragment", "onCreate")
        // Inflate the layout for this fragment
        return view
    }

}