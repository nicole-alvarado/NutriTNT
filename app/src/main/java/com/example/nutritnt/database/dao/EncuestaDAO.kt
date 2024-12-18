package com.example.nutritnt.database.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.nutritnt.database.entities.Encuesta
import com.example.nutritnt.database.entities.Encuesta_Alimento

@Dao
interface EncuestaDAO {
    // LiveData como ya hemos visto anteriormente va a contener nuestros
    // datos y permitir que estos sean observados dentro de un ciclo de vida dado.
    // Siempre guarda / almacena en caché la última versión de los datos.
    // Notifica a sus observadores activos cuando los datos han cambiado.
    // Dado que estamos obteniendo el contenido completo de la base de datos,
    // se nos notifica cada vez que algun dato haya cambiado.
    @Query("SELECT * from tabla_encuesta ORDER BY encuestaId ASC")
    fun getEncuestas(): LiveData<List<Encuesta>>


    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertar(encuesta: Encuesta)

    @Query("SELECT DISTINCT zona FROM tabla_encuesta")
    suspend fun getAllZonas(): List<String>

    @Query("DELETE FROM tabla_encuesta")
    suspend fun borrarTodos()

    @Query("SELECT COUNT(encuestaId) from tabla_encuesta")
    suspend fun cantidadDeEncuestas(): Int

    @Update
    suspend fun actualizar(encuesta: Encuesta)

    @Query("""
        SELECT e.* 
        FROM tabla_encuesta e
        WHERE e.codigoParticipante = :codigo
    """)
    fun getEncuestaByCodigoParticipante(codigo: String): LiveData<Encuesta>

}