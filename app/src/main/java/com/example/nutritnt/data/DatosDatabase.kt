package com.example.nutritnt.data

import com.example.nutritnt.R
import com.example.nutritnt.database.entities.Encuesta
import com.example.nutritnt.database.entities.EncuestaAlimento
import com.example.nutritnt.database.entities.Encuestador
import com.example.nutritnt.database.entities.Zona

data class Portion(val portions: List<String>, val imgsPortions: List<Int>, val alimentoID: Int)

class DatosDatabase {

    companion object{
        val datosConsumoYogur = listOf(
            EncuestaAlimento(1,  "100",  "semana",  1, 1,1,"Finalizada"),
            EncuestaAlimento( 2,  "200",  "semana",  1, 2,1,"Finalizada"),
            EncuestaAlimento( 3,  "275",  "mes",  2, 3,1,"Finalizada"),
            EncuestaAlimento( 4,  "100",  "dia",  4, 4,1,"Finalizada"),
            EncuestaAlimento( 5,  "200",  "semana",  2, 5,1,"Finalizada"),
            EncuestaAlimento( 6,  "275",  "dia",  1, 6,1,"Finalizada"),
            EncuestaAlimento( 7,  "100",  "mes",  3, 7,1,"Finalizada"),
            EncuestaAlimento( 8,  "200",  "dia",  2, 8,1,"Finalizada"),
            EncuestaAlimento( 9,  "275",  "semana",  4, 9,1,"Finalizada"),
            EncuestaAlimento( 10,  "100",  "mes",  1, 10,1,"Finalizada"),
            EncuestaAlimento( 11,  "200",  "dia",  3, 11,1,"Finalizada"),
            EncuestaAlimento( 12,  "275",  "semana",  2, 12,1,"Finalizada"),
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


        val encuestadores = listOf(
            Encuestador(encuestadorId = 1, "correo@gmail.com","1234")
        )

        val zonas = listOf(
            Zona(1,"sur","norte","este","oeste")
        )

        val portions = listOf(
            Portion(listOf("5gr","15gr"), listOf(R.drawable.cuchara5grq, R.drawable.cuchara15grq), 1),      //Leche en polvo entera
            Portion(listOf("100ml","350ml"), listOf(R.drawable.taza100mlq, R.drawable.taza350mlq), 2),      //Leche fluida entera
            Portion(listOf("100gr","185gr"), listOf(R.drawable.yogur100mgq, R.drawable.yogur185mgq), 3),      //Yogur entero bebible
            Portion(listOf("15gr","35gr"), listOf(R.drawable.quesopd_op1, R.drawable.quesopd_op2), 4),      //Queso De pasta dura (ej. Sardo, Romano, Provolone, Reggianito, Parmesano)
            Portion(listOf("15gr","35gr"), listOf(R.drawable.quesosm_op1, R.drawable.quesosm_op2), 5),      //Queso De pasta semidura/azul (ej. Holanda, Gouda, Fontina, Pategras, Dambo)
            Portion(listOf("20gr","35gr"), listOf(R.drawable.manteca20gr, R.drawable.manteca35gr), 6),      //Manteca
            Portion(listOf("5gr","15gr"), listOf(R.drawable.cuchara5grq, R.drawable.cuchara15grq), 7),      //Huevo De gallina entero crudo-hervido- poché
            Portion(listOf("5gr","15gr"), listOf(R.drawable.cuchara5grq, R.drawable.cuchara15grq), 8),      //Huevo De gallina entero frito
            Portion(listOf("5gr","15gr"), listOf(R.drawable.cuchara5grq, R.drawable.cuchara15grq), 9),      //Carne Vacuna magra (ej. bola de lomo, colita de cuadril, cuadril, nalga, tapa de nalga, paleta, cuadrada, peceto, tortuguita y vacío)
            Portion(listOf("5gr","15gr"), listOf(R.drawable.cuchara5grq, R.drawable.cuchara15grq), 10),     //Carne Vacuna cortes grasos (ej. aguja, bife ancho y angosto, cogote, asado, costillar, entraña, osobuco, matambre, palomita)
            Portion(listOf("5gr","15gr"), listOf(R.drawable.cuchara5grq, R.drawable.cuchara15grq), 11),     //Carne picada
            Portion(listOf("5gr","15gr"), listOf(R.drawable.cuchara5grq, R.drawable.cuchara15grq), 12),     //Salchichas
            Portion(listOf("5gr","15gr"), listOf(R.drawable.cuchara5grq, R.drawable.cuchara15grq), 13),     //Salame-salamín-chorizo seco-longaniza
            Portion(listOf("5gr","15gr"), listOf(R.drawable.cuchara5grq, R.drawable.cuchara15grq), 14),     //Mortadela
            Portion(listOf("5gr","15gr"), listOf(R.drawable.cuchara5grq, R.drawable.cuchara15grq), 15),     //Papas fritas caseras
            Portion(listOf("5gr","15gr"), listOf(R.drawable.cuchara5grq, R.drawable.cuchara15grq), 16),     //Pizza
            Portion(listOf("5gr","15gr"), listOf(R.drawable.cuchara5grq, R.drawable.cuchara15grq), 17),     //Empanadas de carne fritas
            Portion(listOf("5gr","15gr"), listOf(R.drawable.cuchara5grq, R.drawable.cuchara15grq), 18),     //Empanadas de carne
            Portion(listOf("5gr","15gr"), listOf(R.drawable.cuchara5grq, R.drawable.cuchara15grq), 19),     //Pastel de papas
            Portion(listOf("5gr","15gr"), listOf(R.drawable.cuchara5grq, R.drawable.cuchara15grq), 20),     //Puchero
            Portion(listOf("5gr","15gr"), listOf(R.drawable.cuchara5grq, R.drawable.cuchara15grq), 21),     //Banana
            Portion(listOf("5gr","15gr"), listOf(R.drawable.cuchara5grq, R.drawable.cuchara15grq), 22),     //Durazno
            Portion(listOf("5gr","15gr"), listOf(R.drawable.cuchara5grq, R.drawable.cuchara15grq), 23),     //Manzana
            Portion(listOf("5gr","15gr"), listOf(R.drawable.cuchara5grq, R.drawable.cuchara15grq), 24),     //Aguas saborizadas clásicas
            Portion(listOf("5gr","15gr"), listOf(R.drawable.cuchara5grq, R.drawable.cuchara15grq), 25),     //Bebidas deportivas y energizantes
            Portion(listOf("5gr","15gr"), listOf(R.drawable.cuchara5grq, R.drawable.cuchara15grq), 26),     //Gaseosas clásicas
            Portion(listOf("5gr","15gr"), listOf(R.drawable.cuchara5grq, R.drawable.cuchara15grq), 27),     //Vino
            Portion(listOf("5gr","15gr"), listOf(R.drawable.cuchara5grq, R.drawable.cuchara15grq), 28),     //Cerveza o aperitivos
            Portion(listOf("5gr","15gr"), listOf(R.drawable.cuchara5grq, R.drawable.cuchara15grq), 29),     //Licor
            Portion(listOf("5gr","15gr"), listOf(R.drawable.cuchara5grq, R.drawable.cuchara15grq), 30),     //Bebidas blancas


            )

    }
}