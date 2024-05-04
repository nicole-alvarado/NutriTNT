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

                // Crear instancia de WelcomeFragment y reemplazar LoginFragment con ella
                val transaction = requireActivity().supportFragmentManager.beginTransaction()
                // R.id.fragmentContainer es el id del contenedor de fragmentos en nuestra actividad
                transaction.replace(R.id.fragmentContainer, WelcomeFragment())
                // Agregar a la pila de retroceso para volver atrás
                transaction.addToBackStack(null)
                transaction.commit()

            } else {
                Toast.makeText(context, "Usuario y/o contraseña incorrectos", Toast.LENGTH_SHORT).show()
            }
        }

        // Aplicar los insets del sistema a la vista
        ViewCompat.setOnApplyWindowInsetsListener(view) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        Log.i("estados","onCreate")
        return view
    }


}