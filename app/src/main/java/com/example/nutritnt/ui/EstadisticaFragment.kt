package com.example.nutritnt.model


import android.R
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.example.nutritnt.data.DatosConsumoGrasas
import com.example.nutritnt.databinding.FragmentEstadisticaBinding
import com.example.nutritnt.viewmodel.EncuestaAlimentoViewModel
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
import com.github.mikephil.charting.formatter.ValueFormatter
import com.github.mikephil.charting.utils.ColorTemplate
import kotlinx.coroutines.launch


class EstadisticaFragment : Fragment() {

    // ViewModel de EncuestaAlimento
    private val encuestaAlimentoViewModel: EncuestaAlimentoViewModel by viewModels()
    private var barChart: BarChart? = null
    private lateinit var binding: FragmentEstadisticaBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {

        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = FragmentEstadisticaBinding.inflate(inflater, container, false)
        val view = binding.root


        val zonas: List<String> = listOf(
            "Zona A",
            "Zona B",
            "Zona C",
            "Zona D"
        )

        barChart = view.findViewById(com.example.nutritnt.R.id.bar_chart)

        actualizarGrafico("dia", zonas)

        val spinner: Spinner = binding.spinnerPeriod
        val periods= listOf("Dia", "Semana", "Mes", "Año") // Ejemplo de lista de opciones para el selector
        val adapter = ArrayAdapter(requireContext(), R.layout.simple_spinner_item, periods)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner.adapter = adapter

        // Configurar el listener para el selector
        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                val periodSelected = periods[position]
                // llamada a la función para actualizar el gráfico con los datos del periodo seleccionado
                actualizarGrafico(periodSelected.lowercase(), zonas)
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                // No se seleccionó nada
            }
        }

        return view

    }

    private fun actualizarGrafico(periodo: String, zonas: List<String>){
        lifecycleScope.launch {
            // Recorrer el array de zonas
            var valorX = 0f
            var otroValorX = 0f
            val entries: MutableList<BarEntry> = ArrayList()
            var totalConsumoGrasas = 0f


            for (zona in zonas) {
                // Observar el flujo desde el ViewModel
                encuestaAlimentoViewModel.fetchEncuestaAlimentosByZonaAndAlimento(zona, 1).collect { datos ->
                    // Llenar el gráfico con los datos obtenidos

                    val totalPorZona: Float = DatosConsumoGrasas.obtenerDatosPorPeriodo(datos, periodo).toFloat()

                    Log.i("xmlEstadisticas", "zona " + zona + " total " + totalPorZona)

                    totalConsumoGrasas += totalPorZona

                    entries.add(BarEntry(valorX, totalPorZona))

                    valorX += 1f
                    otroValorX += 10f
                }
            }
            llenarGrafico(entries, zonas)
            "${"%.2f".format(totalConsumoGrasas)} gr".also { binding.textTotalConsumoGrasas.text = it }
        }
    }

    private fun llenarGrafico(entries: MutableList<BarEntry>, zonas: List<String>) {

        // Crear el conjunto de datos y configurar el gráfico
        val dataSet = BarDataSet(entries, "Datos de la zona")
        dataSet.colors = listOf(
            ColorTemplate.rgb("#4682B4"),  //blue
            ColorTemplate.rgb("#FFA07A"),  // naranjita
            ColorTemplate.rgb("#FF69B4"),  // rosita
            ColorTemplate.rgb("#9370DB")   // violetita
        )
        dataSet.valueTextSize = 12f
        dataSet.valueTextColor = android.graphics.Color.BLACK
        dataSet.valueFormatter = object : ValueFormatter() {
            override fun getBarLabel(barEntry: BarEntry?): String {
                return "%.2f".format(barEntry?.y)
            }
        }

        val barData = BarData(dataSet)
        barData.barWidth = 0.9f

        barChart?.setData(barData)
        barChart?.description?.isEnabled = false
        barChart?.xAxis?.position = XAxis.XAxisPosition.BOTTOM
        barChart?.xAxis?.setDrawGridLines(false)
        barChart?.xAxis?.granularity = 1f
        barChart?.axisLeft?.axisMinimum = 0f
        barChart?.axisRight?.isEnabled = false
       // barChart?.axisLeft?.granularity = 50f
        barChart?.axisLeft?.setDrawGridLines(true)
        barChart?.xAxis?.valueFormatter = IndexAxisValueFormatter(zonas)

        // Actualizar el gráfico
        barChart?.invalidate()
    }

}