package com.example.nutritnt

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
import com.example.nutritnt.databinding.FragmentNewEncuestaBinding

class NewEncuestaFragment : Fragment() {

    private lateinit var binding: FragmentNewEncuestaBinding

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


        val picker1: NumberPicker = binding.numberPicker
        picker1.maxValue = 100
        picker1.minValue = 1

        // access the items of the list
        val portions = resources.getStringArray(R.array.Portion)

        // access the spinner
        val spinner_p = view.findViewById<Spinner>(R.id.spinner_portion)
        if (spinner_p != null) {
            val adapter = ArrayAdapter(
                requireContext(),
                android.R.layout.simple_spinner_item, portions
            )
            spinner_p.adapter = adapter

            spinner_p.onItemSelectedListener = object :
                AdapterView.OnItemSelectedListener{
                override fun onItemSelected(
                    parent: AdapterView<*>,
                    view: View, position: Int, id: Long
                ) {
                    Toast.makeText(
                        requireContext(),
                        getString(R.string.selected_item) + " " +
                                "" + portions[position], Toast.LENGTH_SHORT
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



        val frecuency = resources.getStringArray(R.array.Frecuency)

        // access the spinner
        val spinner_f = view.findViewById<Spinner>(R.id.spinner_frecuency)
        if (spinner_f != null) {
            val adapter = ArrayAdapter(
                requireContext(),
                android.R.layout.simple_spinner_item, frecuency
            )
            spinner_f.adapter = adapter

            spinner_f.onItemSelectedListener = object :
                AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>,
                    view: View, position: Int, id: Long
                ) {
                    Toast.makeText(
                        requireContext(),
                        getString(R.string.selected_item) + " " +
                                "" + frecuency[position], Toast.LENGTH_SHORT
                    ).show()
                }

                override fun onNothingSelected(parent: AdapterView<*>) {
                    // write code to perform some action
                }
            }
        }

        picker1.setOnValueChangedListener { picker, oldVal, newVal ->
            val valuePicker1 = picker1.value
            Log.d("picker value", valuePicker1.toString())
        }



        binding.buttonRegister.setOnClickListener {

            val portionPosition =   spinner_p.selectedItemPosition
            val valorPortionPosition = portions[portionPosition]

            val frecuencyPosition =   spinner_f.selectedItemPosition
            val valorFrecuencyPosition = frecuency[frecuencyPosition]

            val picker = picker1.value

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

}