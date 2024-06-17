package com.example.nutritnt.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.example.nutritnt.R
import com.example.nutritnt.databinding.FragmentMenuEstadisticasBinding
import com.example.nutritnt.databinding.FragmentNuevaEncuestaAlimentoBinding


class MenuEstadisticasFragment : Fragment() {

    private lateinit var binding: FragmentMenuEstadisticasBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentMenuEstadisticasBinding.inflate(layoutInflater)
        val view = binding.root

        binding.buttonEstGenerales.setOnClickListener{
            findNavController().navigate(R.id.action_menuEstadisticasFragment_to_estadisticaFragment)
        }

        binding.buttonEstMap.setOnClickListener{
            findNavController().navigate(R.id.action_menuEstadisticasFragment_to_estadisticaMapFragment)
        }

        binding.buttonBackWelcome.setOnClickListener{
            findNavController().navigate(R.id.action_menuEstadisticasFragment_to_welcomeFragment)
        }


        return view


    }


}