package com.example.nutritnt.data

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.Image
import androidx.fragment.app.Fragment
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.example.nutritnt.R
import com.example.nutritnt.viewmodel.EncuestaAlimentoViewModel
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
import com.github.mikephil.charting.formatter.ValueFormatter
import com.github.mikephil.charting.utils.ColorTemplate

class OtraEstadisticaFragment : Fragment() {

    private val encuestaAlimentoViewModel: EncuestaAlimentoViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return ComposeView(requireContext()).apply {
            setContent {
                MyApp {
                    DrawBars()
                }
            }
        }
    }

    @Preview
    @Composable
    fun PreviewMyContent() {
        BackgroundImage()
    }

    @Composable
    fun BackgroundImage() {
        Box(
            modifier = Modifier
                .fillMaxSize()
        ) {
            Image(
                painter = painterResource(id = R.drawable.background_estadisticas), // Reemplaza `your_image` con el nombre de tu imagen en drawable
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
            )
            Box(
                modifier = Modifier
                    .fillMaxSize()

            ) {
                DrawBars()
            }
        }
    }

    @Composable
    fun DrawBars() {

        Text(text = "Resultado total de consumo: ",
            fontSize = 20.sp)
        Text(text = String.format("%.2f", 10F) + " ml",
            fontSize = 18.sp)

        val zonas: List<String> = listOf(
            "Zona A",
            "Zona B",
            "Zona C",
            "Zona D",
        )

        for (zona in zonas){
            encuestaAlimentoViewModel.getEncuestaAlimentosByZonaAndAlimento(zona, 1).observe(viewLifecycleOwner, Observer { encuestaAlimentos ->
                // Actualiza la UI con los datos
                encuestaAlimentos?.let {

                }
            })
        }





        val barChartData = listOf(
            BarEntry(0f, 298.52f),
            BarEntry(1f, 314.72f),
            BarEntry(2f, 150.25f),
            BarEntry(3f, 210.60f)
        )

        val labels = listOf("A", "B", "C", "D")

        val barDataSet = BarDataSet(barChartData, "Sample Data")
        barDataSet.colors = listOf(
            ColorTemplate.rgb("#FF1341"),  // Red
            ColorTemplate.rgb("#00FF00"),  // Green
            ColorTemplate.rgb("#0000FF"),  // Blue
            ColorTemplate.rgb("#FFFF00")   // Yellow
        )
        barDataSet.valueTextSize = 12f
        barDataSet.valueTextColor = Color.BLACK
        barDataSet.valueFormatter = object : ValueFormatter() {
            override fun getBarLabel(barEntry: BarEntry?): String {
                return "%.2f".format(barEntry?.y)
            }
        }

        val barData = BarData(barDataSet)
        barData.barWidth = 0.9f

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top
        ) {
            InfoBox(color = androidx.compose.ui.graphics.Color.Red, percentage = 298.52f)

            Spacer(modifier = Modifier.height(16.dp))

            AndroidView(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(300.dp),
                factory = { context ->
                    BarChart(context).apply {
                        this.data = barData
                        this.description.isEnabled = false

                        // Customize X axis
                        this.xAxis.position = XAxis.XAxisPosition.BOTTOM
                        this.xAxis.setDrawGridLines(false)
                        this.xAxis.granularity = 1f
                        this.xAxis.valueFormatter = IndexAxisValueFormatter(labels)

                        // Customize Y axis
                        this.axisLeft.axisMinimum = 0f
                        this.axisLeft.granularity = 50f
                        this.axisLeft.setDrawGridLines(true)
                        this.axisLeft.valueFormatter = object : ValueFormatter() {
                            override fun getFormattedValue(value: Float): String {
                                return "%.2f".format(value)
                            }
                        }

                        // Disable right Y axis
                        this.axisRight.isEnabled = false

                        // Refresh chart
                        this.invalidate()
                    }
                }
            )
        }
    }

    @Composable
    fun InfoBox(color: androidx.compose.ui.graphics.Color, percentage: Float) {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(16.dp)
                    .background(color)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(text = "%.2f".format(percentage), fontSize = 16.sp, color = androidx.compose.ui.graphics.Color.Black)
        }
    }

    @Composable
    fun MyApp(content: @Composable () -> Unit) {
        MaterialTheme {
            Surface(
                modifier = Modifier.fillMaxSize(),
                color = androidx.compose.ui.graphics.Color.White
            ) {
                content()
            }
        }
    }
}
