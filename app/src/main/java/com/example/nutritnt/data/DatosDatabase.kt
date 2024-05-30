package com.example.nutritnt.data

import com.example.nutritnt.database.entities.Alimento
import com.example.nutritnt.database.entities.Encuesta
import com.example.nutritnt.database.entities.Encuesta_Alimento
import com.example.nutritnt.database.entities.Encuestador
import com.example.nutritnt.database.entities.InformacionNutricional
import com.example.nutritnt.database.entities.Zona

class DatosDatabase {

    companion object{
        val datosConsumoYogur = listOf(
            Encuesta_Alimento(1,  "100",  "semana",  1, 1,1,"Finalizada"),
            Encuesta_Alimento( 2,  "200",  "semana",  1, 2,1,"Finalizada"),
            Encuesta_Alimento( 3,  "275",  "mes",  2, 3,1,"Finalizada"),
            Encuesta_Alimento( 4,  "100",  "dia",  4, 4,1,"Finalizada"),
            Encuesta_Alimento( 5,  "200",  "semana",  2, 5,1,"Finalizada"),
            Encuesta_Alimento( 6,  "275",  "dia",  1, 6,1,"Finalizada"),
            Encuesta_Alimento( 7,  "100",  "mes",  3, 7,1,"Finalizada"),
            Encuesta_Alimento( 8,  "200",  "dia",  2, 8,1,"Finalizada"),
            Encuesta_Alimento( 9,  "275",  "semana",  4, 9,1,"Finalizada"),
            Encuesta_Alimento( 10,  "100",  "mes",  1, 10,1,"Finalizada"),
            Encuesta_Alimento( 11,  "200",  "dia",  3, 11,1,"Finalizada"),
            Encuesta_Alimento( 12,  "275",  "semana",  2, 12,1,"Finalizada"),
        )

        val encuestas = listOf(
            Encuesta(1, "ID-123", "21-05-2024", "Finalizada", "Zona A","codigo","latitud","longitud"),
            Encuesta(2, "ID-890", "22-05-2024", "Finalizada", "Zona C","codigo","latitud","longitud"),
            Encuesta(3, "ID-296", "23-05-2024", "Finalizada", "Zona D","codigo","latitud","longitud"),
            Encuesta(4, "ID-719", "15-04-2024", "Finalizada", "Zona B","codigo","latitud","longitud"),
            Encuesta(5, "ID-655", "16-04-2024", "Comenzada", "Zona A","codigo","latitud","longitud"),
            Encuesta(6, "ID-333", "21-05-2024", "Comenzada", "Zona D","codigo","latitud","longitud"),
            Encuesta(7, "ID-978", "22-05-2024", "Comenzada", "Zona D","codigo","latitud","longitud"),
            Encuesta(8, "ID-246", "23-05-2024", "Comenzada", "Zona C","codigo","latitud","longitud"),
            Encuesta(9, "ID-367", "15-04-2024", "Finalizada", "Zona B","codigo","latitud","longitud"),
            Encuesta(10, "ID-518", "16-04-2024", "Comenzada", "Zona A","codigo","latitud","longitud"),
            Encuesta(11, "ID-332", "21-05-2024", "Comenzada", "Zona A","codigo","latitud","longitud"),
            Encuesta(12, "ID-796", "22-05-2024", "Comenzada", "Zona C","codigo","latitud","longitud"),
            Encuesta(13, "ID-447", "23-05-2024", "Comenzada", "Zona C","codigo","latitud","longitud"),
            Encuesta(14, "ID-517", "15-04-2024", "Finalizada", "Zona B","codigo","latitud","longitud"),
            Encuesta(15, "ID-642", "16-04-2024", "Comenzada", "Zona A","codigo","latitud","longitud")
        )

        val alimentos = listOf(
            Alimento(alimentoId = 1, "Yogur bebible", 100, 4f)
        )

        val encuestadores = listOf(
            Encuestador(encuestadorId = 1, "correo@gmail.com","1234")
        )

        val zonas = listOf(
            Zona(1,"sur","norte","este","oeste")
        )

        val informacionNutricional = listOf(
            InformacionNutricional(1,0F,0F,0F,0F,0F,0F,0F,0F)
        )

    }
}