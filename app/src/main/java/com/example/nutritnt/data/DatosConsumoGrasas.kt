package com.example.nutritnt.data

import android.util.Log
import com.example.nutritnt.database.relations.EncuestaAlimento_AlimentoInformacionNutricional


public class DatosConsumoGrasas {



    companion object {


        fun obtenerDatosPorPeriodo(listaEncuestaAlimento: List<EncuestaAlimento_AlimentoInformacionNutricional> , periodoBuscado: String): Float {

            val sumaTotal = listaEncuestaAlimento.map { datos ->
                Log.i("contadorGrasasTotales", "periodo " + datos.encuestaAlimento.period + " alimentoid " + datos.encuestaAlimento.alimentoId + " encuestaAlimentoID " + datos.encuestaAlimento.encuestaAlimentoId)
                val frecuenciaAjustada = when (periodoBuscado) {
                    "dia" -> {
                        when (datos.encuestaAlimento.period) {
                            "dia" -> datos.encuestaAlimento.frecuency.toFloat()
                            "semana" -> datos.encuestaAlimento.frecuency.toFloat() / 7
                            "mes" -> datos.encuestaAlimento.frecuency.toFloat() / 31
                            "año" -> datos.encuestaAlimento.frecuency.toFloat() / 365
                            else -> datos.encuestaAlimento.frecuency.toFloat()
                        }
                    }

                    "semana" -> {
                        when (datos.encuestaAlimento.period) {
                            "dia" -> datos.encuestaAlimento.frecuency.toFloat() * 7
                            "semana" -> datos.encuestaAlimento.frecuency.toFloat()
                            "mes" -> datos.encuestaAlimento.frecuency.toFloat() / 4  // Suponiendo 4 semanas por mes
                            "año" -> datos.encuestaAlimento.frecuency.toFloat() / 52 // Suponiendo 52 semanas por año
                            else -> datos.encuestaAlimento.frecuency.toFloat()
                        }
                    }

                    "mes" -> {
                        when (datos.encuestaAlimento.period) {
                            "dia" -> datos.encuestaAlimento.frecuency.toFloat() * 31
                            "semana" -> datos.encuestaAlimento.frecuency.toFloat() * 4  // Suponiendo 4 semanas por mes
                            "mes" -> datos.encuestaAlimento.frecuency.toFloat()
                            "año" -> datos.encuestaAlimento.frecuency.toFloat() / 12
                            else -> datos.encuestaAlimento.frecuency.toFloat()
                        }
                    }

                    "año" -> {
                        when (datos.encuestaAlimento.period) {
                            "dia" -> datos.encuestaAlimento.frecuency.toFloat() * 365
                            "semana" -> datos.encuestaAlimento.frecuency.toFloat() * 52 // Suponiendo 52 semanas por año
                            "mes" -> datos.encuestaAlimento.frecuency.toFloat() * 12
                            "año" -> datos.encuestaAlimento.frecuency.toFloat()
                            else -> datos.encuestaAlimento.frecuency.toFloat()
                        }
                    }

                    else -> datos.encuestaAlimento.frecuency
                }
                Log.i("frecuenciaAjustada ", " " +frecuenciaAjustada.toFloat())

                Log.i ("contadorGrasasTotales", "total " + datos.encuestaAlimento.portion.toInt()*(datos.alimentoInformacionNutricional.informacionNutricional.grasasTotales/datos.alimentoInformacionNutricional.informacionNutricional.cantidad) *frecuenciaAjustada.toFloat())
              //  datos.portion.toInt()*(DatosDatabase.alimentos[0].grasas_totales/DatosDatabase.alimentos[0].cantidad) *frecuenciaAjustada.toFloat()
                datos.encuestaAlimento.portion.toInt()*(datos.alimentoInformacionNutricional.informacionNutricional.grasasTotales/datos.alimentoInformacionNutricional.informacionNutricional.cantidad) *frecuenciaAjustada.toFloat()
            }.sum()


            return sumaTotal

        }


    }




}