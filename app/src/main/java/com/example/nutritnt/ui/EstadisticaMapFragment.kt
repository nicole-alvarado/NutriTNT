package com.example.nutritnt.ui

import android.graphics.Color
import android.os.Bundle
import android.preference.PreferenceManager
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.nutritnt.R
import org.osmdroid.config.Configuration.*

import org.osmdroid.views.MapView
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.overlay.Marker
import org.osmdroid.views.overlay.Polyline


class EstadisticaMapFragment : Fragment() {

    private lateinit var mapView: MapView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
      //  Configuration.getInstance().userAgentValue = BuildConfig
        getInstance().load(context, PreferenceManager.getDefaultSharedPreferences(context))
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_estadistica_map, container, false)
        mapView = view.findViewById(R.id.map)
        mapView.setBuiltInZoomControls(true)
        mapView.setMultiTouchControls(true)
        mapView.controller.setZoom(15.0)
        val startPoint = GeoPoint(-42.7802058, -65.0369597) // Coordenadas de ejemplo
        mapView.controller.setCenter(startPoint)

        drawSeparationLines()

        val locations = listOf(
            GeoPoint(-42.780000, -65.050000),
            GeoPoint(-42.760000, -65.070000),
            GeoPoint(-42.775000, -65.040000),
            GeoPoint(-42.770000, -65.060000)
        )

        // Clasificar las ubicaciones en cuadrantes y agregar marcadores
        addMarkers(locations)


        return view
    }

    private fun addMarkers(locations: List<GeoPoint>) {
        for (location in locations) {
            val quadrant = determineQuadrant(location)
            val marker = Marker(mapView)
            marker.position = location
            marker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM)
            marker.title = "Cuadrante $quadrant"
            mapView.overlays.add(marker)
        }
        mapView.invalidate() // Refrescar el mapa
    }

    private fun determineQuadrant(location: GeoPoint): Int {
        // Definir las coordenadas de intersección
        val centerLatitude = -42.7782905  // Coordenada media de latitud entre los puntos de las líneas horizontales
        val centerLongitude = -65.057755  // Coordenada media de longitud entre los puntos de las líneas verticales

        return when {
            location.latitude >= centerLatitude && location.longitude >= centerLongitude -> 1
            location.latitude >= centerLatitude && location.longitude < centerLongitude -> 2
            location.latitude < centerLatitude && location.longitude < centerLongitude -> 3
            else -> 4
        }
    }

    private fun drawSeparationLines() {
        val north = mapView.boundingBox.latNorth
        val south = mapView.boundingBox.latSouth
        val east = mapView.boundingBox.lonEast
        val west = mapView.boundingBox.lonWest

        val centerLatitude = (north + south) / 2
        val centerLongitude = (east + west) / 2

        // Línea horizontal (latitud media)
        val horizontalLine = Polyline()
        horizontalLine.addPoint(GeoPoint(-42.769439, -65.030516))
        horizontalLine.addPoint(GeoPoint(-42.787142, -65.083988))
        horizontalLine.color = Color.RED

        // Línea vertical (longitud media)
        val verticalLine = Polyline()
        verticalLine.addPoint(GeoPoint(-42.788024, -65.040644))
        verticalLine.addPoint(GeoPoint(-42.753702, -65.060509))
        verticalLine.color = Color.BLUE

        // Agregar las líneas al mapa
        mapView.overlayManager.add(horizontalLine)
        mapView.overlayManager.add(verticalLine)

        mapView.invalidate() // Refrescar el mapa
    }

    override fun onResume() {
        super.onResume()
        mapView.onResume()
    }

    override fun onPause() {
        super.onPause()
        mapView.onPause()
    }


}