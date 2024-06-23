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
    var estado: String,

    @ColumnInfo(name = "zona")
    var zona: String,

    @ColumnInfo(name = "codigoParticipante")
    var codigoParticipante: String,

    @ColumnInfo(name = "latitud")
    var latitud: String,

    @ColumnInfo(name = "longitud")
    var longitud: String,
)

fun Encuesta.toMap(): Map<String, Any> {
    return mapOf(
        "encuestaId" to encuestaId,
        "nombre" to nombre,
        "fecha" to fecha,
        "estado" to estado,
        "zona" to zona,
        "codigoParticipante" to codigoParticipante,
        "latitud" to latitud,
        "longitud" to longitud
    )
}