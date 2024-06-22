package com.example.nutritnt.ui.prueba

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
import android.widget.EditText
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.example.nutritnt.R
import com.example.nutritnt.data.DatosDatabase
import com.example.nutritnt.data.DatosDatabase.Companion.portions
import com.example.nutritnt.data.ImagesGroups
import com.example.nutritnt.data.Portion
import com.example.nutritnt.database.entities.Alimento
import com.example.nutritnt.database.entities.EncuestaAlimento
import com.example.nutritnt.databinding.FragmentCopiaNuevaEncuestaAlimentoBinding
import com.example.nutritnt.viewmodel.AlimentoViewModel
import com.example.nutritnt.viewmodel.EncuestaAlimentoViewModel


class CopiaNuevaEncuestaAlimentoFragment : Fragment() {

    private lateinit var binding: FragmentCopiaNuevaEncuestaAlimentoBinding
    private val encuestaAlimentoViewModel: EncuestaAlimentoViewModel by viewModels()
    private val alimentoViewModel: AlimentoViewModel by viewModels()

    //  private val args: CopiaNuevaEncuestaAlimentoFragmentArgs by navArgs()
    private lateinit var editText: EditText
    private lateinit var minusButton: ImageView
    private lateinit var plusButton: ImageView
    private var valueFrecuency: Int = 0

    private lateinit var encuestaAlimento: EncuestaAlimento
    private var currentIndex: Int = -1
    private lateinit var currentEncuesta: EncuestaAlimento
    private lateinit var todasLasEncuestas: List<EncuestaAlimento>
    private var alimentos: List<Alimento> = emptyList()
    private var isDataLoaded =
        false // Variable booleana para comprobar si el observable ya se ejecutó

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


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflar el layout del fragmento utilizando ViewBinding
        binding = FragmentCopiaNuevaEncuestaAlimentoBinding.inflate(layoutInflater)
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

        // List of all checkboxes
        val checkBoxes = listOf(
            binding.checkBoxNunca,
            binding.checkBoxDia,
            binding.checkBoxMes,
            binding.checkBoxSemana,
            binding.checkBoxAnio
        )

        // Set up listeners for each checkbox
        checkBoxes.forEach { checkBox ->
            checkBox.setOnCheckedChangeListener { buttonView, isChecked ->
                if (isChecked) {
                    // Uncheck all other checkboxes
                    checkBoxes.filter { it != buttonView }.forEach { it.isChecked = false }
                    Log.i("pruebaEncuestaAlimentoCopy", checkBox.text.toString())
                    currentEncuesta.period = checkBox.text.toString()
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
        binding.textTitlePortionA.text = spannableA

        val spannableB = SpannableStringBuilder()
        spannableB.append(
            createStyledText(
                "Opción B: ",
                " ",
                underlinedSize = 18,
                regularSize = 14
            )
        )
        binding.textTitlePortionB.text = spannableB

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
        binding.textTitlePortionC.text = spannableC

        val spannableD = SpannableStringBuilder()
        spannableD.append(
            createStyledText(
                "Opción D: ",
                " ",
                underlinedSize = 18,
                regularSize = 14
            )
        )
        binding.textTitlePortionD.text = spannableD

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
        binding.textTitlePortionE.text = spannableE

        // Configurar las imágenes de las porciones
        textviewPortionA = binding.textTitlePortionA
        textviewPortionC = binding.textTitlePortionC
        textviewPortionE = binding.textTitlePortionE
        imageviewPortionB = binding.imageviewPortionB
        imageViewPortionD = binding.imageviewPortionD
        framePortionA = binding.framePortionA
        framePortionB = binding.framePortionB
        framePortionC = binding.framePortionC
        framePortionD = binding.framePortionD
        framePortionE = binding.framePortionE

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
        val encuestaAlimentoId = 1

        alimentoViewModel.todosLosAlimentos.observe(viewLifecycleOwner, Observer { alimentosList ->
            alimentos = alimentosList
            encuestaAlimentoViewModel.getEncuestasAlimentosByEncuestaId(1)
                .observe(viewLifecycleOwner, Observer { encuestas ->
                    if (!isDataLoaded) {
                        todasLasEncuestas = encuestas
                        currentIndex =
                            todasLasEncuestas.indexOfFirst { it.encuestaAlimentoId == encuestaAlimentoId }
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
        binding.buttonAnterior.setOnClickListener { showPreviousEncuesta() }
        binding.buttonSiguiente.setOnClickListener { showNextEncuesta() }

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
            binding.imageviewTitle.setImageResource(ImagesGroups.iconResourceMap[nombreGrupo.lowercase()]!!)
            binding.textViewNameAlimento.text = alimento.descripcion
            val portion = findPortionByAlimentoID(alimento.alimentoId)

            binding.textPortionB.text = portion?.portions?.get('B') ?: "no existe"
            binding.textPortionD.text = portion?.portions?.get('D') ?: "no existe"

            val images = DatosDatabase.getPortionImagesForAlimento(alimento.alimentoId)
            if (images.size >= 2) {
                imageviewPortionB.setImageResource(images[0])
                imageViewPortionD.setImageResource(images[1])
            }
        }

        editText.setText(encuestaAlimento.frecuency.toString())


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

    private fun updatePortionEncuesta(portionSelected: Char){
        val portion = findPortionByAlimentoID(currentEncuesta.alimentoId)?.portions?.get(portionSelected).toString()
        currentEncuesta.portion = extractNumber(portion).toString()
        encuestaAlimentoViewModel.update(currentEncuesta)
    }

    // Incrementa el valor de la frecuencia y actualiza el campo de texto.
    private fun increment() {
        Log.i(
            "alvaraIncrement",
            "currentEncuestaId: " + currentEncuesta.encuestaAlimentoId.toString()
        )
        valueFrecuency = currentEncuesta.frecuency
        valueFrecuency++
        updateFrecuencyEditText(valueFrecuency)
        currentEncuesta.frecuency = valueFrecuency
        encuestaAlimentoViewModel.update(currentEncuesta)
    }

    // Decrementa el valor de la frecuencia y actualiza el campo de texto.
    private fun decrement() {
        Log.i(
            "alvaraDecrement",
            "currentEncuestaId: " + currentEncuesta.encuestaAlimentoId.toString()
        )
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
        val regex =
            Regex("^[0-9]+") // Expresión regular para encontrar solo los números al inicio del texto
        val matchResult = regex.find(input)
        return matchResult?.value?.toIntOrNull()
            ?: 0 // Devolver el valor encontrado como entero o 0 si no se encontraron números
    }


    fun findPortionByAlimentoID(alimentoID: Int): Portion? {
        return portions.find { it.alimentoID == alimentoID }
    }


}