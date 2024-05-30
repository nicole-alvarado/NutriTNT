package com.example.nutritnt.database.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.nutritnt.database.entities.InformacionNutricional

@Dao
interface InformacionNutricionalDAO {
    @Query("SELECT * from tabla_informacion_nutricional ORDER BY informacionNutricionalId ASC")
    fun getInformacionesNutricionales(): LiveData<List<InformacionNutricional>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertar(informacionNutricional: InformacionNutricional)

    @Query("DELETE FROM tabla_informacion_nutricional")
    suspend fun borrarTodos()

    @Query("SELECT COUNT(informacionNutricionalId) from tabla_informacion_nutricional")
    suspend fun cantidadDeInformacionesNutricionales(): Int

    @Query("""
        SELECT i.*
        FROM tabla_informacion_nutricional i
        WHERE i.informacionNutricionalId = :id
    """)
    fun getInformacionNutricionalById(id: Int): LiveData<InformacionNutricional>
}