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
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.nutritnt.R
import com.example.nutritnt.data.DatosDatabase
import com.example.nutritnt.database.entities.Encuesta
import com.example.nutritnt.database.entities.EncuestaAlimento
import com.example.nutritnt.databinding.FragmentNuevaEncuestaAlimentoBinding
import com.example.nutritnt.viewmodel.AlimentoViewModel
import com.example.nutritnt.viewmodel.EncuestaAlimentoViewModel
import com.example.nutritnt.viewmodel.EncuestaViewModel
import kotlinx.coroutines.launch

class NuevaEncuestaAlimentoFragment : Fragment() {

    private lateinit var binding: FragmentNuevaEncuestaAlimentoBinding
    private val encuestaAlimentoViewModel: EncuestaAlimentoViewModel by viewModels()
    private val encuestaViewModel: EncuestaViewModel by viewModels()
    private val alimentoViewModel: AlimentoViewModel by viewModels()

    // Inicializar la variable para manejar los argumentos utilizando navArgs()
    private val args: NuevaEncuestaAlimentoFragmentArgs by navArgs()

    private lateinit var editText: EditText
    private lateinit var minusButton: Button
    private lateinit var plusButton: Button
    private var valueFrecuency: Int = 0

    private lateinit var encuesta: Encuesta
    private lateinit var encuestaAlimento: EncuestaAlimento
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentNuevaEncuestaAlimentoBinding.inflate(layoutInflater)
        val view = binding.root

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

        // Obtener el código del participante pasado como argumento desde el fragmento anterior
        val id = args.encuestaAlimentoId
        Log.i("nik", "Encuesta alimento por args: "+id.toString())

        encuestaAlimentoViewModel.getEncuestaAlimentoById(id).observe(viewLifecycleOwner, Observer{
            this.encuestaAlimento = it
            Log.i("nik ", "Encuesta alimento obtenida!"+encuestaAlimento.encuestaAlimentoId.toString())

            // Observar el LiveData para obtener los datos del alimento asociado a la encuesta de alimento
            alimentoViewModel.fetchAlimentoByEncuestaAlimento(encuestaAlimento.encuestaAlimentoId).observe(viewLifecycleOwner) { alimento ->
                // Actualizar la vista con los datos del alimento
                binding.textViewNameAlimento.text = alimento?.descripcion ?: "Descripción no disponible"
                Log.i("Alimentoooo", alimento?.descripcion ?: "Descripción no disponible")

                // Obtener las porciones correspondientes al alimento desde DatosDatabase
                val portions = DatosDatabase.portions.find { alimento.alimentoId == alimento?.alimentoId }?.portions ?: emptyList()
                setupSpinner(binding.spinnerPortion, portions, encuestaAlimento.portion)
            }

            // Configurar el spinner para el periodo usando el valor almacenado en encuestaAlimento
            setupSpinner(binding.spinnerPeriod, resources.getStringArray(R.array.Period).toList(), encuestaAlimento.period)
            editText.setText(encuestaAlimento.frecuency.toString())

        })

        binding.buttonRegistrar.setOnClickListener {
            // Obtener los valores seleccionados de los Spinners y el texto ingresado en el EditText
            val selectedPortion = extractNumber(binding.spinnerPortion.selectedItem.toString())
            val selectedPeriod = binding.spinnerPeriod.selectedItem.toString()
            val frecuency = editText.text.toString().toIntOrNull() ?: 0

            Log.d("nik", "Botón registrar clickeado")

            // Actualizar encuesta alimento
            encuestaAlimento.portion = selectedPortion.toString()
            encuestaAlimento.period = selectedPeriod
            encuestaAlimento.frecuency = frecuency
            encuestaAlimento.estado = "COMPLETADA"

            encuestaAlimentoViewModel.update(encuestaAlimento)
            Log.d("nik", "Encuesta actualizada!")

            //findNavController().navigate(R.id.action_newEncuestaFragment_to_listEncuestasAlimentosFragment)
            findNavController().navigate(NuevaEncuestaAlimentoFragmentDirections.actionNewEncuestaFragmentToListEncuestasAlimentosFragment(encuestaAlimento.encuestaId))
        }

        return view
    }

    // Configurar un Spinner con un ArrayAdapter, un listener de selección de item y seleccionar el valor predeterminado
    private fun setupSpinner(spinner: Spinner, items: List<String>, defaultValue: String? = null) {
        val adapter = ArrayAdapter(
            requireContext(),
            android.R.layout.simple_spinner_item,
            items
        )
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner.adapter = adapter

        if (defaultValue != null && defaultValue.isNotEmpty()) {
            val defaultPosition = items.indexOf(defaultValue)
            if (defaultPosition >= 0) {
                spinner.setSelection(defaultPosition)
            }
        }

        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
                if (view != null) {
                    val selectedOption = parent.getItemAtPosition(position).toString()
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

    // Función para extraer solo los números de un string
    private fun extractNumber(input: String): Int {
        val regex = Regex("[0-9]+") // Expresión regular para encontrar solo los números
        val matchResult = regex.find(input)
        return matchResult?.value?.toIntOrNull() ?: 0 // Devolver el valor encontrado como entero o 0 si no se encontraron números
    }
}