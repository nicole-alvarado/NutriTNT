package com.example.nutritnt

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.navigation.fragment.navArgs
import com.example.nutritnt.databinding.FragmentDetailEncuestaBinding

class DetailEncuestaFragment : Fragment() {
    private lateinit var binding: FragmentDetailEncuestaBinding
    // Inicializar la variable para manejar los argumentos utilizando navArgs()
    val args:DetailEncuestaFragmentArgs by navArgs()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Obtener los datos como argumentos desde el fragmento anterior
        val idEncuesta = args.idEncuesta
        val fecha = args.fecha
        val estado = args.estado

        Log.i("conSafeArgs", "ID de Encuesta: $idEncuesta")

        // Configurar los valores en las vistas
        binding.idEncuestaValorTextView.text = idEncuesta
        binding.fechaValorTextView.text = fecha
        binding.estadoValorTextView.text = estado
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        // Inflar el diseño del fragmento utilizando el FragmentWelcomeBinding
        binding = FragmentDetailEncuestaBinding.inflate(inflater, container, false)
        val view = binding.root

        Log.i("enDetailEncuestaFragment", "onCreate")
        // Inflate the layout for this fragment
        return view
    }

}