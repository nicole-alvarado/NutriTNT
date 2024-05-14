package com.example.nutritnt.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

data class Selector(val text: String)

class NewEncuestaViewModel : ViewModel() {
    private var _portion = MutableLiveData<String>()
    private var _period = MutableLiveData<String>()
    private var _frecuency = MutableLiveData<Int>()

    val portion = listOf(
        Selector("100ml"),
        Selector( "200ml"),
        Selector("275ml")
    )

    val period = listOf(
        Selector("dia"),
        Selector( "semana"),
        Selector("mes"),
        Selector("año")
    )

    val frecuency: LiveData<Int>
        get() = _frecuency

    // Establecer el valor del usuario
    fun setFrecuency(frecuency: String) {
        _frecuency.value = frecuency.toInt()
        Log.d("enNewEncuestaViewModel", "Frecuencia actualizada: $frecuency")
    }



}