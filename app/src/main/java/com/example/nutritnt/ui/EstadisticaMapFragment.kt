package com.example.nutritnt.ui

import android.annotation.SuppressLint
import android.graphics.Color
import android.os.Bundle
import android.preference.PreferenceManager
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.nutritnt.R
import com.example.nutritnt.database.entities.Encuesta
import com.example.nutritnt.databinding.FragmentEstadisticaMapBinding
import com.example.nutritnt.databinding.FragmentUbicacionConsumidorBinding
import com.example.nutritnt.viewmodel.EncuestaViewModel
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptor
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.gms.maps.model.PolylineOptions
import org.osmdroid.config.Configuration.*
import org.osmdroid.views.MapView
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.overlay.Marker
import org.osmdroid.views.overlay.Polyline


class EstadisticaMapFragment : Fragment(), OnMapReadyCallback {

    private lateinit var googleMap: GoogleMap
    private var marker: com.google.android.gms.maps.model.Marker? = null
    private lateinit var binding: FragmentEstadisticaMapBinding
    private val encuestaViewModel : EncuestaViewModel by viewModels()
    private lateinit var legendContainer: LinearLayout

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentEstadisticaMapBinding.inflate(layoutInflater)
        val view = binding.root

        val mapFragment = childFragmentManager.findFragmentById(R.id.mapView) as SupportMapFragment
        mapFragment.getMapAsync(this)

        legendContainer = binding.leyendEstadisticaMapContainer

        binding.buttonInicio.setOnClickListener(){
            findNavController().navigate(R.id.action_estadisticaMapFragment_to_welcomeFragment)
        }


        return view
    }

    override fun onMapReady(map: GoogleMap) {
        googleMap = map

        // Habilitar controles de zoom
        googleMap.uiSettings.isZoomControlsEnabled = true

        val startPoint = LatLng(-42.775082, -65.047036)
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(startPoint, 15f))

        // Observa los datos de encuestas
        encuestaViewModel.todasLasEncuestas.observe(viewLifecycleOwner) { encuestas ->
            agregarMarcadores(encuestas)
            mostrarLeyenda(encuestas)
        }







    }

    private fun agregarMarcadores(encuestas: List<Encuesta>) {
        googleMap.clear() // Limpia marcadores anteriores

        drawSeparationLines()

        for (encuesta in encuestas) {
            Log.i("encuestaMapF", "id: ${encuesta.encuestaId} : lat: ${encuesta.latitud.toDoubleOrNull()} long: ${encuesta.longitud.toDoubleOrNull()}")
            val location = LatLng(encuesta.latitud.toDouble(), encuesta.longitud.toDouble())
            val markerOptions = MarkerOptions()
                .position(location)
                .title(encuesta.zona)
                .icon(obtenerIconoParaZona(encuesta.zona))

            googleMap.addMarker(markerOptions)
        }

    }

    private fun obtenerIconoParaZona(zona: String): BitmapDescriptor {
        return when (zona) {
            "Zona 1" -> BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE)
            "Zona 2" -> BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_MAGENTA)
            "Zona 3" -> BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_CYAN)
            else -> BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN)
        }
    }

    @SuppressLint("SetTextI18n")
    private fun mostrarLeyenda(encuestas: List<Encuesta>) {
        // Limpiar el contenedor de leyenda anterior
        legendContainer.removeAllViews()

        // Contar encuestas por zona
        val encuestasPorZona = encuestas.groupBy { it.zona }
        encuestasPorZona.forEach { (zona, encuestas) ->
            val color = obtenerColorParaZona(zona)

            // Crear la vista de la leyenda
            val legendItem = layoutInflater.inflate(R.layout.leyend_estadistica_map_container, legendContainer, false)

            val colorBox = legendItem.findViewById<View>(R.id.color_box)
            val zonaText = legendItem.findViewById<TextView>(R.id.zona_text)
            val cantidadText = legendItem.findViewById<TextView>(R.id.cantidad_text)

            colorBox.setBackgroundColor(color)
            zonaText.text = zona
            cantidadText.text = "cantidad de encuestados: " + encuestas.size

            // Añadir la vista de leyenda al contenedor
            legendContainer.addView(legendItem)
        }
    }

    private fun obtenerColorParaZona(zona: String): Int {
        return when (zona) {
            "Zona 1" -> Color.parseColor("#FFA500") // ORANGE
            "Zona 2" -> Color.parseColor("#FF00FF") // MAGENTA
            "Zona 3" -> Color.parseColor("#00FFFF") // CYAN
            else -> Color.parseColor("#30f72d")    // GREEN
        }
    }

    private fun drawSeparationLines() {
        val centerLatitude = ((-42.769412) + (-42.787398)) /2
        val centerLongitude = ((-65.030643) + (-65.083944)) /2


        Log.i("centerlat", "latitud " + centerLatitude)
        Log.i("centerlat", "longitud " + centerLongitude)


        // Definir puntos para las líneas
        val horizontalLine = mutableListOf(
            LatLng(-42.769412, -65.030643),
            LatLng(-42.787398, -65.083944)
        )

        val verticalLine = mutableListOf(
            LatLng(-42.751162, -65.061959),
            LatLng(-42.790792, -65.036940)
        )

        // Dibujar líneas en el mapa
        googleMap.addPolyline(PolylineOptions().addAll(horizontalLine).color(Color.RED))
        googleMap.addPolyline(PolylineOptions().addAll(verticalLine).color(Color.RED))
    }




}