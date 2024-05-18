package com.example.nutritnt.database

import android.content.Context
import android.util.Log
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.nutritnt.database.dao.EncuestaDAO
import com.example.nutritnt.database.entities.Encuesta
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import kotlin.random.Random

// Anotar la clase para convertirla en una Database Room
// con una Tabla (entity) de la clase Encuesta

@Database(entities = [Encuesta::class], version = 4, exportSchema = false)
abstract class EncuestaRoomDatabase : RoomDatabase() {

    abstract fun encuestaDao(): EncuestaDAO

    companion object {
        // Singleton previene multiples instancias de
        // la base de datos abriendose al mismo tiempo
        @Volatile
        private var INSTANCIA: EncuestaRoomDatabase? = null

        fun obtenerDatabase(
            context: Context,
            viewModelScope: CoroutineScope
        ): EncuestaRoomDatabase {
            Log.i("EncuestaRoomDatabase", "obtenerDatabase iniciado")
            val instanciaTemporal = INSTANCIA
            if (instanciaTemporal != null) {
                Log.i("EncuestaRoomDatabase", "Devolver instancia temporal")
                return instanciaTemporal
            }
            synchronized(this) {
                Log.i("EncuestaRoomDatabase", "Sincronizado")
                val instancia = Room.databaseBuilder(
                    context.applicationContext,
                    EncuestaRoomDatabase::class.java,
                    "encuesta_database"
                )
                    .addCallback(EncuestaDatabaseCallback(viewModelScope))
                    .build()
                Log.i("EncuestaRoomDatabase", "Base de datos creada")
                INSTANCIA = instancia
                return instancia
            }
        }

        private class EncuestaDatabaseCallback(
            private val scope: CoroutineScope
        ) : RoomDatabase.Callback() {

            override fun onOpen(db: SupportSQLiteDatabase) {
                super.onOpen(db)
                INSTANCIA?.let { database ->
                    Log.i("EncuestaRoomDatabase", "Database Abierta")
                    scope.launch {
                        cargarBaseDeDatos(database.encuestaDao())
                    }
                }
            }

            suspend fun cargarBaseDeDatos(encuestaDAO: EncuestaDAO) {
                Log.i("EncuestaRoomDatabase", "Cargar Base de Datos iniciado")
                if (encuestaDAO.cantidadDeEncuestas() == 0) {
                    Log.i("EncuestaRoomDatabase", "Base de datos vacía, cargando datos de ejemplo...")
                    encuestaDAO.borrarTodos()

                    val encuesta1 = Encuesta(
                        1,
                        "nombre-1",
                        "21-05-2024",
                        "ACTIVA"
                    )
                    encuestaDAO.insertar(encuesta1)

                    val encuesta2 = Encuesta(
                        2,
                        "nombre-2",
                        "22-05-2024",
                        "ACTIVA"
                    )
                    encuestaDAO.insertar(encuesta2)

                    val encuesta3 = Encuesta(
                        3,
                        "nombre-3",
                        "23-05-2024",
                        "ACTIVA"
                    )
                    encuestaDAO.insertar(encuesta3)


                    Log.i("EncuestaRoomDatabase", "Datos de ejemplo insertados")
                } else {
                    // Nuevas encuestas
                    encuestaDAO.insertar(Encuesta(4, generateRandomId(), "15-04-2024", "Finalizada"))
                    encuestaDAO.insertar(Encuesta(5, generateRandomId(), "16-04-2024", "Comenzada"))
                    encuestaDAO.insertar(Encuesta(6, generateRandomId(), "17-04-2024", "Finalizada"))
                    encuestaDAO.insertar(Encuesta(7, generateRandomId(), "18-04-2024", "Finalizada"))
                    encuestaDAO.insertar(Encuesta(8, generateRandomId(), "19-04-2024", "Comenzada"))
                    encuestaDAO.insertar(Encuesta(9, generateRandomId(), "12-04-2024", "Comenzada"))
                    encuestaDAO.insertar(Encuesta(10, generateRandomId(), "21-04-2024", "Comenzada"))
                    encuestaDAO.insertar(Encuesta(11, generateRandomId(), "01-04-2024", "Finalizada"))
                    encuestaDAO.insertar(Encuesta(12, generateRandomId(), "20-04-2024", "Finalizada"))

                    Log.i("EncuestaRoomDatabase", "La base de datos ya contiene datos")
                }
            }

            private fun generateRandomId(): String {
                return "ID-${Random.nextInt(1000)}"
            }
        }

    }
}