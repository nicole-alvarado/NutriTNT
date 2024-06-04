package com.example.nutritnt.database.relations

import androidx.room.Embedded
import androidx.room.Relation
import com.example.nutritnt.database.entities.Alimento
import com.example.nutritnt.database.entities.InformacionNutricional

data class AlimentoInformacionNutricional(
    @Embedded val alimento: Alimento,
    @Relation(
    parentColumn = "informacionNutricionalId",
    entityColumn = "informacionNutricionalId"
)
val informacionNutricional: InformacionNutricional
)