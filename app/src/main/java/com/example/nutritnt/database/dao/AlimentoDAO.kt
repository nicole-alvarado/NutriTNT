package com.example.nutritnt.database.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.nutritnt.database.entities.Alimento

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
}