package com.example.nutritnt.database

import androidx.lifecycle.LiveData
import com.example.nutritnt.database.dao.EncuestaAlimentoDAO
import com.example.nutritnt.database.entities.Encuesta
import com.example.nutritnt.database.entities.EncuestaAlimento
import com.example.nutritnt.database.relations.EncuestaAlimento_AlimentoInformacionNutricional
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext

class RepositorioDeEncuestasAlimento(private val encuestaAlimentoDAO : EncuestaAlimentoDAO) {
    // LiveData observada va a notificar a sus observadores cuando los datos cambien.
    val todasLasEncuestasAlimento: LiveData<List<EncuestaAlimento>> = encuestaAlimentoDAO.getEncuestasAlimento()

    // La inserción se realiza en un hilo que no sea UI, ya que sino la aplicación se bloqueará. Para
    // informar a los métodos de llamada debemos realizar la inserción en una función suspend.
    // De esta manera, Room garantiza que no se realicen operaciones de larga ejecución
    // en el hilo principal, bloqueando la interfaz de usuario.
    suspend fun insertar(encuestaAlimento: EncuestaAlimento){
        encuestaAlimentoDAO.insertar(encuestaAlimento)
    }

    suspend fun actualizar(encuestaAlimento: EncuestaAlimento){
        encuestaAlimentoDAO.actualizar(encuestaAlimento)
    }

    suspend fun getEncuestaAlimentosByZonaAndAlimento(zona: String, alimentoId: Int): List<EncuestaAlimento> {
        return withContext(Dispatchers.IO) {
            encuestaAlimentoDAO.getEncuestaAlimentosByZonaAndAlimento(zona, alimentoId)
        }
    }

    suspend fun getEncuestaAlimentosByZona(zona: String): List<EncuestaAlimento> {
        return withContext(Dispatchers.IO) {
            encuestaAlimentoDAO.getEncuestaAlimentosByZona(zona)
        }
    }

    suspend fun getEncuestasAlimentosConInfo(zona: String): List<EncuestaAlimento_AlimentoInformacionNutricional> {
        return encuestaAlimentoDAO.getEncuestasAlimentosConInfo(zona)
    }

    fun getEncuestaAlimentoByEncuestaAndAlimento(encuestaId: Int, alimentoId: Int): LiveData<EncuestaAlimento>{
        return encuestaAlimentoDAO.getEncuestaAlimentoByEncuestaAndAlimento(encuestaId, alimentoId)
    }

    fun getEncuestasAlimentosByEncuestaId(id: Int): LiveData<List<EncuestaAlimento>>{
        return encuestaAlimentoDAO.getEncuestasAlimentosByEncuestaId(id)
    }

    fun getEncuestaAlimentoById(id: Int): LiveData<EncuestaAlimento>{
        return encuestaAlimentoDAO.getEncuestaAlimentoById(id)
    }

    suspend fun getEncuestaAlimentosByCodigoParticipante(codigoParticipante: String): List<EncuestaAlimento> {
        return encuestaAlimentoDAO.getEncuestaAlimentosByCodigoParticipante(codigoParticipante)
    }

    fun getEncuestaAlimentosWithAlimentoAndInfo(codigoParticipante: String): Flow<List<EncuestaAlimento_AlimentoInformacionNutricional>> {
        return encuestaAlimentoDAO.getEncuestaAlimentosWithAlimentoAndInfoByCodigoParticipante(codigoParticipante)
    }
}