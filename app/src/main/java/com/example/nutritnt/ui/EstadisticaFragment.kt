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
import androidx.navigation.fragment.findNavController
import com.example.nutritnt.data.DatosConsumoGrasas
import com.example.nutritnt.database.relations.EncuestaAlimento_AlimentoInformacionNutricional
import com.example.nutritnt.databinding.FragmentEstadisticaBinding
import com.example.nutritnt.viewmodel.AlimentoViewModel
import com.example.nutritnt.viewmodel.EncuestaAlimentoViewModel
import com.example.nutritnt.viewmodel.EncuestaViewModel
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.components.Legend
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
    private val encuestaViewModel: EncuestaViewModel by viewModels()
    private val alimentoViewModel: AlimentoViewModel by viewModels()
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

        barChart = view.findViewById(com.example.nutritnt.R.id.bar_chart)

        setupBarChart();
        loadBarChartData();

        //actualizarGrafico("dia")

        binding.buttonmap.setOnClickListener{
            findNavController().navigate(com.example.nutritnt.R.id.action_estadisticaFragment_to_estadisticaMapFragment)

        }

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
                            val totalPorZona: Float = DatosConsumoGrasas.obtenerDatosPorPeriodo(encuestas, periodo)
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
                llenarGrafico(entries, listadoZonas)
                "${"%.2f".format(totalConsumoGrasas)} gr".also { binding.textTotalConsumoGrasas.text = it }
            })


        }

        }

    private fun llenarGrafico(entries: MutableList<BarEntry>, zonas: List<String>) {
        Log.i("entries", entries.toString() + " zonas " + zonas)

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


    private fun setupBarChart() {
        barChart!!.setDrawBarShadow(false)
        barChart!!.setDrawValueAboveBar(true)
        barChart!!.description.isEnabled = false
        val xAxis = barChart!!.xAxis
        xAxis.position = XAxis.XAxisPosition.BOTTOM
        xAxis.setGranularity(1f)
        xAxis.setDrawGridLines(false)
        val legend = barChart!!.legend
        legend.verticalAlignment = Legend.LegendVerticalAlignment.TOP
        legend.horizontalAlignment = Legend.LegendHorizontalAlignment.LEFT
        legend.orientation = Legend.LegendOrientation.HORIZONTAL
        legend.setDrawInside(false)
        barChart!!.setDrawGridBackground(false)
    }

    private fun loadBarChartData() {
        val entriesZona1: MutableList<BarEntry> = ArrayList()
        val entriesZona2: MutableList<BarEntry> = ArrayList()
        val entriesZona3: MutableList<BarEntry> = ArrayList()

        // Datos de ejemplo
        entriesZona1.add(
            BarEntry(
                0f,
                floatArrayOf(50f, 30f, 70f)
            )
        ) // Frecuencia de consumo en Zona 1
        entriesZona2.add(
            BarEntry(
                1f,
                floatArrayOf(40f, 45f, 60f)
            )
        ) // Frecuencia de consumo en Zona 2
        entriesZona3.add(
            BarEntry(
                2f,
                floatArrayOf(60f, 50f, 55f)
            )
        ) // Frecuencia de consumo en Zona 3
        val set1 = BarDataSet(entriesZona1, "Zona 1")
        set1.setColors(Color.rgb(104, 241, 175), Color.rgb(164, 228, 251), Color.rgb(242, 247, 158))
        val set2 = BarDataSet(entriesZona2, "Zona 2")
        set2.setColors(Color.rgb(242, 247, 158), Color.rgb(104, 241, 175), Color.rgb(164, 228, 251))
        val set3 = BarDataSet(entriesZona3, "Zona 3")
        set3.setColors(Color.rgb(164, 228, 251), Color.rgb(242, 247, 158), Color.rgb(104, 241, 175))
        val data = BarData(set1, set2, set3)
        data.setValueTextSize(10f)
        data.barWidth = 0.3f // Definir el ancho de las barras
        val alimentos = arrayOf("Manzana", "Pan", "Leche")
        val xAxis = barChart!!.xAxis
        xAxis.valueFormatter = IndexAxisValueFormatter(alimentos)
        barChart!!.setData(data)
        barChart!!.groupBars(0.5f, 0.06f, 0.02f)
        barChart!!.invalidate() // Refrescar el gráfico
    }




}