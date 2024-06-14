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
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.nutritnt.R
import com.example.nutritnt.database.entities.Encuesta
import com.example.nutritnt.database.entities.EncuestaAlimento
import com.example.nutritnt.databinding.FragmentNuevaEncuestaAlimentoBinding
import com.example.nutritnt.viewmodel.EncuestaAlimentoViewModel
import com.example.nutritnt.viewmodel.EncuestaViewModel
import kotlinx.coroutines.launch

class NuevaEncuestaAlimentoFragment : Fragment() {

    private lateinit var binding: FragmentNuevaEncuestaAlimentoBinding
    private val encuestaAlimentoViewModel: EncuestaAlimentoViewModel by viewModels()
    private val encuestaViewModel: EncuestaViewModel by viewModels()

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

        // Obtener el código del participante pasado como argumento desde el fragmento anterior
        val id = args.encuestaAlimentoId
        Log.i("nik", "Encuesta alimento por args: "+id.toString())

        encuestaAlimentoViewModel.getEncuestaAlimentoById(id).observe(viewLifecycleOwner, Observer{
            this.encuestaAlimento = it
            Log.i("nik ", "Encuesta alimento obtenida!"+encuestaAlimento.encuestaAlimentoId.toString())
        })

//        // Observar los datos de Encuesta y EncuestaAlimento
//        encuestaViewModel.getEncuestaByCodigoParticipante(codigoParticipante).observe(viewLifecycleOwner, Observer { encuesta ->
//            this.encuesta = encuesta
//            Log.i("Muricion Encuesta", encuesta.encuestaId.toString())
//
//            // Una vez la encuesta está cargada y segura de que encuestaId está disponible, procedemos a cargar los EncuestaAlimento
//            encuestaAlimentoViewModel.getEncuestaAlimentoByEncuestaAndAlimento(encuesta.encuestaId, 1).observe(viewLifecycleOwner, Observer { encuestaAlimento ->
//                this.encuestaAlimento = encuestaAlimento
//                Log.i("Muricion Encuesta Alimento", encuestaAlimento.encuestaAlimentoId.toString())
//            })
//        })

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
