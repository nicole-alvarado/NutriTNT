package com.example.nutritnt.data

import android.app.Application
import android.util.Log
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import com.example.nutritnt.database.entities.Encuesta_Alimento

import com.example.nutritnt.viewmodel.EncuestaAlimentoViewModel
import com.example.nutritnt.viewmodel.EncuestaViewModel

data class Datos(val zona: Int, val porcion: String, val periodo: String, val frecuencia: Int)

data class Alimento (val nombre: String, val grasasTotales: Float, val tamaño: String)


public class DatosConsumoGrasas {



    companion object {

       // private var datosConsumoYogur: List<EncuestaAlimentoWithZona>? = null

       // fun registrarDatos(lista: List<EncuestaAlimentoWithZona>): List<EncuestaAlimentoWithZona>{
        //    if (datosConsumoYogur == null)
        //        datosConsumoYogur = lista.toList()

          //  return datosConsumoYogur as List<EncuestaAlimentoWithZona>

        //}


        fun obtenerDatosPorPeriodo(listaEncuestaAlimento: List<Encuesta_Alimento> , periodoBuscado: String): Float {

            // Filtrar por zona
           // val datosPorZona = datosConsumoYogur?.filter {  it.zona.toInt() == zonaBuscada }
         //   val datosPorZona = DatosEncuestaAlimentos.datosConsumoYogur.filter { it.zona == zonaBuscada }

            // Filtrar por período dentro de la zona y ajustar los valores de frecuencia según el período
           // val sumaTotal = datosPorZona?.map { datos ->
            val sumaTotal = listaEncuestaAlimento.map { datos ->
                Log.i("contadorGrasasTotales", "datos " + datos.period + " id " + datos.alimentoId)
                val frecuenciaAjustada = when (periodoBuscado) {
                    "dia" -> {
                        when (datos.period) {
                            "dia" -> datos.frecuency.toFloat()
                            "semana" -> datos.frecuency.toFloat() / 7
                            "mes" -> datos.frecuency.toFloat() / 31
                            "año" -> datos.frecuency.toFloat() / 365
                            else -> datos.frecuency.toFloat()
                        }
                    }

                    "semana" -> {
                        when (datos.period) {
                            "dia" -> datos.frecuency.toFloat() * 7
                            "semana" -> datos.frecuency.toFloat()
                            "mes" -> datos.frecuency.toFloat() / 4  // Suponiendo 4 semanas por mes
                            "año" -> datos.frecuency.toFloat() / 52 // Suponiendo 52 semanas por año
                            else -> datos.frecuency.toFloat()
                        }
                    }

                    "mes" -> {
                        when (datos.period) {
                            "dia" -> datos.frecuency.toFloat() * 31
                            "semana" -> datos.frecuency.toFloat() * 4  // Suponiendo 4 semanas por mes
                            "mes" -> datos.frecuency.toFloat()
                            "año" -> datos.frecuency.toFloat() / 12
                            else -> datos.frecuency.toFloat()
                        }
                    }

                    "año" -> {
                        when (datos.period) {
                            "dia" -> datos.frecuency.toFloat() * 365
                            "semana" -> datos.frecuency.toFloat() * 52 // Suponiendo 52 semanas por año
                            "mes" -> datos.frecuency.toFloat() * 12
                            "año" -> datos.frecuency.toFloat()
                            else -> datos.frecuency.toFloat()
                        }
                    }

                    else -> datos.frecuency
                }
                Log.i("frecuenciaAjustada ", " " +frecuenciaAjustada.toFloat())

                Log.i ("contadorGrasasTotales", "total " + datos.portion.toInt()*(DatosDatabase.alimentos[0].grasas_totales/DatosDatabase.alimentos[0].cantidad) *frecuenciaAjustada.toFloat())
                datos.portion.toInt()*(DatosDatabase.alimentos[0].grasas_totales/DatosDatabase.alimentos[0].cantidad) *frecuenciaAjustada.toFloat()
            }.sum()

            return sumaTotal

        }


    }




}