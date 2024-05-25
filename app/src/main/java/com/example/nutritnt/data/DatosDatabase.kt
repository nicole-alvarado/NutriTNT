package com.example.nutritnt.data

import com.example.nutritnt.database.entities.Alimento
import com.example.nutritnt.database.entities.Encuesta
import com.example.nutritnt.database.entities.Encuesta_Alimento

class DatosDatabase {

    companion object{
        val datosConsumoYogur = listOf(
            Encuesta_Alimento(1,  "100",  "semana",  1, 1,1),
            Encuesta_Alimento( 2,  "200",  "semana",  1, 2,1),
            Encuesta_Alimento( 3,  "275",  "mes",  2, 3,1),
            Encuesta_Alimento( 4,  "100",  "dia",  4, 4,1),
            Encuesta_Alimento( 5,  "200",  "semana",  2, 5,1),
            Encuesta_Alimento( 6,  "275",  "dia",  1, 6,1),
            Encuesta_Alimento( 7,  "100",  "mes",  3, 7,1),
            Encuesta_Alimento( 8,  "200",  "dia",  2, 8,1),
            Encuesta_Alimento( 9,  "275",  "semana",  4, 9,1),
            Encuesta_Alimento( 10,  "100",  "mes",  1, 10,1),
            Encuesta_Alimento( 11,  "200",  "dia",  3, 11,1),
            Encuesta_Alimento( 12,  "275",  "semana",  2, 12,1),
        )

        val encuestas = listOf(
            Encuesta(1, "nombre1", "21-05-2024", "ACTIVA", "Zona A"),
            Encuesta(2, "nombre2", "22-05-2024", "ACTIVA", "Zona C"),
            Encuesta(3, "nombre3", "23-05-2024", "ACTIVA", "Zona D"),
            Encuesta(4, "nombre4", "15-04-2024", "Finalizada", "Zona B"),
            Encuesta(5, "nombre5", "16-04-2024", "Comenzada", "Zona A"),
            Encuesta(6, "nombre6", "21-05-2024", "ACTIVA", "Zona A"),
            Encuesta(7, "nombre7", "22-05-2024", "ACTIVA", "Zona C"),
            Encuesta(8, "nombre8", "23-05-2024", "ACTIVA", "Zona C"),
            Encuesta(9, "nombre9", "15-04-2024", "Finalizada", "Zona B"),
            Encuesta(10, "nombre10", "16-04-2024", "Comenzada", "Zona A"),
            Encuesta(11, "nombre11", "21-05-2024", "ACTIVA", "Zona A"),
            Encuesta(12, "nombre12", "22-05-2024", "ACTIVA", "Zona C"),
            Encuesta(13, "nombre13", "23-05-2024", "ACTIVA", "Zona D"),
            Encuesta(14, "nombre14", "15-04-2024", "Finalizada", "Zona B"),
            Encuesta(15, "nombre15", "16-04-2024", "Comenzada", "Zona A"),
        )

        val alimentos = listOf(
            Alimento(alimentoId = 1, "Yogur bebible", 100, 4f)
        )
    }
}