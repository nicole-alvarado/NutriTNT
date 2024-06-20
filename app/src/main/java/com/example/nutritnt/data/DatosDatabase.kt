package com.example.nutritnt.data

import com.example.nutritnt.R
import com.example.nutritnt.database.entities.Encuesta
import com.example.nutritnt.database.entities.EncuestaAlimento
import com.example.nutritnt.database.entities.Encuestador
import com.example.nutritnt.database.entities.Zona

data class Portion(val portions: Map<Char, String>, val imgsPortions: List<Int>, val alimentoID: Int)

class DatosDatabase {

    companion object{
        val datosConsumoYogur = listOf(
            EncuestaAlimento(1,  "100",  "semana",  1, 1,1,"NO COMPLETADA"),
            EncuestaAlimento( 2,  "200",  "semana",  1, 1,2,"NO COMPLETADA"),
            EncuestaAlimento( 3,  "275",  "mes",  2, 1,3,"NO COMPLETADA"),
            EncuestaAlimento( 4,  "100",  "dia",  4, 1,4,"NO COMPLETADA"),
            EncuestaAlimento( 5,  "200",  "semana",  2, 2,1,"COMPLETADA"),
            EncuestaAlimento( 6,  "275",  "dia",  1, 2,2,"NO COMPLETADA"),
            EncuestaAlimento( 7,  "100",  "mes",  3, 2,3,"NO COMPLETADA"),
            EncuestaAlimento( 8,  "200",  "dia",  2, 4,1,"NO COMPLETADA"),
            EncuestaAlimento( 9,  "275",  "semana",  4, 4,2,"COMPLETADA"),
            EncuestaAlimento( 10,  "100",  "mes",  1, 4,3,"NO COMPLETADA"),
            EncuestaAlimento( 11,  "200",  "dia",  3, 4,4,"NO COMPLETADA"),
            EncuestaAlimento( 12,  "275",  "semana",  2, 4,5,"COMPLETADA"),
        )

        val encuestas = listOf(
            Encuesta(1, "ID-123", "21-05-2024", "Finalizada", "Zona 4","codigo1","-42.780570", "-65.032188"),
            Encuesta(2, "ID-890", "22-05-2024", "Finalizada", "Zona 3","codigo2","-42.783688", "-65.054804"),
            Encuesta(3, "ID-296", "23-05-2024", "Finalizada", "Zona 2","codigo3","-42.775260","-65.061424"),
            Encuesta(4, "ID-719", "15-04-2024", "Finalizada", "Zona 1","codigo4","-42.771819","-65.042573"),
            Encuesta(5, "ID-655", "16-04-2024", "Comenzada", "Zona 1","codigo5","-42.753473","-65.053012"),
            Encuesta(6, "ID-333", "21-05-2024", "Comenzada", "Zona 4","codigo6","-42.772346","-65.031083"),
            Encuesta(7, "ID-978", "22-05-2024", "Comenzada", "Zona 4","codigo7","-42.778363","-65.034773"),
            Encuesta(8, "ID-246", "23-05-2024", "Comenzada", "Zona 3","codigo8","-42.788190","-65.068591"),
            Encuesta(9, "ID-367", "15-04-2024", "Finalizada", "Zona 2","codigo9","-42.762643","-65.058892"),
            Encuesta(10, "ID-518", "16-04-2024", "Comenzada", "Zona 1","codigo10","-42.754545","-65.038936"),
            Encuesta(11, "ID-332", "21-05-2024", "Comenzada", "Zona 1","codigo11","-42.771189","-65.047251"),
            Encuesta(12, "ID-796", "22-05-2024", "Comenzada", "Zona 3","codigo12","-42.790522","-65.069910"),
            Encuesta(13, "ID-447", "23-05-2024", "Comenzada", "Zona 3","codigo13","-42.784412","-65.050512"),
            Encuesta(14, "ID-517", "15-04-2024", "Finalizada", "Zona 2","codigo14","-42.758105","-65.068204"),
            Encuesta(15, "ID-642", "16-04-2024", "Comenzada", "Zona 1","codigo15","-42.756973","-65.049397")
        )


        val encuestadores = listOf(
            Encuestador(encuestadorId = 1, "correo@gmail.com","1234")
        )

        val zonas = listOf(
            Zona(1,"sur","norte","este","oeste")
        )

        val portions = listOf(
            Portion(mapOf('A' to "2gr",'B' to "5gr",'C' to "10gr",'D' to "15gr",'E' to "25gr" ), listOf(R.drawable.cuchara5grq, R.drawable.cuchara15grq), 1),      //Leche en polvo entera
            Portion(mapOf('A' to "30ml", 'B' to "100ml", 'C' to "150ml", 'D' to "350ml", 'E' to "500ml"), listOf(R.drawable.taza100mlq, R.drawable.taza350mlq), 2),      //Leche fluida entera
            Portion(mapOf('A' to "30gr", 'B' to "100gr",'C' to "150gr", 'D' to "185gr", 'E' to "250gr"), listOf(R.drawable.yogur100mgq, R.drawable.yogur185mgq), 3),      //Yogur entero bebible
            Portion(mapOf('A' to "2gr",'B' to "15gr", 'C' to "20gr",'D' to "35gr", 'E' to "50gr"), listOf(R.drawable.quesopd_op1, R.drawable.quesopd_op2), 4),      //Queso De pasta dura (ej. Sardo, Romano, Provolone, Reggianito, Parmesano)
            Portion(mapOf('A' to "2gr",'B' to "15gr", 'C' to "20gr",'D' to "35gr", 'E' to "50gr"), listOf(R.drawable.quesosm_op1, R.drawable.quesosm_op2), 5),      //Queso De pasta semidura/azul (ej. Holanda, Gouda, Fontina, Pategras, Dambo)
            Portion(mapOf('A' to "8gr",'B' to "20gr", 'C' to "25gr",'D' to "35gr", 'E' to "50gr"), listOf(R.drawable.manteca20gr, R.drawable.manteca35gr), 6),      //Manteca

            Portion(mapOf('A' to "100gr",'B' to "300gr",'C' to "450gr",'D' to "600gr",'E' to "1200gr" ), listOf(R.drawable.huvoscrudos300gr, R.drawable.huevoscrudos600gr), 7),      //Huevo De gallina entero crudo-hervido- poché
            Portion(mapOf('A' to "100gr",'B' to "300gr",'C' to "450gr",'D' to "600gr",'E' to "1200gr" ), listOf(R.drawable.huvosfritos300gr, R.drawable.huevosfritas600gr), 8),      //Huevo De gallina entero frito
            Portion(mapOf('A' to "100gr",'B' to "300gr",'C' to "450gr",'D' to "800gr",'E' to "1200gr"), listOf(R.drawable.carnemagra300gr, R.drawable.carnemagra800gr), 9),      //Carne Vacuna magra (ej. bola de lomo, colita de cuadril, cuadril, nalga, tapa de nalga, paleta, cuadrada, peceto, tortuguita y vacío)
            Portion(mapOf('A' to "100gr",'B' to "250gr",'C' to "350gr",'D' to "400gr",'E' to "800gr"), listOf(R.drawable.carnegrasa250gr, R.drawable.carnegrasa400gr), 10),     //Carne Vacuna cortes grasos (ej. aguja, bife ancho y angosto, cogote, asado, costillar, entraña, osobuco, matambre, palomita)
            Portion(mapOf('A' to "100gr",'B' to "150gr",'C' to "350gr",'D' to "400gr",'E' to "900gr"), listOf(R.drawable.carnepicada150gr, R.drawable.carnepicada400gr), 11),     //Carne picada
            Portion(mapOf('A' to "40gr",'B' to "120gr",'C' to "200gr",'D' to "240gr",'E' to "350gr"), listOf(R.drawable.salchichas120gr, R.drawable.salchichas240gr), 12),     //Salchichas
            Portion(mapOf('A' to "100gr",'B' to "150gr",'C' to "200gr",'D' to "250gr",'E' to "600gr"), listOf(R.drawable.salame150gr, R.drawable.salame250gr), 13),     //Salame-salamín-chorizo seco-longaniza
            Portion(mapOf('A' to "100gr",'B' to "150gr",'C' to "200gr",'D' to "250gr",'E' to "600gr"), listOf(R.drawable.mortadela150gr, R.drawable.mortadela250gr), 14),     //Mortadela
            Portion(mapOf('A' to "100gr",'B' to "150gr",'C' to "250gr",'D' to "350gr",'E' to "600gr"), listOf(R.drawable.papasfritascaseras150gr, R.drawable.papasfritascaseras350gr), 15),     //Papas fritas caseras
            Portion(mapOf('A' to "100gr",'B' to "250gr",'C' to "400gr",'D' to "500gr",'E' to "800gr"), listOf(R.drawable.pizza250gr, R.drawable.pizza500gr), 16),     //Pizza
            Portion(mapOf('A' to "100gr",'B' to "300gr",'C' to "450gr",'D' to "600gr",'E' to "1200gr"), listOf(R.drawable.empanadasfritascarne_300gr, R.drawable.empanadasfritascarne_600gr), 17),     //Empanadas de carne fritas
            Portion(mapOf('A' to "100gr",'B' to "240gr",'C' to "350gr",'D' to "480gr",'E' to "600gr"), listOf(R.drawable.empanadascarne_240gr, R.drawable.empanadascarne_480gr), 18),     //Empanadas de carne

            Portion(mapOf('A' to "100gr",'B' to "300gr",'C' to "450gr",'D' to "600gr",'E' to "1200gr"),listOf(R.drawable.cuchara5grq, R.drawable.cuchara15grq), 19),     //Pastel de papas
            Portion(mapOf('A' to "100gr",'B' to "300gr",'C' to "450gr",'D' to "600gr",'E' to "1200gr"),listOf(R.drawable.cuchara5grq, R.drawable.cuchara15grq), 20),     //Puchero
            Portion(mapOf('A' to "100gr",'B' to "300gr",'C' to "450gr",'D' to "600gr",'E' to "1200gr"),listOf(R.drawable.cuchara5grq, R.drawable.cuchara15grq), 21),     //Banana
            Portion(mapOf('A' to "100gr",'B' to "300gr",'C' to "450gr",'D' to "600gr",'E' to "1200gr"),listOf(R.drawable.cuchara5grq, R.drawable.cuchara15grq), 22),     //Durazno
            Portion(mapOf('A' to "100gr",'B' to "300gr",'C' to "450gr",'D' to "600gr",'E' to "1200gr"),listOf(R.drawable.cuchara5grq, R.drawable.cuchara15grq), 23),     //Manzana
            Portion(mapOf('A' to "100gr",'B' to "300gr",'C' to "450gr",'D' to "600gr",'E' to "1200gr"),listOf(R.drawable.cuchara5grq, R.drawable.cuchara15grq), 24),     //Aguas saborizadas clásicas
            Portion(mapOf('A' to "100gr",'B' to "300gr",'C' to "450gr",'D' to "600gr",'E' to "1200gr"),listOf(R.drawable.cuchara5grq, R.drawable.cuchara15grq), 25),     //Bebidas deportivas y energizantes
            Portion(mapOf('A' to "100gr",'B' to "300gr",'C' to "450gr",'D' to "600gr",'E' to "1200gr"),listOf(R.drawable.cuchara5grq, R.drawable.cuchara15grq), 26),     //Gaseosas clásicas
            Portion(mapOf('A' to "100gr",'B' to "300gr",'C' to "450gr",'D' to "600gr",'E' to "1200gr"),listOf(R.drawable.cuchara5grq, R.drawable.cuchara15grq), 27),     //Vino
            Portion(mapOf('A' to "100gr",'B' to "300gr",'C' to "450gr",'D' to "600gr",'E' to "1200gr"),listOf(R.drawable.cuchara5grq, R.drawable.cuchara15grq), 28),     //Cerveza o aperitivos
            Portion(mapOf('A' to "100gr",'B' to "300gr",'C' to "450gr",'D' to "600gr",'E' to "1200gr"),listOf(R.drawable.cuchara5grq, R.drawable.cuchara15grq), 29),     //Licor
            Portion(mapOf('A' to "100gr",'B' to "300gr",'C' to "450gr",'D' to "600gr",'E' to "1200gr"),listOf(R.drawable.cuchara5grq, R.drawable.cuchara15grq), 30),     //Bebidas blancas


            )

        // Método estático para obtener imágenes de porción para un alimento específico
        fun getPortionImagesForAlimento(alimentoId: Int): List<Int> {
            val portion = portions.find { it.alimentoID == alimentoId }
            return portion?.imgsPortions ?: emptyList()
        }

    }
}