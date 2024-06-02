package com.example.nutritnt.ui.encuesta

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.nutritnt.R
import com.example.nutritnt.databinding.FragmentNuevaEncuestaBinding

class NuevaEncuestaFragment : Fragment() {

    private lateinit var binding: FragmentNuevaEncuestaBinding


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentNuevaEncuestaBinding.inflate(layoutInflater)
        val view = binding.root
        // Inflate the layout for this fragment
        return view
    }

}