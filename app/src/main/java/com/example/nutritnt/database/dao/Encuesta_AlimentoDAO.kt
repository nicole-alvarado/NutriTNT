package com.example.nutritnt.database.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.example.nutritnt.database.entities.Encuesta_Alimento
import com.example.nutritnt.database.relations.EncuestaAlimento_AlimentoInformacionNutricional

@Dao
interface Encuesta_AlimentoDAO {

    @Query("SELECT * from tabla_encuesta_alimento ORDER BY encuestaAlimentoId ASC")
    fun getEncuestasAlimento(): LiveData<List<Encuesta_Alimento>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertar(encuesta_alimento: Encuesta_Alimento)

    @Query("DELETE FROM tabla_encuesta_alimento")
    suspend fun borrarTodos()

    @Query("SELECT COUNT(encuestaAlimentoId) from tabla_encuesta_alimento")
    suspend fun cantidadDeEncuestasAlimento(): Int

    @Query("""
        SELECT ea.* 
        FROM tabla_encuesta_alimento ea
        INNER JOIN tabla_encuesta eg ON ea.encuestaId = eg.encuestaId
        WHERE eg.zona = :zona AND ea.alimentoId = :alimentoId
    """)
    fun getEncuestaAlimentosByZonaAndAlimento(zona: String, alimentoId: Int): List<Encuesta_Alimento>


    @Query("""
        SELECT ea.* 
        FROM tabla_encuesta_alimento ea
        INNER JOIN tabla_encuesta eg ON ea.encuestaId = eg.encuestaId
        WHERE eg.zona = :zona
    """)
    fun getEncuestaAlimentosByZona(zona: String): List<Encuesta_Alimento>

    @Transaction
    @Query("""
        SELECT ea.* 
        FROM tabla_encuesta_alimento ea
        INNER JOIN tabla_encuesta eg ON ea.encuestaId = eg.encuestaId
        WHERE eg.zona = :zona
    """)
    suspend fun getEncuestasAlimentosConInfo(zona: String): List<EncuestaAlimento_AlimentoInformacionNutricional>



}