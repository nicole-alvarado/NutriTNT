package com.example.nutritnt

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.nutritnt.databinding.FragmentLoginBinding

class LoginFragment : Fragment() {

    private lateinit var binding: FragmentLoginBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        // Inflar el diseño del fragmento utilizando el FragmentWelcomeBinding
        binding = FragmentLoginBinding.inflate(inflater, container, false)
        val view = binding.root

        binding.loginButton.text = "Ingresar"
        binding.loginButton.setOnClickListener(){
            Log.d("Botones", "Botón ingresar clickeado")
            if (binding.inputUsuario.editText?.text.toString() == "admin" && binding.inputPassword.editText?.text.toString() == "tnt2024"){
                Toast.makeText(context, "Ingreso exitoso!", Toast.LENGTH_SHORT).show()
                
                // Navega al WelcomeFragment utilizando la acción generada por Safe Args y pasa el texto como argumento
                findNavController().navigate(LoginFragmentDirections.actionLoginFragmentToWelcomeFragment("Bienvenido/a"))
            } else {
                Toast.makeText(context, "Usuario y/o contraseña incorrectos", Toast.LENGTH_SHORT).show()
            }
        }

        Log.i("estados","onCreate")
        return view
    }


}