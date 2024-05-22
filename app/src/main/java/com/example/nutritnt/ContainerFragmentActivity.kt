package com.example.nutritnt


import android.app.Application
import android.content.res.Configuration
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.nutritnt.database.EncuestaRoomDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ContainerFragmentActivity :  AppCompatActivity() {

        override fun onCreate(savedInstanceState: Bundle?) {
            super.onCreate(savedInstanceState)

            setContentView(R.layout.activity_container_fragment)

            // Inicialización de la base de datos y carga de datos de ejemplo
            val database = EncuestaRoomDatabase.obtenerDatabase(applicationContext)
            val encuestaDAO = database.encuestaDao()
            val alimentoDAO = database.alimentoDao()
            val encuestaAlimentoDAO = database.encuestaAlimentoDao()

            CoroutineScope(Dispatchers.IO).launch {
                EncuestaRoomDatabase.cargarBaseDeDatos(encuestaDAO, encuestaAlimentoDAO, alimentoDAO)
            }

        }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        // Notifica al Fragment actual sobre el cambio de configuración
        supportFragmentManager.findFragmentById(R.id.fragmentContainer)
            ?.onConfigurationChanged(newConfig)
    }

}