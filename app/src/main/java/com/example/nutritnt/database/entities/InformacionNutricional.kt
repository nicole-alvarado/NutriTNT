package com.example.nutritnt.database.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "tabla_informacion_nutricional")
class InformacionNutricional(
    @PrimaryKey(autoGenerate = true)
    var informacionNutricionalId: Int = 0,

    @ColumnInfo(name = "cantidadGramos")
    var cantidadGramos: Float,

    @ColumnInfo(name = "kcalTotales")
    var kcalTotales: Float,

    @ColumnInfo(name = "carbohidratos")
    var carbohidratos: Float,

    @ColumnInfo(name = "proteinas")
    var proteinas: Float,

    @ColumnInfo(name = "grasasTotales")
    var grasasTotales: Float,

    @ColumnInfo(name = "alcohol")
    var alcohol: Float,

    @ColumnInfo(name = "colesterol")
    var colesterol: Float,

    @ColumnInfo(name = "fibra")
    var fibra: Float,
)