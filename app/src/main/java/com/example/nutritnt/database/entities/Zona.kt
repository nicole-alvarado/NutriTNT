package com.example.nutritnt.database.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "tabla_zona")
data class Zona(
    @PrimaryKey(autoGenerate = true)
    var zonaId: Int = 0,

    @ColumnInfo(name = "limiteSur")
    var limiteSur: String,

    @ColumnInfo(name = "limiteNorte")
    var limiteNorte: String,

    @ColumnInfo(name = "limiteEste")
    var limiteEste: String,

    @ColumnInfo(name = "limiteOeste")
    var limiteOeste: String,
)