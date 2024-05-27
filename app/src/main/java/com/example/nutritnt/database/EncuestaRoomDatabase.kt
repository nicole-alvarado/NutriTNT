package com.example.nutritnt.database

import android.content.Context
import android.util.Log
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.Transaction
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.nutritnt.database.dao.AlimentoDAO
import com.example.nutritnt.database.dao.EncuestaDAO
import com.example.nutritnt.database.dao.Encuesta_AlimentoDAO
import com.example.nutritnt.database.entities.Alimento
import com.example.nutritnt.database.entities.Encuesta
import com.example.nutritnt.database.entities.Encuesta_Alimento
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import kotlin.random.Random

@Database(entities = [Encuesta::class, Encuesta_Alimento::class, Alimento::class], version = 5, exportSchema = false)
abstract class EncuestaRoomDatabase : RoomDatabase() {

    abstract fun encuestaAlimentoDao(): Encuesta_AlimentoDAO
    abstract fun encuestaDao(): EncuestaDAO
    abstract fun alimentoDao(): AlimentoDAO

    companion object {
        @Volatile
        private var INSTANCIA: EncuestaRoomDatabase? = null

        fun obtenerDatabase(context: Context): EncuestaRoomDatabase {
            Log.i("EncuestaRoomDatabase", "obtenerDatabase iniciado")
            return INSTANCIA ?: synchronized(this) {
                Log.i("EncuestaRoomDatabase", "Sincronizado")
                val instancia = Room.databaseBuilder(
                    context.applicationContext,
                    EncuestaRoomDatabase::class.java,
                    "encuesta_database"
                )
                    .build()
                INSTANCIA = instancia
                Log.i("EncuestaRoomDatabase", "Base de datos creada")
                instancia
            }
        }


        @Transaction
        open suspend fun safeInsertMultiple(
            encuestasGeneral: List<Encuesta>,
            alimentos: List<Alimento>,
            encuestasAlimento: List<Encuesta_Alimento>,
            encuestaDAO: EncuestaDAO,
            encuestaAlimentoDAO: Encuesta_AlimentoDAO,
            alimentoDAO: AlimentoDAO
        ): Boolean {

            encuestasGeneral.forEach { encuestaGeneral ->
                Log.i("InsercionEnRoom", "encuestaAlimento" + encuestaGeneral.toString())

                val encuestaGeneralId = encuestaDAO.insertar(encuestaGeneral)

            }

            alimentos.forEach { alimento ->
                Log.i("InsercionEnRoom", "encuestaAlimento" + alimento.toString())

                val alimentoId = alimentoDAO.insertar(alimento)

            }

            encuestasAlimento.forEach { encuestaAlimento ->
                Log.i("InsercionEnRoom", "encuestaAlimento" + encuestaAlimento.toString())
                val result = encuestaAlimentoDAO.insertar(encuestaAlimento)

            }
            return true
        }

            suspend fun cargarBaseDeDatos(encuestaDAO: EncuestaDAO, encuestaAlimentoDAO: Encuesta_AlimentoDAO, alimentoDAO: AlimentoDAO) {
                Log.i("EncuestaRoomDatabase", "Cargar Base de Datos iniciado")
                if (encuestaDAO.cantidadDeEncuestas() == 0) {
                    Log.i("EncuestaRoomDatabase", "Base de datos vacía, cargando datos de ejemplo...")

                    val encuestas = listOf(
                        Encuesta(1, "nombre1", "21-05-2024", "ACTIVA", "Zona A"),
                        Encuesta(2, "nombre2", "22-05-2024", "ACTIVA", "Zona C"),
                        Encuesta(3, "nombre3", "23-05-2024", "ACTIVA", "Zona C"),
                        Encuesta(4, "nombre4", "15-04-2024", "Finalizada", "Zona B"),
                        Encuesta(5, "nombre5", "16-04-2024", "Comenzada", "Zona A"),
                        Encuesta(6, "nombre6", "21-05-2024", "ACTIVA", "Zona A"),
                        Encuesta(7, "nombre7", "22-05-2024", "ACTIVA", "Zona C"),
                        Encuesta(8, "nombre8", "23-05-2024", "ACTIVA", "Zona C"),
                        Encuesta(9, "nombre9", "15-04-2024", "Finalizada", "Zona B"),
                        Encuesta(10, "nombre10", "16-04-2024", "Comenzada", "Zona A"),
                        Encuesta(11, "nombre11", "21-05-2024", "ACTIVA", "Zona A"),
                        Encuesta(12, "nombre12", "22-05-2024", "ACTIVA", "Zona C"),
                        Encuesta(13, "nombre13", "23-05-2024", "ACTIVA", "Zona C"),
                        Encuesta(14, "nombre14", "15-04-2024", "Finalizada", "Zona B"),
                        Encuesta(15, "nombre15", "16-04-2024", "Comenzada", "Zona A"),

                    )
                    encuestas.forEach { encuestaDAO.insertar(it) }
                    Log.i("EncuestaRoomDatabase", "Datos de Encuestas insertados")
                }

                if (alimentoDAO.cantidadDeAlimentos() == 0) {
                    val alimento = Alimento(alimentoId = 1, "Yogur bebible", 100, 4F)
                    alimentoDAO.insertar(alimento)
                    Log.i("EncuestaRoomDatabase", "Datos de Alimentos insertados")
                }

                // Uncomment and adjust this section if necessary
                // if (encuestaAlimentoDAO.cantidadDeEncuestasAlimento() == 0) {
                //     val encuestaAlimentos = listOf(
                //         Encuesta_Alimento(1, "1 taza", "Día", 2, 1, 1),
                //         Encuesta_Alimento(2, "1/2 taza", "Semana", 3, 2, 1)
                //     )
                //     encuestaAlimentos.forEach { encuestaAlimentoDAO.insertar(it) }
                //     Log.i("EncuestaRoomDatabase", "Datos de Encuesta_Alimentos insertados")
                // }


        }
    }
}
