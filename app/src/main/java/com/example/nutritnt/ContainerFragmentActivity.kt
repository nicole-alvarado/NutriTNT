package com.example.nutritnt


import android.app.Application
import android.content.res.Configuration
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
class ContainerFragmentActivity :  AppCompatActivity() {

        override fun onCreate(savedInstanceState: Bundle?) {
            super.onCreate(savedInstanceState)

            setContentView(R.layout.activity_container_fragment)

        }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        // Notifica al Fragment actual sobre el cambio de configuración
        supportFragmentManager.findFragmentById(R.id.fragmentContainer)
            ?.onConfigurationChanged(newConfig)
    }

}