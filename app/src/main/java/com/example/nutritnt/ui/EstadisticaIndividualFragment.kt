package com.example.nutritnt.ui


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.example.nutritnt.R
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import com.github.mikephil.charting.utils.ColorTemplate


class EstadisticaIndividualFragment : Fragment() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_estadistica_individual, container, false)
    }

    override fun onViewCreated(@NonNull view: View, @Nullable savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val pieChart = view.findViewById<PieChart>(R.id.pieChart)
        val legendContainer = view.findViewById<LinearLayout>(R.id.legendContainer)

        // Datos para el gráfico
        val entries: MutableList<PieEntry> = ArrayList()
        entries.add(PieEntry(30f, "Categoría 1"))
        entries.add(PieEntry(20f, "Categoría 2"))
        entries.add(PieEntry(50f, "Categoría 3"))
        val dataSet = PieDataSet(entries, "Categorias")
        dataSet.setColors(*ColorTemplate.COLORFUL_COLORS) // Puedes personalizar los colores aquí
        val pieData = PieData(dataSet)
        pieChart.setData(pieData)
        pieChart.invalidate() // Actualiza el gráfico

        // Crear la leyenda manualmente
        createLegend(legendContainer, entries, dataSet.colors)
    }

    private fun createLegend(
        legendContainer: LinearLayout,
        entries: List<PieEntry>,
        colors: List<Int>
    ) {
        val inflater = LayoutInflater.from(context)
        for (i in entries.indices) {
            val legendItem: View = inflater.inflate(R.layout.legend_chart_item, legendContainer, false)
            val colorBox = legendItem.findViewById<View>(R.id.colorBox)
            val label = legendItem.findViewById<TextView>(R.id.label)
            colorBox.setBackgroundColor(colors[i])
            label.text = entries[i].label
            legendContainer.addView(legendItem)
        }
    }


}