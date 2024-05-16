package com.example.nutritnt

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import androidx.navigation.fragment.findNavController

class NuevaEncuestaPruebaFragment : Fragment() {
    private lateinit var nombreEditText: EditText

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_nueva_encuesta_prueba, container, false)

        nombreEditText = view.findViewById(R.id.edit_nombre)
        val buttonSave = view.findViewById<Button>(R.id.button_save)

        buttonSave.setOnClickListener {
            val nombre = nombreEditText.text.toString()

            if (nombre.isNotEmpty()) {
                val action = NuevaEncuestaPruebaFragmentDirections.actionNuevaEncuestaPruebaFragmentToEncuestaPrueba(nombre)
                findNavController().navigate(action)
            }
        }

        return view
    }

    companion object {
        const val EXTRA_RESPUESTA_EQUIPO_1 = "unpsjb.ing.tnt.roompartidos.REPLY_EQUIPO_A"
    }
}