package com.example.nutritnt.ui

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
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
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.nutritnt.R
import com.example.nutritnt.data.DatosDatabase
import com.example.nutritnt.database.entities.EncuestaAlimento
import com.example.nutritnt.databinding.FragmentNuevaEncuestaAlimentoBinding
import com.example.nutritnt.viewmodel.AlimentoViewModel
import com.example.nutritnt.viewmodel.EncuestaAlimentoViewModel

class NuevaEncuestaAlimentoFragment : Fragment() {

    private lateinit var binding: FragmentNuevaEncuestaAlimentoBinding
    private val encuestaAlimentoViewModel: EncuestaAlimentoViewModel by viewModels()
    private val alimentoViewModel: AlimentoViewModel by viewModels()

    private val args: NuevaEncuestaAlimentoFragmentArgs by navArgs()

    private lateinit var editText: EditText
    private lateinit var minusButton: Button
    private lateinit var plusButton: Button
    private var valueFrecuency: Int = 0

    private lateinit var encuestaAlimento: EncuestaAlimento
    private var currentIndex: Int = -1
    private lateinit var currentEncuesta: EncuestaAlimento
    private lateinit var todasLasEncuestas: List<EncuestaAlimento>
    private var isDataLoaded = false // Variable booleana para comprobar si el observable ya se ejecutó

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflar el layout del fragmento utilizando ViewBinding
        binding = FragmentNuevaEncuestaAlimentoBinding.inflate(layoutInflater)
        val view = binding.root

        editText = view.findViewById(R.id.editText)
        minusButton = view.findViewById(R.id.minusButton)
        plusButton = view.findViewById(R.id.plusButton)

        // Configurar los botones de incremento y decremento
        minusButton.setOnClickListener { decrement() }
        plusButton.setOnClickListener { increment() }

        // Obtener el id de la encuestaAlimento pasado al fragmento
        val encuestaAlimentoId = args.encuestaAlimentoId

        // Obtener todas las encuestas de alimentos
        encuestaAlimentoViewModel.getEncuestasAlimentosByEncuestaId(args.encuestaGeneralId).observe(viewLifecycleOwner, Observer { encuestas ->
            if (!isDataLoaded) { // Solo cargar los datos la primera vez
                todasLasEncuestas = encuestas
                Log.i("Porfas","En el observable")
                // Encontrar el índice de la encuesta actual
                currentIndex = todasLasEncuestas.indexOfFirst { it.encuestaAlimentoId == encuestaAlimentoId }
                if (currentIndex != -1) {
                    currentEncuesta = todasLasEncuestas[currentIndex]
                    encuestaAlimento = currentEncuesta // Inicializar encuestaAlimento
                    // Actualizar la UI con la encuesta actual
                    updateUIWithEncuestaAlimento(currentEncuesta)
                }
                isDataLoaded = true // Marcar que los datos han sido cargados
            }
        })

        // Configurar los botones "Anterior" y "Siguiente"
        binding.buttonAnterior?.setOnClickListener { showPreviousEncuesta() }
        binding.buttonSiguiente?.setOnClickListener { showNextEncuesta() }

        // Botón para guardar la encuestaAlimento y navegar al listado de encuestas alimentos
        binding.buttonRegistrar.setOnClickListener {
            saveEncuestaAlimento()
            findNavController().navigate(NuevaEncuestaAlimentoFragmentDirections.actionNewEncuestaFragmentToListEncuestasAlimentosFragment(encuestaAlimento.encuestaId))
        }

        return view
    }

    // Mostrar la encuesta de alimentos anterior
    private fun showPreviousEncuesta() {
        if (currentIndex > 0) {
            currentIndex--
            currentEncuesta = todasLasEncuestas[currentIndex]
            encuestaAlimento = currentEncuesta // Actualizar encuestaAlimento
            // Actualizar la UI con la encuesta anterior
            updateUIWithEncuestaAlimento(currentEncuesta)
        }
    }

    // Mostrar la siguiente encuesta de alimentos
    private fun showNextEncuesta() {
        if (currentIndex < todasLasEncuestas.size - 1) {
            currentIndex++
            currentEncuesta = todasLasEncuestas[currentIndex]
            encuestaAlimento = currentEncuesta // Actualizar encuestaAlimento
            // Actualizar la UI con la siguiente encuesta
            updateUIWithEncuestaAlimento(currentEncuesta)
        }
    }

    // Actualizar la interfaz con los datos de la encuestaAlimento.
    private fun updateUIWithEncuestaAlimento(encuestaAlimento: EncuestaAlimento) {
        // Observar los cambios en el alimento relacionado con la encuesta
        alimentoViewModel.fetchAlimentoByEncuestaAlimento(encuestaAlimento.encuestaAlimentoId).observe(viewLifecycleOwner) { alimento ->
            binding.textViewNameAlimento.text = alimento?.descripcion ?: "Descripción no disponible"
            val portions = DatosDatabase.portions.find { it.alimentoID == alimento?.alimentoId }?.portions ?: emptyList()
            setupSpinner(binding.spinnerPortion, portions, encuestaAlimento.portion)
        }

        // Configurar los spinners y establecer los valores por defecto
        setupSpinner(binding.spinnerPeriod, resources.getStringArray(R.array.Period).toList(), encuestaAlimento.period)
        editText.setText(encuestaAlimento.frecuency.toString())

        // Observadores para actualizar la base de datos cuando cambian los valores
        binding.spinnerPortion.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
                val selectedPortion = binding.spinnerPortion.selectedItem.toString()
                if (currentEncuesta.portion != selectedPortion) {
                    currentEncuesta.portion = selectedPortion
                    encuestaAlimentoViewModel.update(currentEncuesta)
                }
                Log.i("pruebas", currentEncuesta.toString())
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }

        binding.spinnerPeriod.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
                val selectedPeriod = binding.spinnerPeriod.selectedItem.toString()
                if (currentEncuesta.period != selectedPeriod) {
                    currentEncuesta.period = selectedPeriod
                    encuestaAlimentoViewModel.update(currentEncuesta)
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }

        editText.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                val frequency = s.toString().toIntOrNull() ?: 0
                if (currentEncuesta.frecuency != frequency) {
                    currentEncuesta.frecuency = frequency
                    encuestaAlimentoViewModel.update(currentEncuesta)
                }
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })
    }

    // Guardar los datos de la encuesta de alimentos en la base de datos.
    private fun saveEncuestaAlimento() {
        if (::currentEncuesta.isInitialized) {
            val selectedPortion = binding.spinnerPortion.selectedItem.toString()
            val selectedPeriod = binding.spinnerPeriod.selectedItem.toString()
            val frecuency = editText.text.toString().toIntOrNull() ?: 0

            currentEncuesta.portion = selectedPortion
            currentEncuesta.period = selectedPeriod
            currentEncuesta.frecuency = frecuency
            currentEncuesta.estado = "COMPLETADA"

            encuestaAlimentoViewModel.update(currentEncuesta)
        } else {
            Log.e("NuevaEncuestaAlimentoFragment", "La encuesta actual no está inicializada")
        }
    }

    // Configura un spinner con los elementos proporcionados y establece un valor por defecto si es necesario.
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
    }

    // Incrementa el valor de la frecuencia y actualiza el campo de texto.
    private fun increment() {
        valueFrecuency++
        editText.setText(valueFrecuency.toString())
    }

    // Decrementa el valor de la frecuencia y actualiza el campo de texto.
    private fun decrement() {
        if (valueFrecuency > 0) {
            valueFrecuency--
            editText.setText(valueFrecuency.toString())
        }
    }
}