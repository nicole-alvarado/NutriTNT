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
import androidx.navigation.fragment.findNavController
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
    private lateinit var totalPorZona: List<Consumo>
    private var periodoSelected: String = "dia"
    private var zonaSelected: String = "Todas"

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

        // Inicializa las selecciones por defecto
        periodoSelected = "Dia"
        zonaSelected = "Todas"

        // Configura los spinners
        val zonas = listOf("Todas", "Zona 1", "Zona 2", "Zona 3", "Zona 4")
        val periods = listOf("Dia", "Semana", "Mes", "Año")

        val spinner: Spinner = binding.spinnerPeriod
        val adapter = ArrayAdapter(requireContext(), R.layout.simple_spinner_item, periods)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner.adapter = adapter

        val spinnerZonas: Spinner = binding.spinnerZona
        val adapterZonas = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, zonas)
        adapterZonas.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerZonas.adapter = adapterZonas

        // Configura los listeners para los spinners
        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                periodoSelected = periods[position]
                actualizarGrafico()
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }

        spinnerZonas.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                zonaSelected = zonas[position]
                actualizarGrafico()
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }

        // Configura el observador una vez
        encuestaAlimentoViewModel.encuestasAlimentosConInfo.observe(viewLifecycleOwner, Observer { dataMap ->
            dataMap?.let {
                actualizarDatos(dataMap)
            }
        })

        binding.buttonBackMenu.setOnClickListener{
            findNavController().navigate(com.example.nutritnt.R.id.action_estadisticaFragment_to_menuEstadisticasFragment)
        }

        return view
    }

    // Actualiza el gráfico con los datos actuales
    private fun actualizarGrafico() {
        // Llama a la función para actualizar los datos del ViewModel si es necesario
        lifecycleScope.launch {
            // Suponiendo que este método es necesario para cargar los datos
            alimentoViewModel.cargarAlimentosConInformacionNutricional()

            val listadoZonas = encuestaViewModel.obtenerZonasDistintas()
            encuestaAlimentoViewModel.fetchEncuestasAlimentosConInfo(listadoZonas)
        }
    }

    // Actualiza los datos y el gráfico
    private fun actualizarDatos(dataMap: Map<String, List<EncuestaAlimento_AlimentoInformacionNutricional>>) {
        val encuestasFiltradas: List<EncuestaAlimento_AlimentoInformacionNutricional> = if (zonaSelected == "Todas") {
            dataMap.values.flatten()
        } else {
            dataMap[zonaSelected] ?: emptyList()
        }

        if (dataMap.isNotEmpty()) {
            val totalPorZona = DatosConsumo.calcularValoresNutricionales(encuestasFiltradas, periodoSelected.lowercase())
            createLegend(binding.containerCardView, totalPorZona, zonaSelected)
        }
    }


    private fun createLegend(
        legendContainer: LinearLayout,
        entries: List<Consumo>,
        zona: String
    ) {
        legendContainer.removeAllViews() // Limpiar vistas anteriores si es necesario
        val inflater = LayoutInflater.from(legendContainer.context)

        // Iterar sobre las zonas para crear una CardView por zona

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
                labelItem.text = entry.descripcion
                infoPorcentaje.text = "${"%.2f".format(entry.porcentaje)}%" // Configurar porcentaje
                infoGramos.text = "${"%.2f".format(entry.gramos)}gr" // Configurar gramos

                // Agregar el item de leyenda al contenedor dentro de la CardView
                legendItemContainer.addView(legendItem)
            }

            // Agregar la CardView completa (con todas sus entradas de leyenda) a legendContainer
            legendContainer.addView(legendZone)

    }





}