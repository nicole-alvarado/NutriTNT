package com.example.nutritnt
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.nutritnt.databinding.FragmentLoginBinding
import com.example.nutritnt.viewmodel.LoginViewModel
import com.google.firebase.auth.FirebaseAuth

class LoginFragment : Fragment() {

    private lateinit var binding: FragmentLoginBinding
    val viewModel: LoginViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {


        // Inflar el diseño del fragmento utilizando el FragmentWelcomeBinding
        //binding = FragmentLoginBinding.inflate(inflater, container, false)
        //val view = binding.root

        // Inflar el diseño del fragmento utilizando DataBindingUtil para generar el binding
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_login, container, false)
        Log.d("DataBinding", "Layout inflado correctamente")

        binding.lifecycleOwner = viewLifecycleOwner
        binding.loginViewModel = viewModel

        binding.loginButton.text = "Ingresar"

        // CÓDIGO PARA AUTHENTICATION
//        binding.loginButton.setOnClickListener(){
//
//            // Obtener inputUsuario e inputPassword desde el xml
//            val usernameInput = binding.inputUsuario.editText?.text.toString()
//            val passwordInput = binding.inputPassword.editText?.text.toString()
//
//            // Actualizar los LiveData en el ViewModel con el usuario y la contraseña ingresado
//            viewModel.setUser(usernameInput)
//            viewModel.setPassword(passwordInput)
//
//            Log.d("PruebaAuth", "Usuario ingresado: " + viewModel.user.value)
//            Log.d("PruebaAuth", "Contraseña ingresada: " + viewModel.password.value)
//            if (viewModel.user.value != "" && viewModel.password.value != ""){
//                FirebaseAuth.getInstance().signInWithEmailAndPassword(viewModel.user.value.toString(),viewModel.password.value.toString())
//                    .addOnCompleteListener{
//                        if (it.isSuccessful){
//                            findNavController().navigate(R.id.action_loginFragment_to_welcomeFragment)
//                        }else{
//                            Toast.makeText(context, "Usuario y/o contraseña incorrectos", Toast.LENGTH_SHORT).show()
//                        }
//                    }
//            }
//        }

        binding.loginButton.setOnClickListener(){

            // Obtener inputUsuario e inputPassword desde el xml
            val usernameInput = binding.inputUsuario.editText?.text.toString()
            val passwordInput = binding.inputPassword.editText?.text.toString()

            // Actualizar los LiveData en el ViewModel con el usuario y la contraseña ingresado
            viewModel.setUser(usernameInput)
            viewModel.setPassword(passwordInput)

            Log.d("enLoginFragment", "Usuario ingresado: $usernameInput")
            Log.d("enLoginFragment", "Contraseña ingresada: $passwordInput")

            if (viewModel.user.value != "admin" && viewModel.password.value != "tnt2024"){
                Toast.makeText(context, "Ingreso exitoso!", Toast.LENGTH_SHORT).show()

                // Navega al WelcomeFragment utilizando la acción generada por Safe Args y pasa el texto como argumento
                //findNavController().navigate(LoginFragmentDirections.actionLoginFragmentToWelcomeFragment("Bienvenido/a"))
                findNavController().navigate(R.id.action_loginFragment_to_welcomeFragment)
            } else {
                Toast.makeText(context, "Usuario y/o contraseña incorrectos", Toast.LENGTH_SHORT).show()
            }
        }

        Log.i("estados","onCreate")
        // Retornar la raíz del layout inflado con Data Binding
        return binding.root
    }


}