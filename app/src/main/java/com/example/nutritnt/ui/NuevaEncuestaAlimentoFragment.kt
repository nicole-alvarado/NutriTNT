package com.example.nutritnt.ui

import android.annotation.SuppressLint
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
import android.widget.LinearLayout
import android.widget.Spinner
import android.widget.TextView
import androidx.core.view.children
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.nutritnt.R
import com.example.nutritnt.data.DatosDatabase
import com.example.nutritnt.data.DatosFramesPortions
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
    private lateinit var frames: List<FrameLayout>
    private var selectedPeriod: String = ""


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

        editTextFrecuency = view.findViewById(com.example.nutritnt.R.id.editText)
        minusButton = view.findViewById(com.example.nutritnt.R.id.minusButton)
        plusButton = view.findViewById(com.example.nutritnt.R.id.plusButton)

        // Configurar los botones de incremento y decremento
        minusButton.setOnClickListener { decrement() }
        plusButton.setOnClickListener { increment() }
        frames = listOf(binding.framePortionA!!,binding.framePortionB!!, binding.framePortionC!!, binding.framePortionD!!, binding.framePortionE!!)


        checkBoxes = listOf(
            binding.checkBoxNunca!!,
            binding.checkBoxDia!!,
            binding.checkBoxMes!!,
            binding.checkBoxSemana!!,
            binding.checkBoxAnio!!
        )
        val overlayView = binding.overlayView
        // Set up listeners for each checkbox
        checkBoxes.forEach { checkBox ->
            checkBox?.setOnCheckedChangeListener { buttonView, isChecked ->
                if (isChecked) {

                    checkBoxes.filter { it != buttonView }.forEach { it?.isChecked = false }
                    // Verifica si el CheckBox seleccionado es el que dice "nunca"

                    verifyCheckboxSelected(checkBox.text.toString())

                    // Log y actualización para otros períodos diferentes a "nunca"
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

    private fun verifyCheckboxSelected(checkboxSelectedText: String) {

        if (checkboxSelectedText == "Nunca") {
            binding.overlayView?.visibility = View.VISIBLE
            binding.overlayView?.bringToFront()
            binding.overlayView?.setOnClickListener(null)
            deletePortionSelected()
            updateFrecuencyEditText(0)
        } else {
            // Uncheck all other checkboxes except the current one
            binding.overlayView?.visibility = View.GONE

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
        Log.i("NicoleHoy", encuestaAlimento.toString())
        saveEncuestaAlimento()
        if (currentIndex > 0) {
            currentIndex--
            currentEncuesta = todasLasEncuestas[currentIndex]
            encuestaAlimento = currentEncuesta // Actualizar encuestaAlimento
            updateUIWithEncuestaAlimento(encuestaAlimento)
        }
    }

    // Mostrar la siguiente encuesta de alimentos
    private fun showNextEncuesta() {
        Log.i("NicoleHoy", encuestaAlimento.toString())
        saveEncuestaAlimento();
        if (currentIndex < todasLasEncuestas.size - 1) {
            currentIndex++
            currentEncuesta = todasLasEncuestas[currentIndex]
            encuestaAlimento = currentEncuesta // Actualizar encuestaAlimento
            updateUIWithEncuestaAlimento(encuestaAlimento)
        }
    }

    // Actualizar la interfaz con los datos de la encuestaAlimento.
    @SuppressLint("SetTextI18n")
    private fun updateUIWithEncuestaAlimento(encuestaAlimento: EncuestaAlimento) {
        val alimento = alimentos.find { it.alimentoId == encuestaAlimento.alimentoId }
        if (alimento != null) {
            val nombreGrupo = if (alimento.subgrupo.isBlank()) alimento.grupo else alimento.subgrupo
            Log.i("pruebaEncuestaAlimentoCopy", "nombregrupo: ${nombreGrupo.lowercase()}")
            binding.imageviewTitle?.setImageResource(ImagesGroups.iconResourceMap[nombreGrupo.lowercase()]!!)
            binding.textViewNameAlimento.text = alimento.descripcion

            val portionsAlimento = findPortionByAlimentoID(alimento.alimentoId)
            val portion = encuestaAlimento.portion
            val invertedMap = invertMap(portionsAlimento?.portions)

            selectedPortion = invertedMap?.get(portion) ?: ' '


            selectCorrectCheckBoxes(encuestaAlimento.period)

            editTextFrecuency.setText(encuestaAlimento.frecuency.toString())


            binding.textPortionB?.text =
                (portionsAlimento?.portions?.get('B') + portionsAlimento?.medidaPortion)
            binding.textPortionD?.text = (portionsAlimento?.portions?.get('D') + portionsAlimento?.medidaPortion)


            val images = DatosDatabase.getPortionImagesForAlimento(alimento.alimentoId)
            if (images.size >= 2) {
                imageviewPortionB.setImageResource(images[0])
                imageViewPortionD.setImageResource(images[1])
            }


            frames = listOf(binding.framePortionA!!,binding.framePortionB!!, binding.framePortionC!!, binding.framePortionD!!, binding.framePortionE!!)
            selectPortionFrame(frames, selectedPortion)
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

    private fun saveEncuestaAlimento() {
        if (::currentEncuesta.isInitialized) {
            if (currentEncuesta.period == "Nunca" ||
                (currentEncuesta.portion != "" && currentEncuesta.period != "" && currentEncuesta.frecuency != 0)) {

                currentEncuesta.estado = "COMPLETADA"

                // Actualizar la encuesta en la base de datos
                encuestaAlimentoViewModel.update(currentEncuesta)

                // Verificar y actualizar el estado general de la encuesta si es necesario
                verificarEstados(currentEncuesta)
            } else {
                Log.e("NicoleHoy", "La encuesta actual no está completada")
            }
        } else {
            Log.e("NicoleHoy", "La encuesta actual no está inicializada correctamente")
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


    // Incrementa el valor de la frecuencia y actualiza el campo de texto.
    private fun increment() {
        valueFrecuency = currentEncuesta.frecuency
        valueFrecuency++
        updateFrecuencyEditText(valueFrecuency)
        currentEncuesta.frecuency = valueFrecuency
        encuestaAlimentoViewModel.update(currentEncuesta)
    }

    // Decrementa el valor de la frecuencia y actualiza el campo de texto.
    private fun decrement() {
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

       // modifyViewColorSelected(selectedFrame)

        previousSelectedFrame?.setBackgroundResource(com.example.nutritnt.R.drawable.default_background)

       // previousSelectedFrame?.let { modifyViewColorNotSelected(it) }

        selectedFrame.setBackgroundResource(com.example.nutritnt.R.drawable.border_portion_selected)

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

    private fun selectPortionFrame(allFrames: List<FrameLayout?>, charPortion: Char) {


        allFrames.forEach { frame ->

            frame?.let {
                val portionSelected = DatosFramesPortions.framePortionNames[charPortion]
                if(portionSelected?.let { it1 -> resources.getResourceName(frame.id).contains(it1) } == true){
                    frame.setBackgroundResource(com.example.nutritnt.R.drawable.border_portion_selected)
                    previousSelectedFrame = frame
                   // modifyViewColorSelected(frame)
                } else {
                    frame.setBackgroundResource(com.example.nutritnt.R.drawable.default_backgound_imgnoselected)
                 //   modifyViewColorNotSelected(frame)
                }
            }

        }


    }


    private fun deletePortionSelected() {
        frames.forEach { frame ->

            frame?.let {
                    frame.setBackgroundResource(com.example.nutritnt.R.drawable.default_background)
                   // modifyViewColorNotSelected(frame)
            }
        }
    }

    private fun selectCorrectCheckBoxes(periodSelected: String) {
        checkBoxes.forEach {
            it.isChecked = (it.text == periodSelected)
            Log.i("pruebaEncuestaAlimentoCopy", "checkbox: ${it.text.toString()}")
        }
        verifyCheckboxSelected(periodSelected)
    }

    fun <Char, String> invertMap(map: Map<Char, String>?): Map<String, Char>? {
        return map?.entries?.associate { (key, value) -> value to key }
    }


    private fun modifyViewColorSelected(frame: FrameLayout){

        val linearLayout: LinearLayout? = frame.getChildAt(0) as? LinearLayout

        linearLayout?.children?.forEach { view ->

                view.alpha = 1f

        }

    }

    private fun modifyViewColorNotSelected(frame: FrameLayout){

        val linearLayout: LinearLayout? = frame.getChildAt(0) as? LinearLayout

        linearLayout?.children?.forEach { view ->

            view.alpha = 0.5f

        }

    }
}