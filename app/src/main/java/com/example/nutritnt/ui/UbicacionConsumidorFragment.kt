package com.example.nutritnt.ui

import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.nutritnt.R
import com.example.nutritnt.data.DatosDatabase
import com.example.nutritnt.databinding.FragmentNuevaEncuestaAlimentoBinding
import com.example.nutritnt.databinding.FragmentUbicacionConsumidorBinding
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.gms.maps.model.PolylineOptions



class UbicacionConsumidorFragment : Fragment(), OnMapReadyCallback {

    private lateinit var googleMap: GoogleMap
    private var marker: Marker? = null
    private lateinit var binding: FragmentUbicacionConsumidorBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentUbicacionConsumidorBinding.inflate(layoutInflater)
        val view = binding.root

        val mapFragment = childFragmentManager.findFragmentById(R.id.mapView) as SupportMapFragment
        mapFragment.getMapAsync(this)

        val myButton: Button = binding.sigButton
        myButton.setOnClickListener {
            marker?.let {
                val position = it.position
                val latitude = position.latitude
                val longitude = position.longitude

                val pos = determineQuadrant(position)

                Toast.makeText(context, "Lat: $latitude, Lon: $longitude, position: $pos", Toast.LENGTH_LONG).show()
            }
        }



        return view
    }

    override fun onMapReady(map: GoogleMap) {
        googleMap = map

        // Habilitar controles de zoom
        googleMap.uiSettings.isZoomControlsEnabled = true

        // Ubicación inicial (París)
        val startPoint = LatLng(-42.775082, -65.047036)
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(startPoint, 15f))

        // Añadir un marcador inicial
        marker = googleMap.addMarker(MarkerOptions().position(startPoint).draggable(true))

        googleMap.setOnMarkerDragListener(object : GoogleMap.OnMarkerDragListener {
            override fun onMarkerDragStart(marker: Marker) {
                // Código opcional al empezar a arrastrar
            }

            override fun onMarkerDrag(marker: Marker) {
                // Código opcional mientras se arrastra
            }

            override fun onMarkerDragEnd(marker: Marker) {
                val position = marker.position
                val latitude = position.latitude
                val longitude = position.longitude
                // Hacer algo con la nueva posición del marcador
                Toast.makeText(context, "Marker moved to Lat: $latitude, Lon: $longitude", Toast.LENGTH_SHORT).show()

            }
        })

      //  drawAxes(startPoint)
       // drawSeparationLines()
        drawPerpendicularLines()

    }

    private fun determineQuadrant(location: LatLng): Int {
        //LA INTERSECCION ES EN BOUCHARD Y GALES
        // Definir las coordenadas de intersección
        val centerLatitude = -42.774875213237536
        // Coordenada media de latitud entre los puntos de las líneas horizontales
        val centerLongitude =  -65.0468285009265

        return when {
            location.latitude >= centerLatitude && location.longitude >= centerLongitude -> 1
            location.latitude >= centerLatitude && location.longitude < centerLongitude -> 2
            location.latitude < centerLatitude && location.longitude < centerLongitude -> 3
            else -> 4
        }
    }

    private fun drawPerpendicularLines() {
        // Coordenadas de los extremos de las líneas
        val point1 = LatLng(-42.769412, -65.030643) // Punto 1
        val point2 = LatLng(-42.751162, -65.061959) // Punto 2

        // Calcular coordenadas de los puntos de intersección
        val intersectionPoint1 = LatLng(point1.latitude, point2.longitude)
        val intersectionPoint2 = LatLng(point2.latitude, point1.longitude)

        // Dibujar las líneas en el mapa
        googleMap.addPolyline(PolylineOptions().add(point1, intersectionPoint1).color(Color.RED)) // Línea 1
        googleMap.addPolyline(PolylineOptions().add(point2, intersectionPoint1).color(Color.RED)) // Línea 2
        googleMap.addPolyline(PolylineOptions().add(point1, intersectionPoint2).color(Color.BLUE)) // Línea 3
        googleMap.addPolyline(PolylineOptions().add(point2, intersectionPoint2).color(Color.BLUE)) // Línea 4
    }


    private fun drawSeparationLines() {
        val centerLatitude = ((-42.769412) + (-42.787398)) /2
        val centerLongitude = ((-65.030643) + (-65.083944)) /2


        Log.i("centerlat", "latitud " + centerLatitude)
        Log.i("centerlat", "longitud " + centerLongitude)

        googleMap.addMarker(MarkerOptions().position(LatLng(centerLatitude, centerLongitude)).draggable(true))


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
        googleMap.addPolyline(PolylineOptions().addAll(verticalLine).color(Color.BLUE))
    }



}