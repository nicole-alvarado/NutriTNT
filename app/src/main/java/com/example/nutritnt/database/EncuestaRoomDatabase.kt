package com.example.nutritnt.database

import android.content.Context
import android.util.Log
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.Transaction
import com.example.nutritnt.database.dao.AlimentoDAO
import com.example.nutritnt.database.dao.EncuestaDAO
import com.example.nutritnt.database.dao.EncuestaAlimentoDAO
import com.example.nutritnt.database.dao.EncuestadorDAO
import com.example.nutritnt.database.dao.InformacionNutricionalDAO
import com.example.nutritnt.database.dao.ZonaDAO
import com.example.nutritnt.database.entities.Alimento
import com.example.nutritnt.database.entities.Encuesta
import com.example.nutritnt.database.entities.EncuestaAlimento
import com.example.nutritnt.database.entities.Encuestador
import com.example.nutritnt.database.entities.InformacionNutricional
import com.example.nutritnt.database.entities.Zona
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

@Database(entities = [Encuesta::class, EncuestaAlimento::class, Alimento::class, Encuestador::class, Zona::class, InformacionNutricional::class], version = 6, exportSchema = false)
abstract class EncuestaRoomDatabase : RoomDatabase() {

    abstract fun encuestaAlimentoDao(): EncuestaAlimentoDAO
    abstract fun encuestaDao(): EncuestaDAO
    abstract fun alimentoDao(): AlimentoDAO
    abstract fun encuestadorDao(): EncuestadorDAO

    abstract fun zonaDao(): ZonaDAO
    abstract fun informacionNutricionalDao(): InformacionNutricionalDAO

    companion object {
        @Volatile
        private var INSTANCIA: EncuestaRoomDatabase? = null

        fun obtenerDatabase(context: Context): EncuestaRoomDatabase {

            return INSTANCIA ?: synchronized(this) {

                val instancia = Room.databaseBuilder(
                    context.applicationContext,
                    EncuestaRoomDatabase::class.java,
                    "encuesta_database"
                ).fallbackToDestructiveMigration()
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
            encuestasAlimento: List<EncuestaAlimento>,
            encuestadores: List<Encuestador>,
            zonas: List<Zona>,
            listaInformacionNutricional: List<InformacionNutricional>,
            encuestaDAO: EncuestaDAO,
            encuestaAlimentoDAO: EncuestaAlimentoDAO,
            alimentoDAO: AlimentoDAO,
            encuestadorDAO: EncuestadorDAO,
            zonaDAO: ZonaDAO,
            informacionNutricionalDAO: InformacionNutricionalDAO
        ): Boolean = withContext(Dispatchers.IO) {
            try {
            listaInformacionNutricional.forEach{info ->

                informacionNutricionalDAO.insertar(info)
            }

            encuestasGeneral.forEach { encuestaGeneral ->
                encuestaDAO.insertar(encuestaGeneral)

            }

            alimentos.forEach { alimento ->
                val alimentoId = alimentoDAO.insertar(alimento)

            }

            encuestasAlimento.forEach { encuestaAlimento ->
                 encuestaAlimentoDAO.insertar(encuestaAlimento)
            }

            encuestadores.forEach{encuestador ->
                encuestadorDAO.insertar(encuestador)
            }

            zonas.forEach{zona ->
                zonaDAO.insertar(zona)
            }

             true
            } catch (e: Exception) {
                false // Indica que hubo un error durante la inserción
            }
        }

    }
}
