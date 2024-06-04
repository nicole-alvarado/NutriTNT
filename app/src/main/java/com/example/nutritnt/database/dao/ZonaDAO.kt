package com.example.nutritnt.database.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.nutritnt.database.entities.Zona

@Dao
interface ZonaDAO {
    @Query("SELECT * from tabla_zona ORDER BY zonaId ASC")
    fun getZonas(): LiveData<List<Zona>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertar(zona: Zona)

    @Query("DELETE FROM tabla_zona")
    suspend fun borrarTodos()

    @Query("SELECT COUNT(zonaId) from tabla_zona")
    suspend fun cantidadDeZonas(): Int

    @Query("""
        SELECT z.*
        FROM tabla_zona z
        WHERE z.zonaId = :id
    """)
    fun getZonaById(id: Int): LiveData<Zona>
}