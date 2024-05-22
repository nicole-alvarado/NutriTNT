    package com.example.nutritnt.database

    import android.content.Context
    import android.util.Log
    import androidx.room.Database
    import androidx.room.Room
    import androidx.room.RoomDatabase
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

    // Anotar la clase para convertirla en una Database Room
    // con una Tabla (entity) de la clase Encuesta

    @Database(entities = [Encuesta::class, Encuesta_Alimento::class, Alimento::class], version = 6, exportSchema = false)
    abstract class EncuestaRoomDatabase : RoomDatabase() {

        abstract fun encuestaAlimentoDao(): Encuesta_AlimentoDAO
        abstract fun encuestaDao(): EncuestaDAO
        abstract fun alimentoDao(): AlimentoDAO

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
                            cargarBaseDeDatos(database.encuestaDao(),database.encuestaAlimentoDao(),database.alimentoDao())
                        }
                    }
                }

                suspend fun cargarBaseDeDatos(encuestaDAO: EncuestaDAO, encuestaAlimentoDAO: Encuesta_AlimentoDAO, alimentoDAO: AlimentoDAO) {
                    Log.i("EncuestaRoomDatabase", "Cargar Base de Datos iniciado")
                    if (encuestaDAO.cantidadDeEncuestas() == 0) {
                        Log.i("EncuestaRoomDatabase", "Base de datos vacía, cargando datos de ejemplo...")
                        encuestaDAO.borrarTodos()

                        val encuesta1 = Encuesta(1,generateRandomId(),"21-05-2024","ACTIVA", "Zona A")
                        encuestaDAO.insertar(encuesta1)
                        val encuesta2 = Encuesta(2,generateRandomId(),"22-05-2024","ACTIVA", "Zona C")
                        encuestaDAO.insertar(encuesta2)
                        val encuesta3 = Encuesta( 3, generateRandomId(),"23-05-2024","ACTIVA", "Zona C")
                        encuestaDAO.insertar(encuesta3)
                        encuestaDAO.insertar(Encuesta(4, generateRandomId(), "15-04-2024", "Finalizada", "Zona B"))
                        encuestaDAO.insertar(Encuesta(5, generateRandomId(), "16-04-2024", "Comenzada", "Zona A"))
                        encuestaDAO.insertar(Encuesta(6, generateRandomId(), "17-04-2024", "Finalizada", "Zona A"))
                        encuestaDAO.insertar(Encuesta(7, generateRandomId(), "18-04-2024", "Finalizada", "Zona A"))
                        encuestaDAO.insertar(Encuesta(8, generateRandomId(), "19-04-2024", "Comenzada", "Zona A"))
                        encuestaDAO.insertar(Encuesta(9, generateRandomId(), "12-04-2024", "Comenzada", "Zona A"))
                        encuestaDAO.insertar(Encuesta(10, generateRandomId(), "21-04-2024", "Comenzada", "Zona A"))
                        encuestaDAO.insertar(Encuesta(11, generateRandomId(), "01-04-2024", "Finalizada", "Zona A"))
                        encuestaDAO.insertar(Encuesta(12, generateRandomId(), "20-04-2024", "Finalizada", "Zona A"))
                        Log.i("EncuestaRoomDatabase", "Datos de Encuestas insertados")

                    }
                    if ( alimentoDAO.cantidadDeAlimentos() == 0) {
                        val alimento1 = Alimento(alimentoId = 1,"Yogur bebible",100,4F)
                        alimentoDAO.insertar(alimento1)
                        Log.i("EncuestaRoomDatabase", "Datos de Alimentos insertados")
                    }

                    if (encuestaAlimentoDAO.cantidadDeEncuestasAlimento() == 0){
                        val encuestaAlimento1 = Encuesta_Alimento(encuestaAlimentoId = 1, portion = "1 taza", period = "Día", frecuency = 2, encuestaId = 1, alimentoId = 1)
                        encuestaAlimentoDAO.insertar(encuestaAlimento1)
                        val encuestaAlimento2 = Encuesta_Alimento(encuestaAlimentoId = 2, "1/2 taza", period = "Semana", frecuency = 3, encuestaId = 2, alimentoId = 1)
                        encuestaAlimentoDAO.insertar(encuestaAlimento2)
                        Log.i("EncuestaRoomDatabase", "Datos de Encuesta_Alimentos insertados")

                    }

                }

                private fun generateRandomId(): String {
                    return "ID-${Random.nextInt(1000)}"
                }
            }

        }
    }
