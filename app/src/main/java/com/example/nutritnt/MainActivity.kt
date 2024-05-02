package com.example.nutritnt

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.nutritnt.R
import com.example.nutritnt.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        //setContentView(R.layout.activity_main)

        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root

        binding.buttonIngresar.text = "Ingresar"

        setContentView(view)

        binding.buttonIngresar.setOnClickListener(){
            Log.d("Botones", "Botón ingresar clickeado")
            val context = binding.root.context
            val intent = Intent(context, WelcomeActivity::class.java)

            context.startActivity(intent) // Iniciar la actividad con el intent
        }

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        Log.i("estados","onCreate")
    }

    override fun onStart() {
        super.onStart()
        Log.i("estados","onStart")
    }

    override fun onResume() {
        super.onResume()
        Log.i("estados","onResume")
    }

    override fun onPause() {
        super.onPause()

        Log.i("estados","onPause")
    }
    override fun onStop() {
        super.onStop()

        Log.i("estados","onStop")
    }

    override fun onDestroy() {
        super.onDestroy()
        finish()
        Log.i("estados","onDestroy")
    }
}