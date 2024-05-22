package com.example.nutritnt.database.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "tabla_encuesta")
data class Encuesta(
    @PrimaryKey(autoGenerate = true)
    var encuestaId: Int = 0,

    @ColumnInfo(name = "nombre")
    var nombre: String,

    @ColumnInfo(name = "fecha")
    var fecha: String,

    @ColumnInfo(name = "estado")
    var estado: String
)