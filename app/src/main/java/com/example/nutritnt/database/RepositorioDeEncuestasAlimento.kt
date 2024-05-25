package com.example.nutritnt.database

import androidx.lifecycle.LiveData
import com.example.nutritnt.database.dao.Encuesta_AlimentoDAO
import com.example.nutritnt.database.entities.Alimento
import com.example.nutritnt.database.entities.Encuesta
import com.example.nutritnt.database.entities.Encuesta_Alimento
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class RepositorioDeEncuestasAlimento(private val encuestaAlimentoDAO : Encuesta_AlimentoDAO) {
    // LiveData observada va a notificar a sus observadores cuando los datos cambien.
    val todasLasEncuestasAlimento: LiveData<List<Encuesta_Alimento>> = encuestaAlimentoDAO.getEncuestasAlimento()

    // La inserción se realiza en un hilo que no sea UI, ya que sino la aplicación se bloqueará. Para
    // informar a los métodos de llamada debemos realizar la inserción en una función suspend.
    // De esta manera, Room garantiza que no se realicen operaciones de larga ejecución
    // en el hilo principal, bloqueando la interfaz de usuario.
    suspend fun insertar(encuestaAlimento: Encuesta_Alimento){
        encuestaAlimentoDAO.insertar(encuestaAlimento)
    }

    suspend fun getEncuestaAlimentosByZonaAndAlimento(zona: String, alimentoId: Int): List<Encuesta_Alimento> {
        return withContext(Dispatchers.IO) {
            encuestaAlimentoDAO.getEncuestaAlimentosByZonaAndAlimento(zona, alimentoId)
        }
    }





}