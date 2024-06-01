package com.example.nutritnt.database.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(tableName = "tabla_alimento",foreignKeys =
[
    ForeignKey(entity = InformacionNutricional::class,
        parentColumns = ["informacionNutricionalId"],
        childColumns = ["informacionNutricionalId"],
        onDelete = ForeignKey.CASCADE)])
data class Alimento (
    @PrimaryKey(autoGenerate = true)
    var alimentoId: Int = 0,

    @ColumnInfo(name = "descripcion")
    var descripcion: String,

    @ColumnInfo(name = "codigo")
    var codigo: String,

    @ColumnInfo(name = "grupo")
    var grupo: String,

    @ColumnInfo(name = "subgrupo")
    var subgrupo: String,

    @ColumnInfo(name = "informacionNutricionalId")
    var informacionNutricionalId: Int,
)