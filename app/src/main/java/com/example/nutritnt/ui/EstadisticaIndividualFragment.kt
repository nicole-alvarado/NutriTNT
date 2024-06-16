package com.example.nutritnt.ui


import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import com.example.nutritnt.R
import com.example.nutritnt.data.Consumo
import com.example.nutritnt.data.DatosConsumo
import com.example.nutritnt.database.relations.EncuestaAlimento_AlimentoInformacionNutricional
import com.example.nutritnt.databinding.FragmentEstadisticaIndividualBinding
import com.example.nutritnt.viewmodel.EncuestaAlimentoViewModel
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import com.github.mikephil.charting.formatter.PercentFormatter
import com.github.mikephil.charting.utils.ColorTemplate
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch


class EstadisticaIndividualFragment : Fragment() {

    private val encuestaAlimentoViewModel: EncuestaAlimentoViewModel by viewModels()
    private lateinit var pieChart: PieChart
    private lateinit var datosConsumo: List<Consumo>
    private lateinit var binding: FragmentEstadisticaIndividualBinding


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentEstadisticaIndividualBinding.inflate(inflater, container, false)
        val view = binding.root

        val legendContainer = view.findViewById<LinearLayout>(R.id.legendContainer)
        pieChart = view.findViewById(R.id.pieChart)

        actualizarGrafico(legendContainer, "dia")

        return view

    }


    private fun actualizarGrafico(legendContainer: LinearLayout, periodo: String) {


        lifecycleScope.launch {

            encuestaAlimentoViewModel.fetchEncuestaAlimentosWithAlimentoAndInfo("codigo1")


            var listaCompletada = false

            encuestaAlimentoViewModel.encuestaAlimentosWithAlimentoAndInfo.observe(
                viewLifecycleOwner,
                Observer { listado ->
                    val entries: MutableList<PieEntry> = ArrayList()
                    Log.i("observerEstInd", "entró a observer")
                    listado?.let { listaCompleta ->

                        Log.i("observerEstInd", listaCompleta.toString())

                        datosConsumo =
                            DatosConsumo.calcularValoresNutricionales(listaCompleta, "dia")


                        if (listaCompleta.isNotEmpty()) {
                            for (dato in datosConsumo) {
                                entries.add(PieEntry(dato.porcentaje, dato.descripcion))
                            }
                            listaCompletada = true
                        }

                    }

                    if (listaCompletada) {
                        llenarGrafico(entries,legendContainer, datosConsumo)
                    }


                })


        }

    }

    private fun llenarGrafico(entries: MutableList<PieEntry>, legendContainer: LinearLayout, datosConsumo: List<Consumo>) {

        var entriesModif: MutableList<PieEntry> = ArrayList()

        for (entry in entries){
            entriesModif.add(PieEntry(entry.value, ""))
        }


        val dataSet = PieDataSet(entriesModif, "Categorias")
        dataSet.colors = listOf(
            ColorTemplate.rgb("#4682B4"),  //blue
            ColorTemplate.rgb("#FFA07A"),  // naranjita
            ColorTemplate.rgb("#FF69B4"),  // rosita
            ColorTemplate.rgb("#9370DB") ,  // violetita
            ColorTemplate.rgb("#dcb4eb"),  // lavanda
            ColorTemplate.rgb("#fadcb4"),  // melocoton
            ColorTemplate.rgb("#e0e0e0")   // gris perla
        )
        pieChart.setUsePercentValues(true)
        val pieData = PieData(dataSet)
        pieData.setValueTextSize(15f)
        pieData.setValueFormatter(PercentFormatter(pieChart))
        pieChart.setData(pieData)
        pieChart.isDrawHoleEnabled = false
        pieChart.description.isEnabled = false // Deshabilitar la descripción del gráfico
        pieChart.legend.isEnabled = false // Deshabilitar la leyenda
        pieChart.invalidate() // Actualiza el gráfico
        createLegend(legendContainer, entries, dataSet.colors, datosConsumo)
    }



    private fun createLegend(
        legendContainer: LinearLayout,
        entries: MutableList<PieEntry>,
        colors: List<Int>,
        datosConsumo: List<Consumo>
    ) {
        legendContainer.removeAllViews()
        val inflater = LayoutInflater.from(context)

        val headers: View = inflater.inflate(R.layout.legend_chart_item_headers, legendContainer, false)
        val headerInfoKey1 = headers.findViewById<TextView>(R.id.info_header_1)
        val headerInfoKey2 = headers.findViewById<TextView>(R.id.info_header_2)

        headerInfoKey1.text = "Porcentaje"
        headerInfoKey2.text = "Gramos"

        legendContainer.addView(headers)

        for (i in entries.indices) {
            val legendItem: View =
                inflater.inflate(R.layout.legend_chart_item, legendContainer, false)
            val colorBox = legendItem.findViewById<View>(R.id.colorBox)
            val label = legendItem.findViewById<TextView>(R.id.label)
            val infoValue1 = legendItem.findViewById<TextView>(R.id.info_value_1)
            val infoValue2 = legendItem.findViewById<TextView>(R.id.info_value_2)
            colorBox.setBackgroundColor(colors[i])
            label.text = datosConsumo[i].descripcion
            infoValue1.text = "${datosConsumo[i].porcentaje}%"
            infoValue2.text = "${datosConsumo[i].gramos}gr"
            legendContainer.addView(legendItem)
        }
    }




}