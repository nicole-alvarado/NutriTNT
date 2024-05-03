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
import com.example.nutritnt.databinding.FragmentLoginBinding




class LoginFragment : Fragment() {

    private lateinit var binding: FragmentLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        binding = FragmentLoginBinding.inflate(layoutInflater)
        val view = binding.root


        binding.loginButton.setOnClickListener(){
            Log.d("Botones", "Botón ingresar clickeado")
            if (binding.inputUsuario.editText?.text.toString() == "admin" && binding.inputPassword.editText?.text.toString() == "tnt2024"){
                Toast.makeText(context, "Ingreso exitoso!", Toast.LENGTH_SHORT).show()

                val context = binding.root.context
                val intent = Intent(context, WelcomeFragment::class.java)

                context.startActivity(intent) // Iniciar la actividad con el intent

            } else {
                Toast.makeText(context, "Usuario y/o contraseña incorrectos", Toast.LENGTH_SHORT).show()
            }

        }

        ViewCompat.setOnApplyWindowInsetsListener(view) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        Log.i("estados","onCreate")

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_login, container, false)
    }


}