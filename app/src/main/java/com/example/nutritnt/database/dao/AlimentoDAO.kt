package com.example.nutritnt.database.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.example.nutritnt.database.entities.Alimento
import com.example.nutritnt.database.relations.AlimentoInformacionNutricional

@Dao
interface AlimentoDAO {
    @Query("SELECT * from tabla_alimento ORDER BY alimentoId ASC")
    fun getAlimentos(): LiveData<List<Alimento>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertar(alimento: Alimento)

    @Query("DELETE FROM tabla_alimento")
    suspend fun borrarTodos()

    @Query("SELECT COUNT(alimentoId) from tabla_alimento")
    suspend fun cantidadDeAlimentos(): Int

    @Query("""
        SELECT a.*
        FROM tabla_alimento a
        INNER JOIN tabla_encuesta_alimento ea ON a.alimentoId == ea.alimentoId
        WHERE ea.encuestaAlimentoId = :encuestaAlimentoId
    """)
    fun getAlimentoByEncuestaAlimentoId(encuestaAlimentoId: Int): LiveData<Alimento>

    @Transaction
    @Query("SELECT * FROM tabla_alimento")
    suspend fun obtenerAlimentosConInformacionNutricional(): List<AlimentoInformacionNutricional>

}