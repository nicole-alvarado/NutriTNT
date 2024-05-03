package com.example.nutritnt

import android.os.Bundle
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.nutritnt.databinding.ActivityDetalleEncuestaBinding

class DetalleEncuestaActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetalleEncuestaBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding = ActivityDetalleEncuestaBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val idEncuesta = intent?.extras?.getString("ENCUESTA_ID").toString()
        val fecha = intent?.extras?.getString("FECHA").toString()
        val estado = intent?.extras?.getString("ESTADO").toString()

        Log.i("detalleEncuesta", idEncuesta)

        binding.idEncuestaValorTextView.text = idEncuesta
        binding.fechaValorTextView.text = fecha
        binding.estadoValorTextView.text = estado

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        Log.i("estados", "onCreate")
    }
}
