package com.example.nutritnt.database.relations

import androidx.room.Embedded
import androidx.room.Relation
import com.example.nutritnt.database.entities.Alimento
import com.example.nutritnt.database.entities.Encuesta_Alimento

data class EncuestaAlimento_AlimentoInformacionNutricional(
    @Embedded val encuestaAlimentos: Encuesta_Alimento,
    @Relation(
        parentColumn = "alimentoId",
        entityColumn = "alimentoId",
        entity = Alimento::class
    )
    val alimentoInformacionNutricional: AlimentoInformacionNutricional
)
