package com.example.nutritnt.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class LoginViewModel : ViewModel(){

    private val _user = MutableLiveData<String>()
    private val _password = MutableLiveData<String>()

    val user: LiveData<String>
        get() = _user

    val password: LiveData<String>
        get() = _password

    // Establecer el valor del usuario
    fun setUser(username: String) {
        _user.value = username
    }

    // Establecer el valor de la contraseña
    fun setPassword(password: String) {
        _password.value = password
    }

    init {
        Log.i("ScoreViewModel", "Usuario: $user")
    }
}