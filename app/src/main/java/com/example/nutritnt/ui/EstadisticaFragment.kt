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
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.selection.toggleable
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.unit.toSize
import androidx.compose.ui.viewinterop.AndroidView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import com.example.nutritnt.data.DatosConsumoGrasas
import com.example.nutritnt.database.entities.Encuesta_Alimento
import com.example.nutritnt.databinding.FragmentDetailEncuestaBinding
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
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch


data class PorcentajeZonas(val zona: String, val variable: String, val total: Int)

data class ConsultaZonaPeriodo(val zona: Int, val periodo: String)

data class GrasasTotalesZona (val zona: String, var grasasTotales: Float)


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
        // Aquí puedes procesar los datos y llenar el gráfico de barras
        // Por ejemplo, crear las entradas para el gráfico



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

    @Composable
    fun generalStatistics(){
        var periodoSeleccionado by remember { mutableStateOf("dia") }
        Box(
            modifier = Modifier.fillMaxSize()
        ) {
         //   Image(
            //    painter = painterResource(id = R.drawable.background_estadisticas),
         //       contentDescription = null,
          //      modifier = Modifier.fillMaxSize(),
           //     contentScale = ContentScale.Crop // Escalar la imagen para llenar el Box
            //)
            Column(
                modifier = Modifier.verticalScroll(rememberScrollState()),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Grasas Totales",
                    fontSize = 24.sp,
                    modifier = Modifier.padding(top = 76.dp)
                )

               selectorPeriodo { periodo ->
                    periodoSeleccionado = periodo
                }


                // Llama a la función que dibuja el gráfico pasando el periodo seleccionado
                dibujoBarras(periodoSeleccionado)

            }
        }
    }

    @Composable
    fun dibujoBarras(periodo: String) {


        val zonas: List<String> = listOf(
            "Zona A",
            "Zona B",
            "Zona C",
            "Zona D"
        )


        var contador = 0f


        val barChartData = remember { mutableStateListOf<BarEntry>() }

        var contadorGrasasTotales by remember { mutableStateOf(0f) }

        var encuestaAlimentosMap by remember { mutableStateOf(emptyMap<String, List<Encuesta_Alimento>>()) }
        var barData by remember { mutableStateOf<BarData?>(null) } // Estado para almacenar los datos del gráfico

        // Llama al Composable que contiene el gráfico dentro de LaunchedEffect
        LaunchedEffect(Unit) {
            val map = mutableMapOf<String, List<Encuesta_Alimento>>()
            zonas.forEach { zona ->
                val encuestas = encuestaAlimentoViewModel.fetchEncuestaAlimentosByZonaAndAlimento(zona, 1)
                    .firstOrNull() ?: emptyList() // Maneja el caso de que no haya datos
                map[zona] = encuestas
            }
            encuestaAlimentosMap = map

            // Configura los datos del gráfico
            val barChartData = ArrayList<BarEntry>()
            var contador = 0f
            encuestaAlimentosMap.forEach { (zona, encuestaAlimentos) ->
                val datos: Float = DatosConsumoGrasas.obtenerDatosPorPeriodo(encuestaAlimentos, periodo).toFloat()
                barChartData.add(BarEntry(contador, datos))
                contador += 1f
            }

            val barDataSet = BarDataSet(barChartData, "Sample Data")
            barDataSet.colors = listOf(
                ColorTemplate.rgb("#4682B4"),  //blue
                ColorTemplate.rgb("#FFA07A"),  // naranjita
                ColorTemplate.rgb("#FF69B4"),  // rosita
                ColorTemplate.rgb("#9370DB")   // violetita
            )
            barDataSet.valueTextSize = 12f
            barDataSet.valueTextColor = android.graphics.Color.BLACK
            barDataSet.valueFormatter = object : ValueFormatter() {
                override fun getBarLabel(barEntry: BarEntry?): String {
                    return "%.2f".format(barEntry?.y)
                }
            }

            val data = BarData(barDataSet)
            data.barWidth = 0.9f
            barData = data
        }

        // Llama al Composable principal y pasa los datos del gráfico como parámetro
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top
        ) {
            AndroidView(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(300.dp),
                factory = { context ->
                    com.github.mikephil.charting.charts.BarChart(context).apply {
                        this.data = barData // Asigna los datos del gráfico
                        this.description.isEnabled = false
                        this.xAxis.position = XAxis.XAxisPosition.BOTTOM
                        this.xAxis.setDrawGridLines(false)
                        this.xAxis.granularity = 1f
                        this.axisLeft.axisMinimum = 0f
                        this.axisLeft.granularity = 50f
                        this.axisLeft.setDrawGridLines(true)
                        this.axisLeft.valueFormatter = object : ValueFormatter() {
                            override fun getFormattedValue(value: Float): String {
                                return "%.2f".format(value)
                            }
                        }
                        this.axisRight.isEnabled = false
                        this.invalidate()
                    }
                }
            )
        }
    }










    var colors = mutableListOf(
        Color(0xFFFF5733),
        Color(0xFFFFC300),
        Color(0xFFFFD700),
        Color(0xFF7FFF00),
        Color(0xFF32CD32),
        Color(0xFF4682B4),
        Color(0xFF4169E1),
        Color(0xFF6495ED),
        Color(0xFF00BFFF),
        Color(0xFF87CEEB),
        Color(0xFF87CEFA),
        Color(0xFFFFA07A),
        Color(0xFFFFB6C1),
        Color(0xFFFF69B4),
        Color(0xFFFF1493),
        Color(0xFFDB7093),
        Color(0xFF9370DB),
        Color(0xFFBA55D3),
        Color(0xFF8A2BE2),
        Color(0xFF4B0082)
    )

    fun randomColor(): Color {
        val randomIndex = (Math.random() * colors.size).toInt()
        val color = colors[randomIndex]

        colors.removeAt(randomIndex)
        return color
    }




    @Composable
    fun selectorPeriodo(onPeriodoSeleccionado: (String) -> Unit) {

        var expanded by remember { mutableStateOf(false) }
        val suggestions = listOf("Dia", "Semana", "Mes", "Año")
        var selectedText by remember { mutableStateOf("dia") }


        var textfieldSize by remember { mutableStateOf(Size.Zero)}

        val icon = if (expanded)
            Icons.Filled.KeyboardArrowUp
        else
            Icons.Filled.KeyboardArrowDown

        val onPeriodoItemClick: (String) -> Unit = { periodo ->
            selectedText = periodo
            expanded = false
            onPeriodoSeleccionado(periodo) // Llama a la lambda con el periodo seleccionado
        }


        Column(
            Modifier
                .padding(20.dp)
                .clickable { expanded = !expanded }) {
            OutlinedTextField(
                value = selectedText,
                onValueChange = { selectedText = it },
                readOnly = true,
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { expanded = !expanded }
                    .onGloballyPositioned { coordinates ->
                        //This value is used to assign to the DropDown the same width
                        textfieldSize = coordinates.size.toSize()
                    },
                label = {Text("Periodo")},
                trailingIcon = {
                    Icon(icon,"contentDescription",
                        Modifier.clickable { expanded = !expanded })
                }
            )
            DropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false },
                modifier = Modifier
                    .toggleable(
                        value = expanded,
                        onValueChange = { expanded = !expanded }
                    )
                    .width(with(LocalDensity.current) { textfieldSize.width.toDp() })
            ) {
                suggestions.forEach { label ->
                    DropdownMenuItem(onClick = {
                        selectedText = label
                        expanded = false
                        onPeriodoItemClick(label.lowercase())
                    }, text = { Text(text = label)})
                }
            }

        }
    }



}