package com.example.nutritnt.database.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import com.example.nutritnt.database.entities.Encuesta
import com.example.nutritnt.database.entities.EncuestaAlimento
import com.example.nutritnt.database.relations.EncuestaAlimento_AlimentoInformacionNutricional
import kotlinx.coroutines.flow.Flow

@Dao
interface EncuestaAlimentoDAO {

    @Query("SELECT * from tabla_encuesta_alimento ORDER BY encuestaAlimentoId ASC")
    fun getEncuestasAlimento(): LiveData<List<EncuestaAlimento>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertar(encuesta_alimento: EncuestaAlimento)

    @Update
    suspend fun actualizar(encuestaAlimento: EncuestaAlimento)

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
    fun getEncuestaAlimentosByZonaAndAlimento(zona: String, alimentoId: Int): List<EncuestaAlimento>


    @Query("""
        SELECT ea.* 
        FROM tabla_encuesta_alimento ea
        INNER JOIN tabla_encuesta eg ON ea.encuestaId = eg.encuestaId
        WHERE eg.zona = :zona
    """)
    fun getEncuestaAlimentosByZona(zona: String): List<EncuestaAlimento>

    @Transaction
    @Query("""
        SELECT ea.* 
        FROM tabla_encuesta_alimento ea
        INNER JOIN tabla_encuesta eg ON ea.encuestaId = eg.encuestaId
        WHERE eg.zona = :zona AND ea.estado="COMPLETADA"
    """)
    suspend fun getEncuestasAlimentosConInfo(zona: String): List<EncuestaAlimento_AlimentoInformacionNutricional>

    @Query("""
        SELECT ea.* 
        FROM tabla_encuesta_alimento ea
        WHERE ea.encuestaId = :encuestaId AND ea.alimentoId = :alimentoId
    """)
    fun getEncuestaAlimentoByEncuestaAndAlimento(encuestaId: Int, alimentoId: Int): LiveData<EncuestaAlimento>

    @Query("""
        SELECT ea.*
        FROM tabla_encuesta_alimento ea
        WHERE ea.encuestaId = :id
    """)
    fun getEncuestasAlimentosByEncuestaId(id: Int): LiveData<List<EncuestaAlimento>>

    @Query("""
        SELECT ea.* 
        FROM tabla_encuesta_alimento ea
        WHERE ea.encuestaAlimentoId = :id
    """)
    fun getEncuestaAlimentoById(id: Int): LiveData<EncuestaAlimento>

    @Query("""
        SELECT ea.* 
        FROM tabla_encuesta_alimento ea
        INNER JOIN tabla_encuesta e ON e.encuestaId = ea.encuestaId
        WHERE e.codigoParticipante = :codigoParticipante
    """)
    suspend fun getEncuestaAlimentosByCodigoParticipante(codigoParticipante: String): List<EncuestaAlimento>

    @Query("""
        SELECT ea.*, a.*, i.*
        FROM tabla_encuesta_alimento ea
        INNER JOIN tabla_alimento a ON a.alimentoId = ea.alimentoId
        INNER JOIN tabla_informacion_nutricional i ON i.informacionNutricionalId = a.informacionNutricionalId
        INNER JOIN tabla_encuesta e ON e.encuestaId = ea.encuestaId
        WHERE e.codigoParticipante = :codigoParticipante
    """)
    fun getEncuestaAlimentosWithAlimentoAndInfoByCodigoParticipante(codigoParticipante: String): Flow<List<EncuestaAlimento_AlimentoInformacionNutricional>>


}