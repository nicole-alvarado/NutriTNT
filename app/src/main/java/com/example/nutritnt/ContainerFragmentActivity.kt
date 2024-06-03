package com.example.nutritnt


import android.content.res.Configuration
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.example.nutritnt.data.DatosDatabase
import com.example.nutritnt.data.ReadCSV
import com.example.nutritnt.database.entities.Alimento
import com.example.nutritnt.database.entities.InformacionNutricional
import com.example.nutritnt.viewmodel.EncuestaAlimentoViewModel
import kotlinx.coroutines.CoroutineScope

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ContainerFragmentActivity :  AppCompatActivity() {

    private lateinit var encuestaAlimentoViewModel: EncuestaAlimentoViewModel

        override fun onCreate(savedInstanceState: Bundle?) {
            super.onCreate(savedInstanceState)

            setContentView(R.layout.activity_container_fragment)

            // Inicialización de la base de datos y carga de datos de ejemplo
            //val database = EncuestaRoomDatabase.obtenerDatabase(applicationContext)

           // val encuestaDAO = database.encuestaDao()
           // val alimentoDAO = database.alimentoDao()
           // val encuestaAlimentoDAO = database.encuestaAlimentoDao()

            encuestaAlimentoViewModel = ViewModelProvider(this).get(EncuestaAlimentoViewModel::class.java)
                var x = 0F
            CoroutineScope(Dispatchers.Main).launch {
                Log.i("xvalue", "Entro en el scrope:" + x)
                x+=1F

                val encuestasGeneral = DatosDatabase.encuestas
                //val alimentos = DatosDatabase.alimentos
                val alimentosList = mutableListOf<Alimento>()
                val informacionNutricionalList = mutableListOf<InformacionNutricional>()

                ReadCSV.readFromCSV(this@ContainerFragmentActivity, alimentosList, informacionNutricionalList)

                val encuestasAlimento = DatosDatabase.datosConsumoYogur
                val encuestadores = DatosDatabase.encuestadores
                val zonas = DatosDatabase.zonas
                Log.i("xvalue", "va a entrar en el insert:" + x + " tamaño de alimentosList " + alimentosList.size)
                encuestaAlimentoViewModel.safeInsertMultiple(encuestasGeneral, alimentosList, encuestasAlimento, encuestadores, zonas, informacionNutricionalList).let { exitoso ->
                        if (exitoso) {
                            Log.i("Insercion", "insercion Exitosa")
                        } else {
                            Log.i("Insercion", "insercion Desastrosa")
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