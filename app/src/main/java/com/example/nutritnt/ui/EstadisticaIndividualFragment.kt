package com.example.nutritnt.ui


import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.CheckBox
import android.widget.LinearLayout
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.nutritnt.R
import com.example.nutritnt.data.Consumo
import com.example.nutritnt.data.DatosConsumo
import com.example.nutritnt.database.entities.Encuesta
import com.example.nutritnt.databinding.FragmentEstadisticaIndividualBinding
import com.example.nutritnt.viewmodel.EncuestaAlimentoViewModel
import com.example.nutritnt.viewmodel.EncuestaViewModel
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import com.github.mikephil.charting.formatter.PercentFormatter
import com.github.mikephil.charting.utils.ColorTemplate
import kotlinx.coroutines.launch
import java.util.Locale
import kotlin.properties.Delegates


class EstadisticaIndividualFragment : Fragment() {

    private val encuestaAlimentoViewModel: EncuestaAlimentoViewModel by viewModels()
    private val encuestaViewModel: EncuestaViewModel by viewModels()
    private lateinit var barChart: BarChart
    private lateinit var datosConsumo: List<Consumo>
    private lateinit var binding: FragmentEstadisticaIndividualBinding
    private lateinit var legendContainer: LinearLayout
    private var shouldUpdateGraphAutomatically = true
    private var encuestaGeneralID by Delegates.notNull<Int>()
    private var encuestaGeneral: Encuesta? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Obtener el argumento pasado desde el Fragment anterior
        arguments?.let {
            encuestaGeneralID = EstadisticaIndividualFragmentArgs.fromBundle(it).encuestaGeneralID

        }

    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentEstadisticaIndividualBinding.inflate(inflater, container, false)
        val view = binding.root

        legendContainer = view.findViewById(R.id.legendContainer)
        barChart = view.findViewById(R.id.barChart)

        encuestaViewModel.getEncuestaById(encuestaGeneralID).observe(
            viewLifecycleOwner,
            Observer { encuesta ->
                encuestaGeneral = encuesta
                if (shouldUpdateGraphAutomatically) {
                    actualizarGrafico("dia")
                }
            }
        )

        binding.textviewVolverListado.setOnClickListener{
            shouldUpdateGraphAutomatically = false
           findNavController().navigate(EstadisticaIndividualFragmentDirections.actionEstadisticaIndividualFragmentToListEncuestasAlimentosFragment(encuestaGeneralID))
        }

        return view

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)



        val expandButton: Button = view.findViewById(R.id.expandButton)
        val expandableSection: LinearLayout = view.findViewById(R.id.expandableSection)
        val confirmButton: Button = view.findViewById(R.id.confirmButton)
        val checkboxDia: CheckBox = view.findViewById(R.id.checkboxDia)
        val checkboxSemana: CheckBox = view.findViewById(R.id.checkboxSemana)
        val checkboxMes: CheckBox = view.findViewById(R.id.checkboxMes)
        val checkboxAnio: CheckBox = view.findViewById(R.id.checkboxAnio)


        // Configurar CheckBoxDia como seleccionado por defecto
        checkboxDia.isChecked = true

        // Crear una lista de todos los CheckBox
        val checkBoxes = listOf(checkboxDia, checkboxSemana, checkboxMes, checkboxAnio)

        // Listener para manejar la selección única
        val checkBoxListener = View.OnClickListener { clickedView ->
            if (clickedView is CheckBox) {
                for (checkBox in checkBoxes) {
                    checkBox.isChecked = (checkBox == clickedView)
                }
            }
        }

        // Asignar el listener a cada CheckBox
        for (checkBox in checkBoxes) {
            checkBox.setOnClickListener(checkBoxListener)
        }

        expandButton.setOnClickListener {
            if (expandableSection.visibility == View.VISIBLE) {
                expandableSection.visibility = View.GONE
                expandButton.text = "Seleccionar periodo"
            } else {
                expandableSection.visibility = View.VISIBLE
                expandButton.text = "Contraer sección"
            }
        }

        confirmButton.setOnClickListener {
            val selectedOption = checkBoxes.find { it.isChecked }?.text ?: "Ninguna opción seleccionada"

            // Llamar a la función deseada con la opción seleccionada
            actualizarGrafico(selectedOption.toString().lowercase())

            // Ocultar la sección y cambiar el texto del botón
            expandableSection.visibility = View.GONE
            expandButton.text = "Seleccionar periodo"
        }



    }
    private fun actualizarGrafico(periodo: String) {


        lifecycleScope.launch {



            encuestaGeneral?.codigoParticipante?.let {
                encuestaAlimentoViewModel.fetchEncuestaAlimentosWithAlimentoAndInfo(
                    it
                )
            }
            binding.titleCodeUserText.text = encuestaGeneral?.codigoParticipante


            var listaCompletada = false

            encuestaAlimentoViewModel.encuestaAlimentosWithAlimentoAndInfo.observe(
                viewLifecycleOwner,
                Observer { listado ->
                    val entries: MutableList<BarEntry> = ArrayList()

                    listado?.let { listaCompleta ->

                        if (listaCompleta.isNotEmpty()) {
                            datosConsumo =
                                DatosConsumo.calcularValoresNutricionales(listaCompleta, periodo)
                            var contador = 0F
                            for (dato in datosConsumo) {
                                entries.add(BarEntry(contador, dato.gramos))
                                contador++
                            }
                            listaCompletada = true
                        }

                    }

                    if (listaCompletada) {
                        llenarGrafico(entries,datosConsumo)
                    }


                })


        }

    }

    private fun llenarGrafico(entries: MutableList<BarEntry>, datosConsumo: List<Consumo>) {


        val dataSet = BarDataSet(entries, "Categorias")
        dataSet.colors = listOf(
            ColorTemplate.rgb("#4682B4"),  //blue
            ColorTemplate.rgb("#FFA07A"),  // naranjita
            ColorTemplate.rgb("#FF69B4"),  // rosita
            ColorTemplate.rgb("#9370DB") ,  // violetita
            ColorTemplate.rgb("#dcb4eb"),  // lavanda
            ColorTemplate.rgb("#fadcb4"),  // melocoton
            ColorTemplate.rgb("#e0e0e0")   // gris perla
        )

        val barData = BarData(dataSet)
        barData.setValueFormatter(null)
        barData.setValueTextSize(0f)
        barChart.setData(barData)
        barChart.description.isEnabled = false // Deshabilitar la descripción del gráfico
        barChart.legend.isEnabled = false // Deshabilitar la leyenda
        barChart.invalidate() // Actualiza el gráfico
        createLegend(legendContainer, entries, dataSet.colors, datosConsumo)
    }



    private fun createLegend(
        legendContainer: LinearLayout,
        entries: MutableList<BarEntry>,
        colors: List<Int>,
        datosConsumo: List<Consumo>
    ) {
        legendContainer.removeAllViews()
        val inflater = LayoutInflater.from(context)

        for (i in entries.indices) {
            val legendItem: View =
                inflater.inflate(R.layout.legend_chart_item, legendContainer, false)
            val colorBox = legendItem.findViewById<View>(R.id.colorBox)
            val label = legendItem.findViewById<TextView>(R.id.label)
            val infoPorcentaje = legendItem.findViewById<TextView>(R.id.informacion_porcentaje)
            val infoGramos = legendItem.findViewById<TextView>(R.id.informacion_gramos)
            colorBox.setBackgroundColor(colors[i])
            label.text = capitalizeFirstLetter(datosConsumo[i].descripcion)
            infoPorcentaje.text = "${"%.2f".format(datosConsumo[i].porcentaje)}%"
            infoGramos.text = "${"%.2f".format(datosConsumo[i].gramos)}gr"
            legendContainer.addView(legendItem)
        }
    }

    fun capitalizeFirstLetter(str: String): String {
        return if (str.isEmpty()) {
            str
        } else {
            str.replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.getDefault()) else it.toString() }
        }
    }




}