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
        binding = FragmentNuevaEncuestaAlimentoBinding.inflate(inflater, container, false)
        val view = binding.root

        editText = view.findViewById(R.id.editText)
        minusButton = view.findViewById(R.id.minusButton)
        plusButton = view.findViewById(R.id.plusButton)

        minusButton.setOnClickListener { decrement() }
        plusButton.setOnClickListener { increment() }

        encuestaAlimentoViewModel.getEncuestasAlimentosByEncuestaId(args.encuestaGeneralId).observe(viewLifecycleOwner, Observer { encuestas ->
            Log.i("Porfas", "En el Observable...")
            if (!isDataLoaded) {
                todasLasEncuestas = encuestas
                currentIndex = todasLasEncuestas.indexOfFirst { it.encuestaAlimentoId == args.encuestaGeneralId }
                if (currentIndex != -1) {
                    currentEncuesta = todasLasEncuestas[currentIndex]
                    encuestaAlimento = currentEncuesta
                    updateUIWithEncuestaAlimento(currentEncuesta)
                }
                isDataLoaded = true
            }
        })

        binding.buttonAnterior?.setOnClickListener { showPreviousEncuestaAlimento() }
        binding.buttonSiguiente?.setOnClickListener { showNextEncuestaAlimento() }
        binding.buttonRegistrar.setOnClickListener {
            saveEncuestaAlimento()
            findNavController().navigate(NuevaEncuestaAlimentoFragmentDirections.actionNewEncuestaFragmentToListEncuestasAlimentosFragment(encuestaAlimento.encuestaId))
        }

        return view
    }

    private fun showPreviousEncuestaAlimento() {
        if (currentIndex > 0) {
            currentIndex--
            currentEncuesta = todasLasEncuestas[currentIndex]
            encuestaAlimento = currentEncuesta
            updateUIWithEncuestaAlimento(currentEncuesta)
        }
    }

    private fun showNextEncuestaAlimento() {
        if (currentIndex < todasLasEncuestas.size - 1) {
            currentIndex++
            currentEncuesta = todasLasEncuestas[currentIndex]
            encuestaAlimento = currentEncuesta
            updateUIWithEncuestaAlimento(currentEncuesta)
        }
    }

    private fun updateUIWithEncuestaAlimento(encuestaAlimento: EncuestaAlimento) {
        Log.i("Porfas", encuestaAlimento.encuestaAlimentoId.toString())
        alimentoViewModel.fetchAlimentoByEncuestaAlimento(encuestaAlimento.encuestaAlimentoId).observe(viewLifecycleOwner, Observer { alimento ->
            binding.textViewNameAlimento.text = alimento?.descripcion ?: "Descripción no disponible"
            val portions = DatosDatabase.portions.find { it.alimentoID == alimento?.alimentoId }?.portions ?: emptyList()
            setupSpinner(binding.spinnerPortion, portions, encuestaAlimento.portion)
        })

        setupSpinner(binding.spinnerPeriod, resources.getStringArray(R.array.Period).toList(), encuestaAlimento.period)
        editText.setText(encuestaAlimento.frecuency.toString())

        setupListeners()
    }

    private fun setupListeners() {
        binding.spinnerPortion.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
                val selectedPortion = binding.spinnerPortion.selectedItem.toString()
                if (currentEncuesta.portion != selectedPortion) {
                    currentEncuesta.portion = selectedPortion
                    encuestaAlimentoViewModel.update(currentEncuesta)
                }
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