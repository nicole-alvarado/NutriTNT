package com.example.nutritnt.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.example.nutritnt.database.EncuestaRoomDatabase
import com.example.nutritnt.database.RepositorioDeAlimentos
import com.example.nutritnt.database.entities.Alimento
import com.example.nutritnt.database.entities.Encuesta_Alimento
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class AlimentoViewModel (application: Application) : AndroidViewModel(application){

    private val repositorio: RepositorioDeAlimentos
    // Usar LiveData y almacenar lo que devuelve getPartidos tiene bastantes beneficios:
    // - Podemos atar un observador a los datos (en lugar de estar verificando cambios continuamente)
    // y solo actualizar la UI cuando los datos cambien.
    // - El Repositorio está totalmente separado de la UI mediante el ViewModel.
    val todosLosAlimentos: LiveData<List<Alimento>>

    init {
        val alimentosDao = EncuestaRoomDatabase
            .obtenerDatabase(application).alimentoDao()
        repositorio = RepositorioDeAlimentos(alimentosDao)
        todosLosAlimentos = repositorio.todosLosAlimentos
    }

    /**
     *
     * Lanzar una nueva corrutina para insertar datos en una forma no-bloqueante
     */
    fun insert(alimento: Alimento) = viewModelScope.launch(Dispatchers.IO) {
        repositorio.insertar(alimento)
    }

    fun fetchAlimentoByEncuestaAlimento(encuestaAlimentoId: Int): LiveData<Alimento> {
        return repositorio.getAlimentoByEncuestaAlimentoId(encuestaAlimentoId)
    }

}