package com.example.nutritnt.viewmodel

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
<<<<<<< HEAD
import androidx.lifecycle.liveData
=======
>>>>>>> feature/listado-encuestas-alimentos
import androidx.lifecycle.viewModelScope
import com.example.nutritnt.database.EncuestaRoomDatabase
import com.example.nutritnt.database.RepositorioDeEncuestasAlimento
import com.example.nutritnt.database.entities.Alimento
import com.example.nutritnt.database.entities.Encuesta
import com.example.nutritnt.database.entities.Encuesta_Alimento
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
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

    /*
    fun getEncuestaAlimentosByZonaAndAlimento(alimentoId: Int): LiveData<List<Encuesta_Alimento>> {
        return liveData {
            val result = repositorio.getEncuestaAlimentosByZonaAndAlimento(alimentoId)
            emit(result)
        }
    }*/

    // StateFlow para la consulta
    private val _encuestaAlimentos = MutableStateFlow<List<Encuesta_Alimento>>(emptyList())
    val encuestaAlimentos: StateFlow<List<Encuesta_Alimento>> = _encuestaAlimentos

    fun fetchEncuestaAlimentosByZonaAndAlimento(zona: String, alimentoId: Int): Flow<List<Encuesta_Alimento>> {
        return flow {
            val datos = repositorio.getEncuestaAlimentosByZonaAndAlimento(zona, alimentoId)
            emit(datos)
        }.flowOn(Dispatchers.IO)
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