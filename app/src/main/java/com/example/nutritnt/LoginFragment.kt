package com.example.nutritnt
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.navigation.fragment.findNavController
import com.example.nutritnt.databinding.FragmentLoginBinding

class LoginFragment : Fragment() {

    private lateinit var binding: FragmentLoginBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        // Inflar el diseño del fragmento utilizando el FragmentWelcomeBinding
        //binding = FragmentLoginBinding.inflate(inflater, container, false)
        //val view = binding.root

        // Inflar el diseño del fragmento utilziando DataBindingUtil para generar el binding
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_login, container, false)
        
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
        // Retorna la raíz del layout inflado con Data Binding
        return binding.root
    }


}