package com.example.nutritnt


import android.app.Application
import android.content.res.Configuration
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.example.nutritnt.data.DatosDatabase
import com.example.nutritnt.database.EncuestaRoomDatabase
import com.example.nutritnt.viewmodel.EncuestaAlimentoViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ContainerFragmentActivity :  AppCompatActivity() {

    private lateinit var encuestaAlimentoViewModel: EncuestaAlimentoViewModel

        override fun onCreate(savedInstanceState: Bundle?) {
            super.onCreate(savedInstanceState)

            setContentView(R.layout.activity_container_fragment)

            // Inicialización de la base de datos y carga de datos de ejemplo
            val database = EncuestaRoomDatabase.obtenerDatabase(applicationContext)
           // val encuestaDAO = database.encuestaDao()
           // val alimentoDAO = database.alimentoDao()
           // val encuestaAlimentoDAO = database.encuestaAlimentoDao()

            encuestaAlimentoViewModel = ViewModelProvider(this).get(EncuestaAlimentoViewModel::class.java)

            lifecycleScope.launch {
                val encuestasGeneral = DatosDatabase.encuestas
                val alimentos = DatosDatabase.alimentos
                val encuestasAlimento = DatosDatabase.datosConsumoYogur

                encuestaAlimentoViewModel.safeInsertMultiple(encuestasGeneral, alimentos, encuestasAlimento).let { exitoso ->
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

        }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        // Notifica al Fragment actual sobre el cambio de configuración
        supportFragmentManager.findFragmentById(R.id.fragmentContainer)
            ?.onConfigurationChanged(newConfig)
    }

}