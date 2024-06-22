package com.example.nutritnt.ui

import android.os.Bundle
import android.text.Editable
import android.text.SpannableString
import android.text.SpannableStringBuilder
import android.text.TextWatcher
import android.text.style.AbsoluteSizeSpan
import android.text.style.UnderlineSpan
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.CheckBox
import android.widget.EditText
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.Spinner
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.nutritnt.R
import com.example.nutritnt.data.DatosDatabase
import com.example.nutritnt.data.ImagesGroups
import com.example.nutritnt.data.Portion
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
    private lateinit var editTextFrecuency: EditText
    private lateinit var minusButton: ImageView
    private lateinit var plusButton: ImageView
    private var valueFrecuency: Int = 0

    private lateinit var encuestaAlimento: EncuestaAlimento
    private lateinit var encuestaGeneral: Encuesta
    private var currentIndex: Int = -1
    private lateinit var currentEncuesta: EncuestaAlimento
    private lateinit var todasLasEncuestas: List<EncuestaAlimento>
    private var alimentos: List<Alimento> = emptyList()
    private var isDataLoaded = false // Variable booleana para comprobar si el observable ya se ejecutó

    private lateinit var textviewPortionA: TextView
    private lateinit var imageviewPortionB: ImageView
    private lateinit var textviewPortionC: TextView
    private lateinit var imageViewPortionD: ImageView
    private lateinit var textviewPortionE: TextView
    private lateinit var framePortionA: FrameLayout
    private lateinit var framePortionB: FrameLayout
    private lateinit var framePortionC: FrameLayout
    private lateinit var framePortionD: FrameLayout
    private lateinit var framePortionE: FrameLayout
    private var selectedPortion: Char = ' '
    private var previousSelectedFrame: FrameLayout? = null
    private lateinit var checkBoxes: List<CheckBox>


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

        editTextFrecuency = view.findViewById(R.id.editText)
        minusButton = view.findViewById(R.id.minusButton)
        plusButton = view.findViewById(R.id.plusButton)

        // Configurar los botones de incremento y decremento
        minusButton.setOnClickListener { decrement() }
        plusButton.setOnClickListener { increment() }

        checkBoxes = listOf(
            binding.checkBoxNunca!!,
            binding.checkBoxDia!!,
            binding.checkBoxMes!!,
            binding.checkBoxSemana!!,
            binding.checkBoxAnio!!
        )

        // Set up listeners for each checkbox
        checkBoxes.forEach { checkBox ->
            checkBox?.setOnCheckedChangeListener { buttonView, isChecked ->
                if (isChecked) {
                    // Uncheck all other checkboxes
                    checkBoxes.filter { it != buttonView }.forEach { it?.isChecked = false }
                    Log.i("pruebaEncuestaAlimentoCopy", checkBox?.text.toString())
                    currentEncuesta.period = checkBox?.text.toString()
                    encuestaAlimentoViewModel.update(currentEncuesta)
                }
            }
        }


        val spannableA = SpannableStringBuilder()
        spannableA.append(
            createStyledText(
                "Opción A: ",
                "menos que B",
                underlinedSize = 18,
                regularSize = 14
            )
        )
        binding.textTitlePortionA?.text = spannableA

        val spannableB = SpannableStringBuilder()
        spannableB.append(
            createStyledText(
                "Opción B: ",
                " ",
                underlinedSize = 18,
                regularSize = 14
            )
        )
        binding.textTitlePortionB?.text = spannableB

        // Estilizar y asignar texto a textViewC
        val spannableC = SpannableStringBuilder()
        spannableC.append(
            createStyledText(
                "Opción C: ",
                "Entre B y D",
                underlinedSize = 18,
                regularSize = 14
            )
        )
        binding.textTitlePortionC?.text = spannableC

        val spannableD = SpannableStringBuilder()
        spannableD.append(
            createStyledText(
                "Opción D: ",
                " ",
                underlinedSize = 18,
                regularSize = 14
            )
        )
        binding.textTitlePortionD?.text = spannableD

        // Estilizar y asignar texto a textViewE
        val spannableE = SpannableStringBuilder()
        spannableE.append(
            createStyledText(
                "Opción E: ",
                "mayor que D",
                underlinedSize = 18,
                regularSize = 14
            )
        )
        binding.textTitlePortionE?.text = spannableE

        // Configurar las imágenes de las porciones
        textviewPortionA = binding.textTitlePortionA!!
        textviewPortionC = binding.textTitlePortionC!!
        textviewPortionE = binding.textTitlePortionE!!
        imageviewPortionB = binding.imageviewPortionB!!
        imageViewPortionD = binding.imageviewPortionD!!
        framePortionA = binding.framePortionA!!
        framePortionB = binding.framePortionB!!
        framePortionC = binding.framePortionC!!
        framePortionD = binding.framePortionD!!
        framePortionE = binding.framePortionE!!

        textviewPortionA.setOnClickListener {
            selectedPortion = 'A'
            highlightSelection(framePortionA, textviewPortionA)

        }

        imageviewPortionB.setOnClickListener {
            selectedPortion = 'B'
            highlightSelection(framePortionB, imageviewPortionB)
        }

        textviewPortionC.setOnClickListener {
            selectedPortion = 'C'
            highlightSelection(framePortionC, textviewPortionC)
        }

        imageViewPortionD.setOnClickListener {
            selectedPortion = 'D'
            highlightSelection(framePortionD, imageViewPortionD)
        }

        textviewPortionE.setOnClickListener {
            selectedPortion = 'E'
            highlightSelection(framePortionE, textviewPortionE)
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




        binding.textviewVolverListado?.setOnClickListener{
            findNavController().navigate(NuevaEncuestaAlimentoFragmentDirections.actionNewEncuestaFragmentToListEncuestasAlimentosFragment(encuestaAlimento.encuestaId))
        }
    }

    private fun createStyledText(
        underlinedText: String,
        regularText: String,
        underlinedSize: Int,
        regularSize: Int
    ): SpannableStringBuilder {
        val spannable = SpannableStringBuilder()

        val underlinedSpan = SpannableString(underlinedText).apply {
            setSpan(
                UnderlineSpan(),
                0,
                underlinedText.length,
                SpannableString.SPAN_EXCLUSIVE_EXCLUSIVE
            )
            setSpan(
                AbsoluteSizeSpan(underlinedSize, true),
                0,
                underlinedText.length,
                SpannableString.SPAN_EXCLUSIVE_EXCLUSIVE
            )
        }

        spannable.append(underlinedSpan)

        if (regularText.length > 2) {
            val regularSpan = SpannableString(regularText).apply {
                setSpan(
                    AbsoluteSizeSpan(regularSize, true),
                    0,
                    regularText.length,
                    SpannableString.SPAN_EXCLUSIVE_EXCLUSIVE
                )
            }

            spannable.append(regularSpan)
        }

        return spannable
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
            val nombreGrupo = if (alimento.subgrupo.isBlank()) alimento.grupo else alimento.subgrupo
            Log.i("pruebaEncuestaAlimentoCopy", "nombregrupo: ${nombreGrupo.lowercase()}")
            binding.imageviewTitle?.setImageResource(ImagesGroups.iconResourceMap[nombreGrupo.lowercase()]!!)
            binding.textViewNameAlimento.text = alimento.descripcion
            val portion = findPortionByAlimentoID(alimento.alimentoId)
            val frames = listOf(binding.framePortionA,binding.framePortionB, binding.framePortionC, binding.framePortionD, binding.framePortionE)

            updateFrecuencyEditText(0)
            deselectAllFrames(frames)
            deselectAllCheckBoxes()

            binding.textPortionB?.text = portion?.portions?.get('B') ?: "no existe"
            binding.textPortionD?.text = portion?.portions?.get('D') ?: "no existe"

            val images = DatosDatabase.getPortionImagesForAlimento(alimento.alimentoId)
            if (images.size >= 2) {
                imageviewPortionB.setImageResource(images[0])
                imageViewPortionD.setImageResource(images[1])
            }
        }


        editTextFrecuency.addTextChangedListener(object : TextWatcher {
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

    private fun updatePortionEncuesta(portionSelected: Char){
        val portion = findPortionByAlimentoID(currentEncuesta.alimentoId)?.portions?.get(portionSelected).toString()
        currentEncuesta.portion = extractNumber(portion).toString()
        encuestaAlimentoViewModel.update(currentEncuesta)
    }

    // Guardar los datos de la encuesta de alimentos en la base de datos.
   /* private fun saveEncuestaAlimento() {
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
*/
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
        editTextFrecuency.setText(frecuency.toString())
    }
    // Función para resaltar la selección de porción (imagen)
    private fun highlightSelection(selectedFrame: FrameLayout, selectedView: View) {
        imageviewPortionB.alpha = 0.5f
        imageViewPortionD.alpha = 0.5f
        selectedView.alpha = 1.0f

        previousSelectedFrame?.setBackgroundResource(R.drawable.default_background)

        selectedFrame.setBackgroundResource(R.drawable.border_portion_selected)

        updatePortionEncuesta(selectedPortion)

        previousSelectedFrame = selectedFrame
    }

    fun extractNumber(input: String): Int {
        val regex = Regex("^[0-9]+") // Expresión regular para encontrar solo los números al inicio del texto
        val matchResult = regex.find(input)
        return matchResult?.value?.toIntOrNull() ?: 0 // Devolver el valor encontrado como entero o 0 si no se encontraron números
    }

    fun findPortionByAlimentoID(alimentoID: Int): Portion? {
        return DatosDatabase.portions.find { it.alimentoID == alimentoID }
    }

    private fun deselectAllFrames(allFrames: List<FrameLayout?>) {
        // Reset all frames to default
        allFrames.forEach { frame ->
            frame?.setBackgroundResource(R.drawable.default_background)
        }

        selectedPortion = ' '
    }

    private fun deselectAllCheckBoxes() {
        checkBoxes.forEach { it.isChecked = false }
    }
}