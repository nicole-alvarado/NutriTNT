package com.example.nutritnt.data

import android.util.Log
import com.example.nutritnt.database.relations.EncuestaAlimento_AlimentoInformacionNutricional

data class Consumo (var descripcion: String, var porcentaje: Float, var gramos: Float)

public class DatosConsumo {



    companion object {

        fun calcularValoresNutricionales(encuestasAlimentos: List<EncuestaAlimento_AlimentoInformacionNutricional>, periodo: String): List<Consumo> {

            var totalCalorias = 0f
            var totalGrasas = 0f
            var totalProteinas = 0f
            var totalCarbohidratos = 0f
            var totalFibras = 0f
            var totalAlcohol = 0f
            var totalColesterol = 0f


            //se calcula la cantidad consumida / 100 (los 100gr por default de InfoNutricional) y se multiplica por el item
            for (alimentoConInfo in encuestasAlimentos) {


                val frecuenciaAjustada = when (periodo) {
                    "dia" -> {
                        when (alimentoConInfo.encuestaAlimento.period.lowercase()) {
                            "dia" -> alimentoConInfo.encuestaAlimento.frecuency.toFloat()
                            "semana" -> alimentoConInfo.encuestaAlimento.frecuency.toFloat() / 7
                            "mes" -> alimentoConInfo.encuestaAlimento.frecuency.toFloat() / 31
                            "año" -> alimentoConInfo.encuestaAlimento.frecuency.toFloat() / 365
                            else -> 0F
                        }
                    }
                    "semana" -> {
                        when (alimentoConInfo.encuestaAlimento.period.lowercase()) {
                            "dia" -> alimentoConInfo.encuestaAlimento.frecuency.toFloat() * 7
                            "semana" -> alimentoConInfo.encuestaAlimento.frecuency.toFloat()
                            "mes" -> alimentoConInfo.encuestaAlimento.frecuency.toFloat() / 4  // Suponiendo 4 semanas por mes
                            "año" -> alimentoConInfo.encuestaAlimento.frecuency.toFloat() / 52 // Suponiendo 52 semanas por año
                            else -> 0F
                        }
                    }

                    "mes" -> {
                        when (alimentoConInfo.encuestaAlimento.period.lowercase()) {
                            "dia" -> alimentoConInfo.encuestaAlimento.frecuency.toFloat() * 31
                            "semana" -> alimentoConInfo.encuestaAlimento.frecuency.toFloat() * 4  // Suponiendo 4 semanas por mes
                            "mes" -> alimentoConInfo.encuestaAlimento.frecuency.toFloat()
                            "año" -> alimentoConInfo.encuestaAlimento.frecuency.toFloat() / 12
                            else -> 0F
                        }
                    }

                    "año" -> {
                        when (alimentoConInfo.encuestaAlimento.period.lowercase()) {
                            "dia" -> alimentoConInfo.encuestaAlimento.frecuency.toFloat() * 365
                            "semana" -> alimentoConInfo.encuestaAlimento.frecuency.toFloat() * 52 // Suponiendo 52 semanas por año
                            "mes" -> alimentoConInfo.encuestaAlimento.frecuency.toFloat() * 12
                            "año" -> alimentoConInfo.encuestaAlimento.frecuency.toFloat()
                            else -> 0F
                        }
                    }


                    else -> {0F}
                }


                var cantidad = 0F
                if (alimentoConInfo.encuestaAlimento.period.lowercase() != "nunca") {
                    cantidad =
                        alimentoConInfo.encuestaAlimento.portion.toInt() * frecuenciaAjustada
                }
                val info = alimentoConInfo.alimentoInformacionNutricional.informacionNutricional

                totalCalorias += info.kcalTotales * (cantidad / 100)
                totalGrasas += info.grasasTotales * (cantidad / 100)
                totalProteinas += info.proteinas * (cantidad / 100)
                totalCarbohidratos += info.carbohidratos * (cantidad / 100)
                totalFibras += info.fibra * (cantidad / 100)
                totalAlcohol += info.alcohol * (cantidad / 100)
                totalColesterol += info.colesterol * (cantidad / 100)
            }

            //El total general de lo consumido
            val total = totalCalorias + totalGrasas + totalProteinas + totalCarbohidratos + totalFibras + totalAlcohol + totalColesterol

            val porcentajeCalorias = if (total > 0) (totalCalorias / total) * 100 else 0f
            val porcentajeGrasas = if (total > 0) (totalGrasas / total) * 100 else 0f
            val porcentajeProteinas = if (total > 0) (totalProteinas / total) * 100 else 0f
            val porcentajeCarbohidratos = if (total > 0) (totalCarbohidratos / total) * 100 else 0f
            val porcentajeFibras = if (total > 0) (totalFibras / total) * 100 else 0f
            val porcentajeAlcohol = if (total > 0) (totalAlcohol / total) * 100 else 0f
            val porcentajeColesterol = if (total > 0) (totalColesterol / total) * 100 else 0f

            var consumo = listOf(
                Consumo("calorias", porcentajeCalorias, totalCalorias),
                Consumo("grasas", porcentajeGrasas, totalGrasas),
                Consumo("proteinas", porcentajeProteinas, totalProteinas),
                Consumo("carbohidratos", porcentajeCarbohidratos, totalCarbohidratos),
                Consumo("fibras", porcentajeFibras, totalFibras),
                Consumo("alcohol", porcentajeAlcohol, totalAlcohol),
                Consumo("colesterol", porcentajeColesterol, totalColesterol)
            )

            var newListConsumo: MutableList<Consumo> = ArrayList<Consumo>()

            for(c in consumo) {
                Log.i("DatosConsumoFrecuency", c.toString())

                if (c.porcentaje != 0f) {
                    newListConsumo.add(c)
                }
            }

            return newListConsumo

        }

    }




}