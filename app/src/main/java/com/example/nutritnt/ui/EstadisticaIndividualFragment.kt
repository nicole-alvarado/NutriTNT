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
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import com.github.mikephil.charting.formatter.PercentFormatter
import com.github.mikephil.charting.utils.ColorTemplate
import kotlinx.coroutines.launch


class EstadisticaIndividualFragment : Fragment() {

    private val encuestaAlimentoViewModel: EncuestaAlimentoViewModel by viewModels()
    private val encuestaViewModel: EncuestaViewModel by viewModels()
    private lateinit var pieChart: PieChart
    private lateinit var datosConsumo: List<Consumo>
    private lateinit var binding: FragmentEstadisticaIndividualBinding
    private lateinit var legendContainer: LinearLayout
    private var shouldUpdateGraphAutomatically = true
    private lateinit var codigo: String
    private var encuestaID: Int = 0


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Obtener el argumento pasado desde el Fragment anterior
        arguments?.let {
            codigo = EstadisticaIndividualFragmentArgs.fromBundle(it).codigo

        }

        lifecycleScope.launch {
            encuestaID = codigo.let {
                encuestaViewModel.obtenerIdEncuestaPorCodigo(it)
            }!!

            // Usar encuestaID aquí después de haberlo obtenido
            if (encuestaID != null) {
                // Procesar el ID de la encuesta
                Log.d("MiFragmento", "ID de la encuesta: $encuestaID")
            } else {
                Log.e("MiFragmento", "No se pudo obtener el ID para el código: $codigo")
            }
        }
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentEstadisticaIndividualBinding.inflate(inflater, container, false)
        val view = binding.root

        legendContainer = view.findViewById(R.id.legendContainer)
        pieChart = view.findViewById(R.id.pieChart)

        if (shouldUpdateGraphAutomatically) {
            actualizarGrafico("dia")
        }

        binding.backButton.setOnClickListener{
            shouldUpdateGraphAutomatically = false
           findNavController().navigate(EstadisticaIndividualFragmentDirections.actionEstadisticaIndividualFragmentToListEncuestasAlimentosFragment(encuestaID))
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


        // Configurar CheckBox1 como seleccionado por defecto
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

            encuestaAlimentoViewModel.fetchEncuestaAlimentosWithAlimentoAndInfo(codigo)
            binding.titleCodeUserText.text = codigo


            var listaCompletada = false

            encuestaAlimentoViewModel.encuestaAlimentosWithAlimentoAndInfo.observe(
                viewLifecycleOwner,
                Observer { listado ->
                    val entries: MutableList<PieEntry> = ArrayList()
                    Log.i("observerEstInd", "entró a observer")
                    listado?.let { listaCompleta ->

                        Log.i("observerEstInd", listaCompleta.toString())




                        if (listaCompleta.isNotEmpty()) {
                            datosConsumo =
                                DatosConsumo.calcularValoresNutricionales(listaCompleta, periodo)
                            for (dato in datosConsumo) {
                                entries.add(PieEntry(dato.porcentaje, dato.descripcion))
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

    private fun llenarGrafico(entries: MutableList<PieEntry>, datosConsumo: List<Consumo>) {

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

        for (i in entries.indices) {
            val legendItem: View =
                inflater.inflate(R.layout.legend_chart_item, legendContainer, false)
            val colorBox = legendItem.findViewById<View>(R.id.colorBox)
            val label = legendItem.findViewById<TextView>(R.id.label)
            val infoPorcentaje = legendItem.findViewById<TextView>(R.id.informacion_porcentaje)
            val infoGramos = legendItem.findViewById<TextView>(R.id.informacion_gramos)
            colorBox.setBackgroundColor(colors[i])
            label.text = datosConsumo[i].descripcion
            infoPorcentaje.text = "${"%.2f".format(datosConsumo[i].porcentaje)}%"
            infoGramos.text = "${"%.2f".format(datosConsumo[i].gramos)}gr"
            legendContainer.addView(legendItem)
        }
    }




}