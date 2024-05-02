package com.example.nutritnt

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.nutritnt.databinding.ActivityListadoEncuestasBinding

class ListadoEncuestasActivity : AppCompatActivity() {

    private lateinit var binding: ActivityListadoEncuestasBinding
    private lateinit var listAdapter: ListaAdapterActivity

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding = ActivityListadoEncuestasBinding.inflate(layoutInflater)
        setContentView(binding.root)

        listAdapter = ListaAdapterActivity(getItemList(), this)
        binding.RecyclerViewEncuestas.layoutManager = LinearLayoutManager(this)
        binding.RecyclerViewEncuestas.adapter = listAdapter

        ViewCompat.setOnApplyWindowInsetsListener(binding.main) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    private fun getItemList(): List<ListaElementoActivity> {
        return mutableListOf(
            ListaElementoActivity("John", "New York", "Active"),
            ListaElementoActivity("Emily", "Los Angeles", "Inactive")
        )
    }
}
