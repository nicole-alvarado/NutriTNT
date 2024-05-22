package com.example.nutritnt.model

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.selection.toggleable
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.unit.toSize
import androidx.fragment.app.viewModels
import com.example.nutritnt.R
import com.example.nutritnt.data.DatosConsumoGrasas
import com.example.nutritnt.viewmodel.EncuestaAlimentoViewModel
import com.github.tehras.charts.bar.BarChart
import com.github.tehras.charts.bar.BarChartData
import com.github.tehras.charts.bar.renderer.label.SimpleValueDrawer


data class PorcentajeZonas(val zona: String, val variable: String, val total: Int)

data class ConsultaZonaPeriodo(val zona: Int, val periodo: String)

data class GrasasTotalesZona (val zona: Int, var grasasTotales: Float)


class EstadisticaFragment : Fragment() {

    // ViewModel de EncuestaAlimento
    private val encuestaAlimentoViewModel: EncuestaAlimentoViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {

        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_estadistica, container, false)
    }

    @Composable
    fun generalStatistics(){
        var periodoSeleccionado by remember { mutableStateOf("dia") }
        Column(
            modifier = Modifier.verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(text = "Grasas Totales",
                fontSize = 24.sp,
                modifier = Modifier.padding(top = 16.dp))

            selectorPeriodo { periodo ->
                periodoSeleccionado = periodo
            }

            // Llama a la función que dibuja el gráfico pasando el periodo seleccionado
            dibujoBarras(periodoSeleccionado)

        }
    }

    @Composable
    fun dibujoBarras(periodo: String){

        val listDatos: List<ConsultaZonaPeriodo> = listOf(
            ConsultaZonaPeriodo(1, periodo),
            ConsultaZonaPeriodo(2, periodo),
            ConsultaZonaPeriodo(3, periodo),
            ConsultaZonaPeriodo(4, periodo)
        )

        val totalGrasasTotales: List<GrasasTotalesZona> = listOf(
            GrasasTotalesZona(1, 0F),
            GrasasTotalesZona(2, 0F),
            GrasasTotalesZona(3, 0F),
            GrasasTotalesZona(4, 0F)
        )
        /*
            val IdsEncuestasAlimentos = encuestaAlimentoViewModel.obtenerIdsEncuestaAlimento()
            Log.i("IDS ", " encuestaA ")


            encuestaAlimentoViewModel.obtenerEncuestasAlimentoConZona(IdsEncuestasAlimentos).observe(viewLifecycleOwner) { encuestas ->
                // Maneja los datos obtenidos aquí
                Log.i("acaEntra ", " encuestaA ")

                encuestas.forEach{ encuesta ->
                    Log.i("encuestaA ", " encuestaA " + encuesta.encuestaAlimento.encuestaId + " zona " + encuesta.zona)
                }

                DatosConsumoGrasas.registrarDatos(encuestas)

                totalGrasasTotales.forEach{ grasasTotalesZona ->
                    grasasTotalesZona.grasasTotales =
                        DatosConsumoGrasas.obtenerDatosPorZonaYPeriodo(grasasTotalesZona.zona,periodo)!!
                }



            } */



        var sumaTotal = 0.0
        totalGrasasTotales.forEach { grasasTotalesZona ->
            sumaTotal += grasasTotalesZona.grasasTotales
        }

        Text(text = "Resultado total de consumo: ",
            fontSize = 20.sp)
        Text(text = String.format("%.2f", sumaTotal) + " ml",
            fontSize = 18.sp)



        var barras = ArrayList<BarChartData.Bar>()
        listDatos.mapIndexed { index, datos ->

            barras.add(
                BarChartData.Bar(
                    label = "Zona " + listDatos.get(index).zona,
                    value = DatosConsumoGrasas.obtenerDatosPorZonaYPeriodo(listDatos.get(index).zona, listDatos.get(index).periodo),
                    color = randomColor()
                )
            )
        }
        BarChart(
            barChartData = BarChartData(
                bars = barras
            ),
            modifier = Modifier
                .padding(horizontal = 30.dp, vertical = 80.dp)
                .height(300.dp),
            labelDrawer = SimpleValueDrawer(
                drawLocation = SimpleValueDrawer.DrawLocation.XAxis
            )
        )



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

    @Preview
    @Composable
    fun PreviewMyComposeContent() {
        generalStatistics()
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


        Column(Modifier.padding(20.dp).clickable { expanded = !expanded }) {
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