package com.example.nutritnt.model


import android.R
import android.graphics.Color
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
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import com.example.nutritnt.data.DatosConsumo
import com.example.nutritnt.database.relations.EncuestaAlimento_AlimentoInformacionNutricional
import com.example.nutritnt.databinding.FragmentEstadisticaBinding
import com.example.nutritnt.viewmodel.AlimentoViewModel
import com.example.nutritnt.viewmodel.EncuestaAlimentoViewModel
import com.example.nutritnt.viewmodel.EncuestaViewModel
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import com.github.mikephil.charting.formatter.PercentFormatter
import com.github.mikephil.charting.formatter.ValueFormatter
import com.github.mikephil.charting.utils.ColorTemplate
import kotlinx.coroutines.launch


class EstadisticaFragment : Fragment() {

    // ViewModel de EncuestaAlimento
    private val encuestaAlimentoViewModel: EncuestaAlimentoViewModel by viewModels()
    private val encuestaViewModel: EncuestaViewModel by viewModels()
    private val alimentoViewModel: AlimentoViewModel by viewModels()
    private lateinit var pieChart: PieChart
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

        pieChart = view.findViewById(com.example.nutritnt.R.id.pie_chart)


        //actualizarGrafico("dia")

        val entries = mutableListOf<PieEntry>(
            PieEntry(40f, "Zona 1"),
            PieEntry(30f, "Zona 2"),
            PieEntry(20f, "Zona 3"),
            PieEntry(10f, "Zona 4")
        )

        val zonas = listOf("Zona 1", "Zona 2", "Zona 3", "Zona 4")
        llenarGrafico(entries, zonas)

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
               // actualizarGrafico(periodSelected.lowercase())
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                // No se seleccionó nada
            }
        }

        return view

    }

    private fun actualizarGrafico(periodo: String){

        lifecycleScope.launch {
            // Recorrer el array de zonas
            var valorX = 0f
            var otroValorX = 0f
            val entries: MutableList<BarEntry> = ArrayList()
            var totalConsumoGrasas = 0f


            alimentoViewModel.cargarAlimentosConInformacionNutricional()

            // Acceder a los datos cargados cuando sea necesario
            val alimentosConInfo = alimentoViewModel.alimentosConInfoNutricional

            val listadoZonas = encuestaViewModel.obtenerZonasDistintas()

            var observadorEjecutado = false

            // Llamar al método con la lista de zonas
            encuestaAlimentoViewModel.fetchEncuestasAlimentosConInfo(listadoZonas)

            // Observar el LiveData una vez
            encuestaAlimentoViewModel.encuestasAlimentosConInfo.observe(viewLifecycleOwner, Observer<Map<String, List<EncuestaAlimento_AlimentoInformacionNutricional>>> { dataMap ->
                if(!observadorEjecutado) {
                    dataMap?.let {
                        Log.i("xmlEstadisticas", listadoZonas.toString())
                        listadoZonas.forEach { zona ->
                            val encuestas = dataMap[zona] ?: emptyList()
                            val totalPorZona: Float = DatosConsumo.obtenerDatosPorPeriodo(encuestas, periodo)
                                .toFloat()

                            Log.i(
                                "xmlEstadisticas",
                                "valorX " + valorX + " zona " + zona + "listado" + encuestas.toString() + " total " + totalPorZona
                            )

                            totalConsumoGrasas += totalPorZona

                            entries.add(BarEntry(valorX, totalPorZona))



                            valorX += 1f
                            otroValorX += 10f
                        }
                    }
                }
                observadorEjecutado = true;
                Log.i("entries", entries.toString())
            //    llenarGrafico(entries, listadoZonas)
             //   "${"%.2f".format(totalConsumoGrasas)} gr".also { binding.textTotalConsumoGrasas.text = it }
            })


        }

        }

    private fun llenarGrafico(entries: MutableList<PieEntry>, zonas: List<String>) {
        Log.i("entries", entries.toString() + " zonas " + zonas)

        // Crear el conjunto de datos y configurar el gráfico
        val dataSet = PieDataSet(entries, "Datos de la zona")
        dataSet.colors = listOf(
            ColorTemplate.rgb("#4682B4"),  // azul
            ColorTemplate.rgb("#FFA07A"),  // naranjita
            ColorTemplate.rgb("#FF69B4"),  // rosita
            ColorTemplate.rgb("#9370DB")   // violetita
        )
        dataSet.valueTextSize = 12f
        dataSet.valueTextColor = Color.BLACK
        dataSet.valueFormatter = object : ValueFormatter() {
            override fun getPieLabel(value: Float, pieEntry: PieEntry?): String {
                return "%.2f".format(value)
            }
        }

        val pieData = PieData(dataSet)
        pieData.setValueTextSize(15f)
        pieData.setValueFormatter(PercentFormatter(pieChart))
        pieChart.setData(pieData)
        pieChart.isDrawHoleEnabled = false
        pieChart.description.isEnabled = false // Deshabilitar la descripción del gráfico
        pieChart.legend.isEnabled = false // Deshabilitar la leyenda
        pieChart.invalidate() // Actualiza el gráfico
    }




}