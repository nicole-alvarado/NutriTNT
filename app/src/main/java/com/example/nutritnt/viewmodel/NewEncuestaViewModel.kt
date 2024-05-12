package com.example.nutritnt.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

data class Selector(val text: String)

class NewEncuestaViewModel : ViewModel() {
    private var _id = MutableLiveData<Long>()
    private var _portion = MutableLiveData<String>()
    private var _period = MutableLiveData<String>()
    private var _frecuency = MutableLiveData<String>()

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




}