package com.example.nutritnt.ui.prueba

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
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
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.nutritnt.R
import com.example.nutritnt.data.DatosDatabase
import com.example.nutritnt.database.entities.Encuesta
import com.example.nutritnt.database.entities.Encuesta_Alimento
import com.example.nutritnt.databinding.FragmentCopiaNuevaEncuestaAlimentoBinding
import com.example.nutritnt.databinding.FragmentNewEncuestaAlimentoBinding
import com.example.nutritnt.ui.NewEncuestaAlimentoFragmentArgs
import com.example.nutritnt.viewmodel.EncuestaAlimentoViewModel
import com.example.nutritnt.viewmodel.EncuestaViewModel
import kotlinx.coroutines.launch


class CopiaNuevaEncuestaAlimentoFragment : Fragment() {

    private lateinit var binding: FragmentCopiaNuevaEncuestaAlimentoBinding
    private val encuestaAlimentoViewModel: EncuestaAlimentoViewModel by viewModels()
    private val encuestaViewModel: EncuestaViewModel by viewModels()

    // Inicializar la variable para manejar los argumentos utilizando navArgs()
   // private val args: NewEncuestaAlimentoFragmentArgs by navArgs()

    private lateinit var editText: EditText
    private lateinit var minusButton: Button
    private lateinit var plusButton: Button
    private var valueFrecuency: Int = 0

    private lateinit var encuesta: Encuesta

    private lateinit var frameCucharaPequena: FrameLayout
    private lateinit var frameCucharaGrande: FrameLayout
    private lateinit var imageViewPortionPequena: ImageView
    private lateinit var imageViewPortionGrande: ImageView
    private var selectedPorcion: String = ""
    private var previousSelectedFrame: FrameLayout? = null



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentCopiaNuevaEncuestaAlimentoBinding.inflate(layoutInflater)
        val view = binding.root

        // Configurar los Spinners
      //  setupSpinner(binding.spinnerPortion, R.array.Portion)
        setupSpinner(binding.spinnerPeriod, R.array.Period)

        binding.cucharaPequena.setImageResource(DatosDatabase.portions[5].imgsPortions[0])
        binding.cucharaGrande.setImageResource(DatosDatabase.portions[5].imgsPortions[1])
        binding.textPotionPequena.text = DatosDatabase.portions[5].portions[0]
        binding.textPortionGrande.text = DatosDatabase.portions[5].portions[1]


        imageViewPortionPequena = binding.cucharaPequena
        imageViewPortionGrande = binding.cucharaGrande


        frameCucharaPequena = binding.frameCucharaPequena
        frameCucharaGrande = binding.frameCucharaGrande

        imageViewPortionPequena.setOnClickListener {
            selectedPorcion = "Cuchara pequeña"
            Toast.makeText(context, "Seleccionaste: $selectedPorcion portion: ${DatosDatabase.portions[0].portions[0]} ", Toast.LENGTH_SHORT).show()
            highlightSelection(frameCucharaPequena, imageViewPortionPequena)
        }

        imageViewPortionGrande.setOnClickListener {
            selectedPorcion = "Cuchara grande"
            Toast.makeText(context, "Seleccionaste: $selectedPorcion portion: \${DatosDatabase.portions[0].portions[1]", Toast.LENGTH_SHORT).show()
            highlightSelection(frameCucharaGrande, imageViewPortionGrande)
        }






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
      //  val codigoParticipante = args.codigoParticipante
      //  Log.i("Argumentos", codigoParticipante)

        // Lanzar una coroutine para llamar a la función suspendida
       /* viewLifecycleOwner.lifecycleScope.launch {
            encuestaViewModel.getEncuestaByCodigoParticipante("pruebita").observe(viewLifecycleOwner) { encuesta ->
                this@CopiaNuevaEncuestaAlimentoFragment.encuesta = encuesta
            }
        }*/

        binding.buttonRegistrar.setOnClickListener {
            // Obtener los valores seleccionados de los Spinners y el texto ingresado en el EditText
          //  val selectedPortion = extractNumber(binding.spinnerPortion.selectedItem.toString())
            val selectedPeriod = binding.spinnerPeriod.selectedItem.toString()
            val frecuency = editText.text.toString().toIntOrNull() ?: 0

            Log.d("Botones", "Botón registrar clickeado")

            // Crear objeto Encuesta_Alimento con los valores seleccionados
            val nuevaEncuestaAlimento = Encuesta_Alimento(
                portion = "",//selectedPortion.toString(),
                period = selectedPeriod,
                frecuency = frecuency,
                encuestaId = encuesta.encuestaId, // ID temporal, debemos asignarle el id correcto de una encuesta
                alimentoId = 1,
                estado = "Finalizada"
            )

            // Insertar nueva encuesta de alimento en la base de datos a través del ViewModel
            encuestaAlimentoViewModel.insert(nuevaEncuestaAlimento)

            // Actualizar el estado de la encuesta
            encuesta.estado = "FINALIZADA"
            encuestaViewModel.update(encuesta)

            findNavController().navigate(R.id.action_newEncuestaFragment_to_listEncuestasAlimentosFragment)
        }

        return view
    }



    private fun highlightSelection(selectedFrame: FrameLayout, selectedImageView: ImageView) {
        imageViewPortionPequena.alpha = 0.5f
        imageViewPortionGrande.alpha = 0.5f
        selectedImageView.alpha = 1.0f

        // Reset the background of the previously selected FrameLayout
        previousSelectedFrame?.setBackgroundResource(R.drawable.default_background)

        // Set the background of the currently selected FrameLayout
        selectedFrame.setBackgroundResource(R.drawable.border)

        // Update the reference to the currently selected FrameLayout
        previousSelectedFrame = selectedFrame
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
                    //Toast.makeText(requireContext(), getString(R.string.selected_item) + " " + selectedOption, Toast.LENGTH_SHORT).show()
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