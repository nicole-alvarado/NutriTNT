package com.example.nutritnt.database.dao

import androidx.lifecycle.LiveData
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.nutritnt.database.entities.Encuesta_Alimento

interface Encuesta_AlimentoDAO {

    @Query("SELECT * from tabla_encuesta_alimento ORDER BY encuestaAlimentoId ASC")
    fun getEncuestasAlimento(): LiveData<List<Encuesta_Alimento>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertar(encuesta_alimento: Encuesta_Alimento)

    @Query("DELETE FROM tabla_encuesta_alimento")
    suspend fun borrarTodos()

    @Query("SELECT COUNT(encuestaAlimentoId) from tabla_encuesta_alimento")
    suspend fun cantidadDeEncuestasAlimento(): Int
}