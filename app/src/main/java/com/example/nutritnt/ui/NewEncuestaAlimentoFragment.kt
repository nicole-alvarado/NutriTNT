package com.example.nutritnt.ui

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.nutritnt.R
import com.example.nutritnt.database.entities.Encuesta_Alimento
import com.example.nutritnt.databinding.FragmentNewEncuestaAlimentoBinding
import com.example.nutritnt.viewmodel.EncuestaAlimentoViewModel

class NewEncuestaAlimentoFragment : Fragment() {

    private lateinit var binding: FragmentNewEncuestaAlimentoBinding
    private val viewModelEncuestaAlimento: EncuestaAlimentoViewModel by viewModels()

    private lateinit var editText: EditText
    private lateinit var minusButton: Button
    private lateinit var plusButton: Button
    private var valueFrecuency: Int = 0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentNewEncuestaAlimentoBinding.inflate(layoutInflater)
        val view = binding.root

        // Configurar los Spinners
        setupSpinner(binding.spinnerPortion, R.array.Portion)
        setupSpinner(binding.spinnerPeriod, R.array.Period)

        // Obtener referencias a las vistas de los botones y el EditText
        editText = view.findViewById(R.id.editText)
        minusButton = view.findViewById(R.id.minusButton)
        plusButton = view.findViewById(R.id.plusButton)

        // Configurar los listeners para los botones
        minusButton.setOnClickListener {
            decrement()
        }

        plusButton.setOnClickListener {
            increment()
        }

        binding.buttonRegistrar.setOnClickListener {
            // Obtener los valores seleccionados de los Spinners y el texto ingresado en el EditText
            val selectedPortion = binding.spinnerPortion.selectedItem.toString()
            val selectedPeriod = binding.spinnerPeriod.selectedItem.toString()
            val frecuency = editText.text.toString().toIntOrNull() ?: 0

            Log.d("Botones", "Botón registrar clickeado")

            // Crear objeto Encuesta_Alimento con los valores seleccionados
            val nuevaEncuestaAlimento = Encuesta_Alimento(
                portion = selectedPortion,
                period = selectedPeriod,
                frecuency = frecuency,
                encuestaId = 2, // ID temporal, debemos asignarle el id correcto de una encuesta
                alimentoId = 1,
            )

            // Insertar nueva encuesta de alimento en la base de datos a través del ViewModel
            viewModelEncuestaAlimento.insert(nuevaEncuestaAlimento)
            //findNavController().navigate(R.id.action_newEncuestaFragment_to_listEncuestasAlimentosFragment)

        }

        return view
    }


    // Configurar un Spinner con un ArrayAdapter y un listener de selección de item
    private fun setupSpinner(spinner: Spinner, arrayResource: Int) {
        val adapter = ArrayAdapter(
            requireContext(),
            android.R.layout.simple_spinner_item,
            resources.getStringArray(arrayResource)
        )
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner.adapter = adapter

        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
                if (view != null) {
                    val selectedOption = parent.getItemAtPosition(position).toString()
                    Toast.makeText(requireContext(), getString(R.string.selected_item) + " " + selectedOption, Toast.LENGTH_SHORT).show()
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                // No se seleccionó nada
            }
        }
    }


    private fun increment() {
        valueFrecuency++
        editText.setText(valueFrecuency.toString())
    }

    private fun decrement() {
        if (valueFrecuency > 0) {
            valueFrecuency--
            editText.setText(valueFrecuency.toString())
        }
    }
}
