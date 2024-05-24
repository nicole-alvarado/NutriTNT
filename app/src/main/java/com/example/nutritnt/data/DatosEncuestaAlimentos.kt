package com.example.nutritnt.data;

import com.example.nutritnt.database.entities.Encuesta_Alimento

data class Encuesta_Alimento_M(val id:Int, val portion: String, val period: String, val frecuency: Int, val encuestaID: Int, val zona: Int)

public class DatosEncuestaAlimentos {

    companion object{
        val datosConsumoYogur = listOf(
            Encuesta_Alimento_M(1,  "100",  "semana",  1, 1,1),
            Encuesta_Alimento_M( 2,  "200",  "semana",  1, 1,1),
            Encuesta_Alimento_M( 3,  "275",  "mes",  2, 1,1),
            Encuesta_Alimento_M( 4,  "100",  "dia",  4, 1,1),
            Encuesta_Alimento_M( 5,  "200",  "semana",  2, 1,2),
            Encuesta_Alimento_M( 6,  "275",  "dia",  1, 1,2),
            Encuesta_Alimento_M( 7,  "100",  "mes",  3, 1,2),
            Encuesta_Alimento_M( 8,  "200",  "dia",  2, 1,3),
            Encuesta_Alimento_M( 9,  "275",  "semana",  4, 1,3),
            Encuesta_Alimento_M( 10,  "100",  "mes",  1, 1,4),
            Encuesta_Alimento_M( 11,  "200",  "dia",  3, 1,4),
            Encuesta_Alimento_M( 12,  "275",  "semana",  2, 1,4),
        )
    }
}