package com.example.nutritnt.data;

import android.content.Context
import android.util.Log
import com.example.nutritnt.database.entities.Alimento
import com.example.nutritnt.database.entities.InformacionNutricional
import java.io.BufferedReader
import java.io.InputStreamReader


public class ReadCSV {

    companion object{
        fun readFromCSV(context: Context, alimentosList: MutableList<Alimento>, informacionNutricionalList: MutableList<InformacionNutricional>) {
            try {
                val inputStream = context.assets.open("alimentos.csv")
                val reader = BufferedReader(InputStreamReader(inputStream))

                // Skip header line
                reader.readLine()

                var line: String?
                while (reader.readLine().also { line = it } != null) {
                    val tokens = line?.split(",")
                    if (tokens != null) {
                        val codigo = tokens[0].trim()
                        val grupo = tokens[1].trim()
                        val subgrupo = tokens[2].trim()
                        val descripcion = tokens[3].trim()

                        val informacionNutricional = InformacionNutricional(
                            informacionNutricionalId = informacionNutricionalList.size + 1,
                            cantidad = tokens[4].trim().toInt(),
                            unidad = tokens[5].trim(),
                            kcalTotales = tokens[6].trim().toFloat(),
                            carbohidratos = tokens[7].trim().toFloat(),
                            proteinas = tokens[8].trim().toFloat(),
                            grasasTotales = tokens[9].trim().toFloat(),
                            alcohol = tokens[10].trim().toFloat(),
                            colesterol = tokens[11].trim().toFloat(),
                            fibra = tokens[12].trim().toFloat()
                        )



                        informacionNutricionalList.add(informacionNutricional)

                        Log.i("insercionCSV", "infonutri: " + informacionNutricional)

                        val alimento = Alimento(
                            alimentoId = alimentosList.size + 1,
                            codigo = codigo,
                            grupo = grupo,
                            subgrupo = subgrupo,
                            descripcion = descripcion,
                            informacionNutricionalId = informacionNutricional.informacionNutricionalId
                        )

                        alimentosList.add(alimento)

                        Log.i("insercionCSV", " alimento: " + alimento)
                    }
                }
                reader.close()
            } catch (e: Exception) {
                Log.e("ReadCSV", "Error reading CSV file: ${e.message}")
            }
        }


    }
}