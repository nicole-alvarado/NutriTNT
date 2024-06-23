package com.example.nutritnt.data

import com.example.nutritnt.R
import com.example.nutritnt.database.entities.Encuesta
import com.example.nutritnt.database.entities.EncuestaAlimento
import com.example.nutritnt.database.entities.Encuestador
import com.example.nutritnt.database.entities.Zona

data class Portion(val portions: Map<Char, String>, val medidaPortion: String, val imgsPortions: List<Int>, val alimentoID: Int)

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

            Portion(mapOf('A' to "2",'B' to "5",'C' to "10",'D' to "15",'E' to "25" ), "gr", listOf(R.drawable.cuchara5grq, R.drawable.cuchara15grq), 1),      //Leche en polvo entera
            Portion(mapOf('A' to "30", 'B' to "100", 'C' to "150", 'D' to "350", 'E' to "500"), "ml", listOf(R.drawable.taza100mlq, R.drawable.taza350mlq), 2),      //Leche fluida entera
            Portion(mapOf('A' to "30", 'B' to "100",'C' to "150", 'D' to "185", 'E' to "250"), "gr", listOf(R.drawable.yogur100mgq, R.drawable.yogur185mgq), 3),      //Yogur entero bebible
            Portion(mapOf('A' to "2",'B' to "15", 'C' to "20",'D' to "35", 'E' to "50"), "gr", listOf(R.drawable.quesopd_op1, R.drawable.quesopd_op2), 4),      //Queso De pasta dura (ej. Sardo, Romano, Provolone, Reggianito, Parmesano)
            Portion(mapOf('A' to "2",'B' to "15", 'C' to "20",'D' to "35", 'E' to "50"), "gr", listOf(R.drawable.quesosm_op1, R.drawable.quesosm_op2), 5),      //Queso De pasta semidura/azul (ej. Holanda, Gouda, Fontina, Pategras, Dambo)
            Portion(mapOf('A' to "8",'B' to "20", 'C' to "25",'D' to "35", 'E' to "50"), "gr", listOf(R.drawable.manteca20gr, R.drawable.manteca35gr), 6),      //Manteca

            Portion(mapOf('A' to "100",'B' to "300",'C' to "450",'D' to "600",'E' to "1200"), "gr", listOf(R.drawable.huvoscrudos300gr, R.drawable.huevoscrudos600gr), 7),      //Huevo De gallina entero crudo-hervido- poché
            Portion(mapOf('A' to "100",'B' to "300",'C' to "450",'D' to "600",'E' to "1200"), "gr", listOf(R.drawable.huvosfritos300gr, R.drawable.huevosfritas600gr), 8),      //Huevo De gallina entero frito
            Portion(mapOf('A' to "100",'B' to "300",'C' to "450",'D' to "800",'E' to "1200"), "gr", listOf(R.drawable.carnemagra300gr, R.drawable.carnemagra800gr), 9),      //Carne Vacuna magra (ej. bola de lomo, colita de cuadril, cuadril, nalga, tapa de nalga, paleta, cuadrada, peceto, tortuguita y vacío)
            Portion(mapOf('A' to "100",'B' to "250",'C' to "350",'D' to "400",'E' to "800"), "gr", listOf(R.drawable.carnegrasa250gr, R.drawable.carnegrasa400gr), 10),     //Carne Vacuna cortes grasos (ej. aguja, bife ancho y angosto, cogote, asado, costillar, entraña, osobuco, matambre, palomita)
            Portion(mapOf('A' to "100",'B' to "150",'C' to "350",'D' to "400",'E' to "900"), "gr", listOf(R.drawable.carnepicada150gr, R.drawable.carnepicada400gr), 11),     //Carne picada
            Portion(mapOf('A' to "40",'B' to "120",'C' to "200",'D' to "240",'E' to "350"), "gr", listOf(R.drawable.salchichas120gr, R.drawable.salchichas240gr), 12),     //Salchichas
            Portion(mapOf('A' to "100",'B' to "150",'C' to "200",'D' to "250",'E' to "600"), "gr", listOf(R.drawable.salame150gr, R.drawable.salame250gr), 13),     //Salame-salamín-chorizo seco-longaniza
            Portion(mapOf('A' to "100",'B' to "150",'C' to "200",'D' to "250",'E' to "600"), "gr", listOf(R.drawable.mortadela150gr, R.drawable.mortadela250gr), 14),     //Mortadela
            Portion(mapOf('A' to "100",'B' to "150",'C' to "250",'D' to "350",'E' to "600"), "gr", listOf(R.drawable.papasfritascaseras150gr, R.drawable.papasfritascaseras350gr), 15),     //Papas fritas caseras
            Portion(mapOf('A' to "100",'B' to "250",'C' to "400",'D' to "500",'E' to "800"), "gr", listOf(R.drawable.pizza250gr, R.drawable.pizza500gr), 16),     //Pizza
            Portion(mapOf('A' to "100",'B' to "300",'C' to "450",'D' to "600",'E' to "1200"), "gr", listOf(R.drawable.empanadasfritascarne_300gr, R.drawable.empanadasfritascarne_600gr), 17),     //Empanadas de carne fritas
            Portion(mapOf('A' to "100",'B' to "240",'C' to "350",'D' to "480",'E' to "600"), "gr", listOf(R.drawable.empanadascarne_240gr, R.drawable.empanadascarne_480gr), 18),     //Empanadas de carne

            Portion(mapOf('A' to "100",'B' to "300",'C' to "450",'D' to "600",'E' to "1200"), "gr",listOf(R.drawable.pasteldepapa_300gr, R.drawable.pasteldepapa_600gr), 19),     //Pastel de papas
            Portion(mapOf('A' to "100",'B' to "300",'C' to "450",'D' to "600",'E' to "1200"), "gr",listOf(R.drawable.puchero_300gr, R.drawable.puchero_600gr), 20),     //Puchero
            Portion(mapOf('A' to "100",'B' to "300",'C' to "450",'D' to "600",'E' to "1200"), "gr",listOf(R.drawable.banana_300gr, R.drawable.banana_600gr), 21),     //Banana
            Portion(mapOf('A' to "100",'B' to "300",'C' to "450",'D' to "600",'E' to "1200"), "gr",listOf(R.drawable.durazno_300gr, R.drawable.durazno_600gr), 22),     //Durazno
            Portion(mapOf('A' to "100",'B' to "300",'C' to "450",'D' to "600",'E' to "1200"), "gr",listOf(R.drawable.manzana_300gr, R.drawable.manzana_600gr), 23),     //Manzana
            Portion(mapOf('A' to "500",'B' to "1000",'C' to "1500",'D' to "2250",'E' to "3000"), "ml",listOf(R.drawable.aguasaborizada_1lt, R.drawable.aguasaborizada_2_25lt), 24),     //Aguas saborizadas clásicas
            Portion(mapOf('A' to "250",'B' to "473",'C' to "500",'D' to "750",'E' to "1000"), "ml",listOf(R.drawable.bebidaenergizante_473ml, R.drawable.bebidaenergizante_750ml), 25),     //Bebidas deportivas y energizantes
            Portion(mapOf('A' to "250",'B' to "500",'C' to "1000",'D' to "1500",'E' to "2250"), "ml",listOf(R.drawable.gaseosa_500ml, R.drawable.gaseosa_1500ml), 26),     //Gaseosas clásicas
            Portion(mapOf('A' to "500",'B' to "750",'C' to "1500",'D' to "3000",'E' to "3500"), "ml",listOf(R.drawable.vino_750ml, R.drawable.vino_3000ml), 27),     //Vino
            Portion(mapOf('A' to "330",'B' to "473",'C' to "710",'D' to "1000",'E' to "1500"), "ml",listOf(R.drawable.cerveza_473ml, R.drawable.cerveza_1000ml), 28),     //Cerveza o aperitivos
            Portion(mapOf('A' to "500",'B' to "750",'C' to "1000",'D' to "1700",'E' to "2000"), "ml",listOf(R.drawable.licor_750ml, R.drawable.licor_1700ml), 29),     //Licor
            Portion(mapOf('A' to "500",'B' to "750",'C' to "1000",'D' to "1750",'E' to "2000"), "ml",listOf(R.drawable.bebidablanca_750ml, R.drawable.bebidablanca_1750ml), 30),     //Bebidas blancas
        )
        // Método estático para obtener imágenes de porción para un alimento específico
        fun getPortionImagesForAlimento(alimentoId: Int): List<Int> {
            val portion = portions.find { it.alimentoID == alimentoId }
            return portion?.imgsPortions ?: emptyList()
        }

    }
}