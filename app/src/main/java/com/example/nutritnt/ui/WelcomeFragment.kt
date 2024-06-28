package com.example.nutritnt.ui

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.example.nutritnt.R
import com.example.nutritnt.databinding.FragmentWelcomeBinding
import com.google.firebase.auth.FirebaseAuth


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
            binding.buttonNewEncuesta.text = "Nueva encuesta"
            //binding.textBienvenida.text = "Bienvenido/a"

            binding.buttonEncuestas.setOnClickListener {

                findNavController().navigate(R.id.action_welcomeFragment_to_listEncuestasFragment)
            }

            binding.buttonNewEncuesta.setOnClickListener{

                findNavController().navigate(R.id.action_welcomeFragment_to_nuevaEncuestaFragment)
            }

            binding.buttonEstadisticas.setOnClickListener{
                findNavController().navigate(R.id.action_welcomeFragment_to_menuEstadisticasFragment)
            }

            binding.buttonLogOut?.setOnClickListener{
                FirebaseAuth.getInstance().signOut()
                findNavController().navigate(R.id.action_welcomeFragment_to_loginFragment)
            }
            return view
        }

    }
