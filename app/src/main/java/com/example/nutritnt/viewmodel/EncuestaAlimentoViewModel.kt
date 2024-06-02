package com.example.nutritnt.viewmodel

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.liveData
import androidx.lifecycle.viewModelScope
import com.example.nutritnt.database.EncuestaRoomDatabase
import com.example.nutritnt.database.RepositorioDeEncuestasAlimento
import com.example.nutritnt.database.entities.Alimento
import com.example.nutritnt.database.entities.Encuesta
import com.example.nutritnt.database.entities.Encuesta_Alimento
import com.example.nutritnt.database.entities.Encuestador
import com.example.nutritnt.database.entities.InformacionNutricional
import com.example.nutritnt.database.entities.Zona
import com.example.nutritnt.database.relations.EncuestaAlimento_AlimentoInformacionNutricional
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

    private val _encuestasAlimentosConInfo = MutableLiveData<Map<String, List<EncuestaAlimento_AlimentoInformacionNutricional>>>()
    val encuestasAlimentosConInfo: LiveData<Map<String, List<EncuestaAlimento_AlimentoInformacionNutricional>>> get() = _encuestasAlimentosConInfo


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


    fun fetchEncuestaAlimentosByZonaAndAlimento(zona: String, alimentoId: Int): Flow<List<Encuesta_Alimento>> {
        return flow {
            val datos = repositorio.getEncuestaAlimentosByZonaAndAlimento(zona, alimentoId)
            emit(datos)
        }.flowOn(Dispatchers.IO)
    }

    fun fetchEncuestaAlimentosByZona(zona: String): Flow<List<Encuesta_Alimento>> {
        return flow {
            val datos = repositorio.getEncuestaAlimentosByZona(zona)
            emit(datos)
        }.flowOn(Dispatchers.IO)
    }


    fun fetchEncuestasAlimentosConInfo(zonas: List<String>) {
        viewModelScope.launch {
            val dataMap = mutableMapOf<String, List<EncuestaAlimento_AlimentoInformacionNutricional>>()
            zonas.forEach { zona ->
                val data = repositorio.getEncuestasAlimentosConInfo(zona)
                dataMap[zona] = data
            }
            _encuestasAlimentosConInfo.value = dataMap
        }
    }



    suspend fun safeInsertMultiple(
        encuestasGeneral: List<Encuesta>,
        alimentos: List<Alimento>,
        encuestasAlimento: List<Encuesta_Alimento>,
        encuestadores: List<Encuestador>,
        zonas: List<Zona>,
        listaInformacionNutricional: List<InformacionNutricional>
    ): Boolean {
        val encuestaDAO = EncuestaRoomDatabase
            .obtenerDatabase(getApplication()).encuestaDao()
        val alimentoDAO = EncuestaRoomDatabase
            .obtenerDatabase(getApplication()).alimentoDao()
        val encuestaAlimentoDAO = EncuestaRoomDatabase
            .obtenerDatabase(getApplication()).encuestaAlimentoDao()
        val encuestadorDAO = EncuestaRoomDatabase
            .obtenerDatabase(getApplication()).encuestadorDao()
        val zonaDAO = EncuestaRoomDatabase
            .obtenerDatabase(getApplication()).zonaDao()
        val informacionNutricionalDAO = EncuestaRoomDatabase
            .obtenerDatabase(getApplication()).informacionNutricionalDao()

        return EncuestaRoomDatabase.safeInsertMultiple(encuestasGeneral, alimentos, encuestasAlimento, encuestadores, zonas, listaInformacionNutricional,
            encuestaDAO, encuestaAlimentoDAO, alimentoDAO, encuestadorDAO, zonaDAO, informacionNutricionalDAO)
    }
}