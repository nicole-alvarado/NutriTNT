package com.example.nutritnt

import android.util.Log

data class ListElementFragment(val nombre: String, val fecha: String, val estado: String){
    init {
        Log.i("enListElementFragment", "Se ha creado un objeto ListaElementoActivity con nombre: $nombre, fecha: $fecha y estado: $estado")
    }
}

