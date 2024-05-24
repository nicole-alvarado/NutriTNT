package com.example.nutritnt.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.nutritnt.database.EncuestaRoomDatabase
import com.example.nutritnt.database.RepositorioDeEncuestasAlimento
import com.example.nutritnt.database.entities.Alimento
import com.example.nutritnt.database.entities.Encuesta
import com.example.nutritnt.database.entities.Encuesta_Alimento
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class EncuestaAlimentoViewModel(application: Application) : AndroidViewModel(application) {

    private val repositorio: RepositorioDeEncuestasAlimento
    // Usar LiveData y almacenar lo que devuelve getPartidos tiene bastantes beneficios:
    // - Podemos atar un observador a los datos (en lugar de estar verificando cambios continuamente)
    // y solo actualizar la UI cuando los datos cambien.
    // - El Repositorio está totalmente separado de la UI mediante el ViewModel.
    val todasLasEncuestasAlimento: LiveData<List<Encuesta_Alimento>>

    init {
        val encuestasAlimentoDao = EncuestaRoomDatabase
            .obtenerDatabase(application).encuestaAlimentoDao()
        repositorio = RepositorioDeEncuestasAlimento(encuestasAlimentoDao)
        todasLasEncuestasAlimento = repositorio.todasLasEncuestasAlimento
    }

    /**
     *
     * Lanzar una nueva corrutina para insertar datos en una forma no-bloqueante
     */
    fun insert(encuestaAlimento: Encuesta_Alimento) = viewModelScope.launch(Dispatchers.IO) {
        repositorio.insertar(encuestaAlimento)
    }

    fun getEncuestaAlimentosByZonaAndAlimento(zona: String, alimentoId: Int): LiveData<List<Encuesta_Alimento>> {
        val result = MutableLiveData<List<Encuesta_Alimento>>()
        viewModelScope.launch {
            result.value = repositorio.getEncuestaAlimentosByZonaAndAlimento(zona, alimentoId)
        }
        return result
    }

    suspend fun safeInsertMultiple(
        encuestasGeneral: List<Encuesta>,
        alimentos: List<Alimento>,
        encuestasAlimento: List<Encuesta_Alimento>
    ): Boolean {
        val encuestaDAO = EncuestaRoomDatabase
            .obtenerDatabase(getApplication()).encuestaDao()
        val alimentoDAO = EncuestaRoomDatabase
            .obtenerDatabase(getApplication()).alimentoDao()
        val encuestaAlimentoDAO = EncuestaRoomDatabase
            .obtenerDatabase(getApplication()).encuestaAlimentoDao()

        return EncuestaRoomDatabase.safeInsertMultiple(encuestasGeneral, alimentos, encuestasAlimento, encuestaDAO, encuestaAlimentoDAO, alimentoDAO)
    }
}