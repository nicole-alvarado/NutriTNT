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
import android.widget.LinearLayout
import android.widget.Spinner
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import com.example.nutritnt.data.Consumo
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


        //actualizarGrafico("dia")

        val entries = mutableListOf<PieEntry>(
            PieEntry(40f, "Zonita 1"),
            PieEntry(30f, "Zonita 2"),
            PieEntry(20f, "Zonita 3"),
            PieEntry(10f, "Zonita 4"),
            PieEntry(10f, "Zonita 5"),
            PieEntry(10f, "Zonita 6"),
            PieEntry(10f, "Zonita 7")
        )

        val zonas = listOf("Zona 1", "Zona 2", "Zona 3", "Zona 4")
      //  llenarGrafico(entries, zonas)
        createLegend(binding.containerCardView, entries, zonas)

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



    private fun createLegend(
        legendContainer: LinearLayout,
        entries: MutableList<PieEntry>,
        zonas: List<String>
    ) {
        legendContainer.removeAllViews() // Limpiar vistas anteriores si es necesario
        val inflater = LayoutInflater.from(legendContainer.context)

        // Iterar sobre las zonas para crear una CardView por zona
        for (zona in zonas) {
            // Inflar la CardView principal por cada zona
            val legendZone: View = inflater.inflate(
                com.example.nutritnt.R.layout.cardview_estadisticas_items,
                legendContainer,
                false
            )

            // Obtener referencia al TextView 'label' dentro de la CardView
            val label = legendZone.findViewById<TextView>(com.example.nutritnt.R.id.cardTitle)
            label.text = zona // Establecer el texto de la zona en el TextView 'label'

            // Obtener el contenedor donde se agregarán los items de leyenda
            val legendItemContainer = legendZone.findViewById<LinearLayout>(com.example.nutritnt.R.id.legendContainer)

            // Iterar sobre las entries para agregar cada item de leyenda
            for (entry in entries) {
                // Inflar el item de leyenda para cada entry
                val legendItem: View = inflater.inflate(
                    com.example.nutritnt.R.layout.legend_estadisticsbyzone_items,
                    legendItemContainer,
                    false
                )

                // Obtener referencias a los TextViews dentro del item de leyenda
                val labelItem = legendItem.findViewById<TextView>(com.example.nutritnt.R.id.label)
                val infoPorcentaje = legendItem.findViewById<TextView>(com.example.nutritnt.R.id.informacion_porcentaje)
                val infoGramos = legendItem.findViewById<TextView>(com.example.nutritnt.R.id.informacion_gramos)

                // Configurar los datos de la entry en los TextViews
                labelItem.text = entry.label
                infoPorcentaje.text = "${4f}%" // Configurar porcentaje
                infoGramos.text = "${3f}gr" // Configurar gramos

                // Agregar el item de leyenda al contenedor dentro de la CardView
                legendItemContainer.addView(legendItem)
            }

            // Agregar la CardView completa (con todas sus entradas de leyenda) a legendContainer
            legendContainer.addView(legendZone)
        }
    }





}