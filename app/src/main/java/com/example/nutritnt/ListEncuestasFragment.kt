package com.example.nutritnt

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.nutritnt.adapter.EncuestaAdapter
import com.example.nutritnt.adapter.EncuestaViewHolder
import com.example.nutritnt.databinding.FragmentListEncuestasBinding
import com.example.nutritnt.provider.EncuestaProvider
import kotlin.random.Random

class ListEncuestasFragment : Fragment() {
    private lateinit var binding: FragmentListEncuestasBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        // Inflar el diseño del fragmento utilizando el FragmentWelcomeBinding
        binding = FragmentListEncuestasBinding.inflate(inflater, container, false)
        val view = binding.root

        ViewCompat.setOnApplyWindowInsetsListener(view) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initRecyclerView()
    }

    private fun initRecyclerView(){
        val recyclerView = binding.recyclerViewEncuesta
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        recyclerView.adapter = EncuestaAdapter(EncuestaProvider.encuestaList)
    }

}