package com.example.nutritnt

import android.content.res.Configuration
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
import android.widget.NumberPicker
import android.widget.Spinner
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import com.example.nutritnt.databinding.FragmentNewEncuestaBinding
import com.example.nutritnt.viewmodel.NewEncuestaViewModel

class NewEncuestaFragment : Fragment() {

    private lateinit var binding: FragmentNewEncuestaBinding

    private lateinit var viewModelNewEncuesta: NewEncuestaViewModel


    private lateinit var editText: EditText
    private lateinit var minusButton: Button
    private lateinit var plusButton: Button
    private var value: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentNewEncuestaBinding.inflate(layoutInflater)
        val view = binding.root

        viewModelNewEncuesta = ViewModelProvider(this).get(NewEncuestaViewModel::class.java)

        // access the spinner
        val spinner_p = view.findViewById<Spinner>(R.id.spinner_portion)
        if (spinner_p != null) {
            val adapter_p = ArrayAdapter(
                requireContext(),
                android.R.layout.simple_spinner_item,
                viewModelNewEncuesta.portion.map { it.text }
            )

            binding.spinnerPortion.adapter = adapter_p

            spinner_p.onItemSelectedListener = object :
                AdapterView.OnItemSelectedListener{
                override fun onItemSelected(
                    parent: AdapterView<*>,
                    view: View, position: Int, id: Long
                ) {
                    Toast.makeText(
                        requireContext(),
                        getString(R.string.selected_item) + " " +
                                "" + viewModelNewEncuesta.portion[position].text, Toast.LENGTH_SHORT
                    ).show()
                }

                override fun onNothingSelected(parent: AdapterView<*>?) {
                    // No se seleccionó nada
                }
            }

            editText = view.findViewById(R.id.editText)
            minusButton = view.findViewById(R.id.minusButton)
            plusButton = view.findViewById(R.id.plusButton)

            // Configura los listeners para los botones
            minusButton.setOnClickListener {
                decrement()
            }

            plusButton.setOnClickListener {
                increment()
            }
        }

        // access the spinner
        val spinner_f = view.findViewById<Spinner>(R.id.spinner_frecuency)
        if (spinner_f != null) {
            val adapter_f = ArrayAdapter(
                requireContext(),
                android.R.layout.simple_spinner_item,
                viewModelNewEncuesta.period.map { it.text }
            )

            binding.spinnerFrecuency.adapter = adapter_f

            spinner_f.onItemSelectedListener = object :
                AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>,
                    view: View, position: Int, id: Long
                ) {
                    Toast.makeText(
                        requireContext(),
                        getString(R.string.selected_item) + " " +
                                "" + viewModelNewEncuesta.period[position].text, Toast.LENGTH_SHORT
                    ).show()
                }

                override fun onNothingSelected(parent: AdapterView<*>) {
                    // write code to perform some action
                }
            }
        }





        binding.buttonRegister.setOnClickListener {

            val portionPosition =   spinner_p.selectedItemPosition

            val frecuencyPosition =   spinner_f.selectedItemPosition

            Log.d("Botones", "Botón registrar clickeado")

            //findNavController().navigate(R.id.action_welcomeFragment_to_listEncuestasFragment)
            // findNavController().navigate(R.id.action_newEncuesta_to_detailEncuestaFragment)
        }



        return view
    }

    private fun increment() {
        value++
        editText.setText(value.toString())
    }

    private fun decrement() {
        if (value > 0) {
            value--
            editText.setText(value.toString())
        }
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        Log.i("holi","Hubo cambio de config")
        // Actualiza el diseño de tus vistas según la nueva configuración aquí
        // Por ejemplo, puedes cambiar la orientación de las vistas, ajustar márgenes, etc.
    }


}