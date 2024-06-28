package com.example.nutritnt


import android.content.res.Configuration
import android.os.Bundle
import android.util.Log
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.nutritnt.data.DatosDatabase
import com.example.nutritnt.data.ReadCSV
import com.example.nutritnt.database.entities.Alimento
import com.example.nutritnt.database.entities.InformacionNutricional
import com.example.nutritnt.viewmodel.EncuestaAlimentoViewModel
import kotlinx.coroutines.CoroutineScope

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainActivity :  AppCompatActivity() {

    private lateinit var encuestaAlimentoViewModel: EncuestaAlimentoViewModel

        override fun onCreate(savedInstanceState: Bundle?) {
            super.onCreate(savedInstanceState)

            setContentView(R.layout.activity_main)


            // Interceptar el botón de retroceso
            onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    // No hacer nada para deshabilitar el botón de retroceso
                }
            })

            encuestaAlimentoViewModel = ViewModelProvider(this).get(EncuestaAlimentoViewModel::class.java)

            CoroutineScope(Dispatchers.Main).launch {

                val encuestasGeneral = DatosDatabase.encuestas
                val alimentosList = mutableListOf<Alimento>()
                val informacionNutricionalList = mutableListOf<InformacionNutricional>()

                ReadCSV.readFromCSV(this@MainActivity, alimentosList, informacionNutricionalList)

                val encuestasAlimento = DatosDatabase.datosConsumoYogur
                val encuestadores = DatosDatabase.encuestadores
                val zonas = DatosDatabase.zonas
                encuestaAlimentoViewModel.safeInsertMultiple(encuestasGeneral, alimentosList, encuestasAlimento, encuestadores, zonas, informacionNutricionalList).let { exitoso ->
                        if (exitoso) {
                            Log.i("Insercion", "insercion Exitosa")
                        } else {
                            Log.i("Insercion", "Error de insercion")
                        }
                    }
            }




          //  CoroutineScope(Dispatchers.IO).launch {
           //     EncuestaRoomDatabase.cargarBaseDeDatos(encuestaDAO, encuestaAlimentoDAO, alimentoDAO)
            //}

            //val encuestaDAO = database.encuestaDao()
            //val alimentoDAO = database.alimentoDao()
            //val encuestaAlimentoDAO = database.encuestaAlimentoDao()

            //CoroutineScope(Dispatchers.IO).launch {
            //    EncuestaRoomDatabase.cargarBaseDeDatos(encuestaDAO, encuestaAlimentoDAO, alimentoDAO)
            //}

        }


    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        // Notifica al Fragment actual sobre el cambio de configuración
        supportFragmentManager.findFragmentById(R.id.fragmentContainer)
            ?.onConfigurationChanged(newConfig)
    }

}