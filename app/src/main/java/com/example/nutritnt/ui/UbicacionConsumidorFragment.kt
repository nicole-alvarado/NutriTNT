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
import androidx.fragment.app.viewModels
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.navigateUp
import com.example.nutritnt.R
import com.example.nutritnt.data.DatosDatabase
import com.example.nutritnt.database.entities.Encuesta
import com.example.nutritnt.databinding.FragmentNuevaEncuestaAlimentoBinding
import com.example.nutritnt.databinding.FragmentUbicacionConsumidorBinding
import com.example.nutritnt.viewmodel.EncuestaViewModel
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.gms.maps.model.PolylineOptions
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlin.properties.Delegates


class UbicacionConsumidorFragment : Fragment(), OnMapReadyCallback {

    private lateinit var googleMap: GoogleMap
    private var marker: Marker? = null
    private lateinit var binding: FragmentUbicacionConsumidorBinding
    private lateinit var codigoParticipante: String
    private val encuestaViewModel : EncuestaViewModel by viewModels()
    private lateinit var encuestaGeneral: Encuesta
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Obtener el argumento pasado desde FragmentA
        arguments?.let {
            codigoParticipante = UbicacionConsumidorFragmentArgs.fromBundle(it).codigoParticipante
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentUbicacionConsumidorBinding.inflate(layoutInflater)
        val view = binding.root

        val mapFragment = childFragmentManager.findFragmentById(R.id.mapView) as SupportMapFragment
        mapFragment.getMapAsync(this)


        encuestaViewModel.getEncuestaByCodigoParticipante(codigoParticipante).observe(viewLifecycleOwner, Observer<Encuesta> { encuesta ->
            // Manipular la encuesta recibida aquí
            encuesta.let {
                encuestaGeneral = encuesta
            }
        })

        val myButton: Button = binding.sigButton
        myButton.setOnClickListener {
            marker?.let {
                val position = it.position
                val latitude = position.latitude
                val longitude = position.longitude

                val pos = determineQuadrant(position)

                encuestaGeneral.latitud = latitude.toString()
                encuestaGeneral.longitud = longitude.toString()
                encuestaGeneral.zona = "Zona $pos"

                actualizarYNavegar(encuestaGeneral)

                Log.i("zonaEncuesta", "id: ${encuestaGeneral.encuestaId} zona ${encuestaGeneral.zona} lat: ${encuestaGeneral.latitud} long: ${encuestaGeneral.longitud} ")

            }
        }



        return view
    }

    private fun actualizarYNavegar(encuesta: Encuesta) {
        // Llamar a la función update del ViewModel en una corrutina
        lifecycleScope.launch {
            encuestaViewModel.update(encuesta)
            // Realizar la navegación usando el componente de navegación
            findNavController().navigate(UbicacionConsumidorFragmentDirections.actionUbicacionConsumidorFragmentToListEncuestasAlimentosFragment(encuesta.encuestaId))
        }
    }


    override fun onMapReady(map: GoogleMap) {
        googleMap = map



        // Habilitar controles de zoom
        googleMap.uiSettings.isZoomControlsEnabled = true

        if (marker == null) {
            // Ubicación inicial
           val startPoint = LatLng(-42.775082, -65.047036)
            googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(startPoint, 14f))
            // Añadir un marcador inicial
            marker = googleMap.addMarker(MarkerOptions().position(startPoint).draggable(true))
        } else {
            val startPoint = LatLng(marker!!.position.latitude, marker!!.position.longitude)
            googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(startPoint, 15f))
        }


        googleMap.setOnMarkerDragListener(object : GoogleMap.OnMarkerDragListener {
            override fun onMarkerDragStart(marker: Marker) {
                // Código opcional al empezar a arrastrar
            }

            override fun onMarkerDrag(marker: Marker) {
                // Código opcional mientras se arrastra
            }

            override fun onMarkerDragEnd(marker: Marker) {
                // Hacer algo con la nueva posición del marcador

            }
        })


     //   drawSeparationLines()


    }

    private fun determineQuadrant(location: LatLng): Int {
        // Coordenadas de las líneas
        val horizontalLineStart = LatLng(-42.769412, -65.030643)
        val horizontalLineEnd = LatLng(-42.787398, -65.083944)
        val verticalLineStart = LatLng(-42.751162, -65.061959)
        val verticalLineEnd = LatLng(-42.790792, -65.036940)

        // Obtén los coeficientes de la ecuación de la línea (y = mx + b)
        val horizontalSlope = (horizontalLineEnd.latitude - horizontalLineStart.latitude) / (horizontalLineEnd.longitude - horizontalLineStart.longitude)
        val horizontalIntercept = horizontalLineStart.latitude - (horizontalSlope * horizontalLineStart.longitude)

        val verticalSlope = (verticalLineEnd.latitude - verticalLineStart.latitude) / (verticalLineEnd.longitude - verticalLineStart.longitude)
        val verticalIntercept = verticalLineStart.latitude - (verticalSlope * verticalLineStart.longitude)


        // Compara la posición de la ubicación con las líneas
        val aboveHorizontalLine = location.latitude > (horizontalSlope * location.longitude + horizontalIntercept)
        val rightOfVerticalLine = location.longitude > (location.latitude - verticalIntercept) / verticalSlope

        return when {
            aboveHorizontalLine && rightOfVerticalLine -> 1
            aboveHorizontalLine && !rightOfVerticalLine -> 2
            !aboveHorizontalLine && !rightOfVerticalLine -> 3
            else -> 4
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
        googleMap.addPolyline(PolylineOptions().addAll(verticalLine).color(Color.BLUE))
    }



}