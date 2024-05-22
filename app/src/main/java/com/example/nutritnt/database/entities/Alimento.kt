package com.example.nutritnt.database.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "tabla_alimento")
data class Alimento (
    @PrimaryKey(autoGenerate = true)
    var alimentoId: Int = 0,

    @ColumnInfo(name = "descripcion")
    var descripcion: String,

    @ColumnInfo(name = "cantidad")
    var cantidad: Int,

    @ColumnInfo(name = "grasas_totales")
    var grasas_totales: Float,
)