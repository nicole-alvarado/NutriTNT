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
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.Spinner
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.nutritnt.R
import com.example.nutritnt.data.DatosDatabase
import com.example.nutritnt.database.entities.Alimento
import com.example.nutritnt.database.entities.Encuesta
import com.example.nutritnt.database.entities.EncuestaAlimento
import com.example.nutritnt.databinding.FragmentNuevaEncuestaAlimentoBinding
import com.example.nutritnt.viewmodel.AlimentoViewModel
import com.example.nutritnt.viewmodel.EncuestaAlimentoViewModel
import com.example.nutritnt.viewmodel.EncuestaViewModel

class NuevaEncuestaAlimentoFragment : Fragment() {

    private lateinit var binding: FragmentNuevaEncuestaAlimentoBinding
    private val encuestaAlimentoViewModel: EncuestaAlimentoViewModel by viewModels()
    private val alimentoViewModel: AlimentoViewModel by viewModels()
    private val encuestaViewModel: EncuestaViewModel by viewModels()


    private val args: NuevaEncuestaAlimentoFragmentArgs by navArgs()
    private lateinit var editText: EditText
    private lateinit var minusButton: Button
    private lateinit var plusButton: Button
    private var valueFrecuency: Int = 0

    private lateinit var encuestaAlimento: EncuestaAlimento
    private lateinit var encuestaGeneral: Encuesta
    private var currentIndex: Int = -1
    private lateinit var currentEncuesta: EncuestaAlimento
    private lateinit var todasLasEncuestas: List<EncuestaAlimento>
    private var alimentos: List<Alimento> = emptyList()
    private var isDataLoaded = false // Variable booleana para comprobar si el observable ya se ejecutó

    private lateinit var imageViewPortionSmall: ImageView
    private lateinit var imageViewPortionLarge: ImageView
    private lateinit var frameLayoutSmall: FrameLayout
    private lateinit var frameLayoutLarge: FrameLayout
    private var selectedPortion: String = ""
    private var previousSelectedFrame: FrameLayout? = null


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflar el layout del fragmento utilizando ViewBinding
        binding = FragmentNuevaEncuestaAlimentoBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        editText = view.findViewById(R.id.editText)
        minusButton = view.findViewById(R.id.minusButton)
        plusButton = view.findViewById(R.id.plusButton)

        // Configurar los botones de incremento y decremento
        minusButton.setOnClickListener { decrement() }
        plusButton.setOnClickListener { increment() }

        // Configurar las imágenes de las porciones
        imageViewPortionSmall = binding.cucharaPequena!!
        imageViewPortionLarge = binding.cucharaGrande!!
        frameLayoutSmall = binding.frameCucharaPequena!!
        frameLayoutLarge = binding.frameCucharaGrande!!

        imageViewPortionSmall.setOnClickListener {
            selectedPortion = "Cuchara pequeña"
            highlightSelection(frameLayoutSmall, imageViewPortionSmall)
        }

        imageViewPortionLarge.setOnClickListener {
            selectedPortion = "Cuchara grande"
            highlightSelection(frameLayoutLarge, imageViewPortionLarge)
        }

        // Obtener el id de la encuestaAlimento pasado al fragmento
        val encuestaAlimentoId = args.encuestaAlimentoId

        // Obtener la encuesta general desde un principio
        encuestaViewModel.getEncuestaById(args.encuestaGeneralId).observe(viewLifecycleOwner, Observer { encuesta ->
            encuestaGeneral = encuesta
            Log.i("PruebaVerificar", encuesta.toString())
        })

        alimentoViewModel.todosLosAlimentos.observe(viewLifecycleOwner, Observer { alimentosList ->
            alimentos = alimentosList
            encuestaAlimentoViewModel.getEncuestasAlimentosByEncuestaId(args.encuestaGeneralId).observe(viewLifecycleOwner, Observer { encuestas ->
                if (!isDataLoaded) {
                    todasLasEncuestas = encuestas
                    currentIndex = todasLasEncuestas.indexOfFirst { it.encuestaAlimentoId == encuestaAlimentoId }
                    if (currentIndex != -1) {
                        currentEncuesta = todasLasEncuestas[currentIndex]
                        encuestaAlimento = currentEncuesta
                        updateUIWithEncuestaAlimento(currentEncuesta)
                    }
                    isDataLoaded = true
                }
            })
        })

        // Configurar los botones "Anterior" y "Siguiente"
        binding.buttonAnterior?.setOnClickListener { showPreviousEncuesta() }
        binding.buttonSiguiente?.setOnClickListener { showNextEncuesta() }

        // Botón para guardar la encuestaAlimento y navegar al listado de encuestas alimentos
        binding.buttonRegistrar.setOnClickListener {
            saveEncuestaAlimento()
            Toast.makeText(context, "¡Guardado con éxito!", Toast.LENGTH_SHORT).show()
        }

        binding.buttonVolverListado?.setOnClickListener{
            findNavController().navigate(NuevaEncuestaAlimentoFragmentDirections.actionNewEncuestaFragmentToListEncuestasAlimentosFragment(encuestaAlimento.encuestaId))
        }
    }

    // Mostrar la encuesta de alimentos anterior
    private fun showPreviousEncuesta() {
        if (currentIndex > 0) {
            currentIndex--
            currentEncuesta = todasLasEncuestas[currentIndex]
            encuestaAlimento = currentEncuesta // Actualizar encuestaAlimento
            updateUIWithEncuestaAlimento(encuestaAlimento)
        }
    }

    // Mostrar la siguiente encuesta de alimentos
    private fun showNextEncuesta() {
        if (currentIndex < todasLasEncuestas.size - 1) {
            currentIndex++
            currentEncuesta = todasLasEncuestas[currentIndex]
            encuestaAlimento = currentEncuesta // Actualizar encuestaAlimento
            updateUIWithEncuestaAlimento(encuestaAlimento)
        }
    }

    // Actualizar la interfaz con los datos de la encuestaAlimento.
    private fun updateUIWithEncuestaAlimento(encuestaAlimento: EncuestaAlimento) {
        val alimento = alimentos.find { it.alimentoId == encuestaAlimento.alimentoId }
        if (alimento != null) {
            binding.textViewNameAlimento.text = alimento.descripcion
            val images = DatosDatabase.getPortionImagesForAlimento(alimento.alimentoId)
            if (images.size >= 2) {
                imageViewPortionSmall.setImageResource(images[0])
                imageViewPortionLarge.setImageResource(images[1])
            }
        }

        setupSpinner(binding.spinnerPeriod, resources.getStringArray(R.array.Period).toList(), encuestaAlimento.period)
        editText.setText(encuestaAlimento.frecuency.toString())

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
            val images = DatosDatabase.getPortionImagesForAlimento(currentEncuesta.alimentoId)
            val index = if (selectedPortion == "Cuchara pequeña") 0 else 1 // Asumiendo que las imágenes están en orden de tamaño

            // Obtener la descripción de la porción seleccionada (por ejemplo, "5gr", "15gr")
            val portionDescription = DatosDatabase.portions.find { it.imgsPortions[index] == images[index] }?.portions?.get('A')

            // Extraer el número de la descripción de la porción
            val portionNumber = portionDescription?.let { extractNumber(it) }

            currentEncuesta.portion =
                (portionNumber ?: 0).toString() // Asignar el valor numérico de la porción seleccionada

            currentEncuesta.period = binding.spinnerPeriod.selectedItem.toString()
            currentEncuesta.frecuency = extractNumber(editText.text.toString()) // Obtener el número de frecuencia desde el EditText
            currentEncuesta.estado = "COMPLETADA"
            encuestaAlimentoViewModel.update(currentEncuesta)

            // Llamar a la función para verificar y finalizar la encuesta general si es necesario
            verificarEstados(currentEncuesta)

        } else {
            Log.e("NuevaEncuestaAlimentoFragment", "La encuesta actual no está inicializada")
        }
    }

    // Verificar y actualizar el estado de las encuestas
    private fun verificarEstados(encuestaAlimento: EncuestaAlimento) {
        Log.i("PruebaVerificar", "Entro a verificar 1")
        Log.i("PruebaVerificar", encuestaAlimento.toString())

        // Usar todasLasEncuestas que ya has cargado
        todasLasEncuestas?.let { encuestasAlimentos ->
            Log.i("PruebaVerificar", encuestasAlimentos.toString())
            if (encuestasAlimentos.all { it.estado == "COMPLETADA" }) {
                Log.i("PruebaVerificar", "Entro a verificar 2")
                updateEncuestaEstado()
            }
        }
    }


    // Actualizar el estado de la encuesta
    private fun updateEncuestaEstado() {
            encuestaGeneral.estado = "FINALIZADA"
            encuestaViewModel.update(encuestaGeneral)
            Log.i("PruebaVerificar", "Estado actualizado a FINALIZADA")
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

        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
                // Aquí puedes manejar la lógica de selección del spinner si es necesario
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                // No se seleccionó nada en el spinner
            }
        }
    }

    // Incrementa el valor de la frecuencia y actualiza el campo de texto.
    private fun increment() {
        Log.i("alvaraIncrement", "currentEncuestaId: " + currentEncuesta.encuestaAlimentoId.toString())
        valueFrecuency = currentEncuesta.frecuency
        valueFrecuency++
        updateFrecuencyEditText(valueFrecuency)
        currentEncuesta.frecuency = valueFrecuency
        encuestaAlimentoViewModel.update(currentEncuesta)
    }

    // Decrementa el valor de la frecuencia y actualiza el campo de texto.
    private fun decrement() {
        Log.i("alvaraDecrement", "currentEncuestaId: " + currentEncuesta.encuestaAlimentoId.toString())
        valueFrecuency = currentEncuesta.frecuency
        if (valueFrecuency > 0) {
            valueFrecuency--
            updateFrecuencyEditText(valueFrecuency)
            currentEncuesta.frecuency = valueFrecuency
            encuestaAlimentoViewModel.update(currentEncuesta)
        }
    }

    private fun updateFrecuencyEditText(frecuency: Int) {
        editText.setText(frecuency.toString())
    }
    // Función para resaltar la selección de porción (imagen)
    private fun highlightSelection(selectedFrame: FrameLayout, selectedImageView: ImageView) {
        imageViewPortionSmall.alpha = 0.5f
        imageViewPortionLarge.alpha = 0.5f
        selectedImageView.alpha = 1.0f

        previousSelectedFrame?.setBackgroundResource(R.drawable.default_background)
        selectedFrame.setBackgroundResource(R.drawable.border_portion_selected)

        previousSelectedFrame = selectedFrame
    }

    fun extractNumber(input: String): Int {
        val regex = Regex("^[0-9]+") // Expresión regular para encontrar solo los números al inicio del texto
        val matchResult = regex.find(input)
        return matchResult?.value?.toIntOrNull() ?: 0 // Devolver el valor encontrado como entero o 0 si no se encontraron números
    }
}