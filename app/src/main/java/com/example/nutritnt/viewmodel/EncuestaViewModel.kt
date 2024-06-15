package com.example.nutritnt.viewmodel

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.nutritnt.database.EncuestaRoomDatabase
import com.example.nutritnt.database.RepositorioDeEncuestas
import com.example.nutritnt.database.entities.Encuesta
import androidx.lifecycle.viewModelScope
import com.example.nutritnt.database.entities.Alimento
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class EncuestaViewModel (application: Application) : AndroidViewModel(application){

    private val repositorio: RepositorioDeEncuestas
    // Usar LiveData y almacenar lo que devuelve getPartidos tiene bastantes beneficios:
    // - Podemos atar un observador a los datos (en lugar de estar verificando cambios continuamente)
    // y solo actualizar la UI cuando los datos cambien.
    // - El Repositorio está totalmente separado de la UI mediante el ViewModel.
    val todasLasEncuestas: LiveData<List<Encuesta>>
    private val _zonas = MutableLiveData<List<String>>()
    val zonas: LiveData<List<String>> = _zonas

    init {
        val encuestasDao = EncuestaRoomDatabase
            .obtenerDatabase(application).encuestaDao()
        repositorio = RepositorioDeEncuestas(encuestasDao)
        todasLasEncuestas = repositorio.todasLasEncuestas
    }

    /**
     *
     * Lanzar una nueva corrutina para insertar datos en una forma no-bloqueante
     */
    fun insert(encuesta: Encuesta) = viewModelScope.launch(Dispatchers.IO) {
        repositorio.insertar(encuesta)
    }

    suspend fun update(encuesta: Encuesta): Boolean {
        return withContext(Dispatchers.IO) {
            repositorio.actualizar(encuesta)
            // Aquí puedes verificar si la actualización fue exitosa y devolver true/false
            true // Por ejemplo, siempre asumimos que la actualización fue exitosa
        }
    }
    suspend fun obtenerZonasDistintas(): List<String> {
        return repositorio.getZonas()
    }

    fun getEncuestaByCodigoParticipante(codigo: String): LiveData<Encuesta> {
        return repositorio.getEncuestaByCodigoParticipante(codigo)
    }



}