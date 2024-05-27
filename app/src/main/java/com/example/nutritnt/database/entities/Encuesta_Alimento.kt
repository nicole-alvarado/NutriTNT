package com.example.nutritnt.database.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(tableName = "tabla_encuesta_alimento",
    foreignKeys =
    [
        ForeignKey(entity = Encuesta::class,
            parentColumns = ["encuestaId"],
            childColumns = ["encuestaId"],
            onDelete = ForeignKey.CASCADE),
        ForeignKey(entity = Alimento::class,
            parentColumns = ["alimentoId"],
            childColumns = ["alimentoId"],
            onDelete = ForeignKey.CASCADE)])

data class Encuesta_Alimento(
    @PrimaryKey(autoGenerate = true)
    var encuestaAlimentoId: Int = 0,

    @ColumnInfo(name = "portion")
    var portion: String,

    @ColumnInfo(name = "period")
    var period: String,

    @ColumnInfo(name = "frecuency")
    var frecuency: Int,

    @ColumnInfo(name = "encuestaId")
    var encuestaId: Int,

    @ColumnInfo(name = "alimentoId")
    var alimentoId: Int,

    @ColumnInfo(name = "estado")
    var estado: String,

)