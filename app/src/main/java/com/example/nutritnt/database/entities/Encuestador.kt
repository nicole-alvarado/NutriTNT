package com.example.nutritnt.database.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "tabla_encuestador")
data class Encuestador(
    @PrimaryKey(autoGenerate = true)
    var encuestadorId: Int = 0,

    @ColumnInfo(name = "email")
    var email: String,

    @ColumnInfo(name = "password")
    var password: Int,
)