package com.example.nutritnt.database.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.nutritnt.database.entities.Encuestador

@Dao
interface EncuestadorDAO {
    @Query("SELECT * from tabla_encuestador ORDER BY encuestadorId ASC")
    fun getEncuestadores(): LiveData<List<Encuestador>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertar(encuestador: Encuestador)

    @Query("DELETE FROM tabla_encuestador")
    suspend fun borrarTodos()

    @Query("SELECT COUNT(encuestadorId) from tabla_encuestador")
    suspend fun cantidadDeEncuestadores(): Int

    @Query("""
        SELECT e.*
        FROM tabla_encuestador e
        WHERE e.encuestadorId = :id
    """)
    fun getEncuestadorById(id: Int): LiveData<Encuestador>
}
