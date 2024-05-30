package com.example.nutritnt.database

import androidx.lifecycle.LiveData
import com.example.nutritnt.database.dao.AlimentoDAO
import com.example.nutritnt.database.dao.EncuestaDAO
import com.example.nutritnt.database.entities.Alimento
import com.example.nutritnt.database.entities.Encuesta
import com.example.nutritnt.database.entities.Encuesta_Alimento
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class RepositorioDeAlimentos  (private val alimentoDAO: AlimentoDAO) {
    // LiveData observada va a notificar a sus observadores cuando los datos cambien.
    val todosLosAlimentos: LiveData<List<Alimento>> = alimentoDAO.getAlimentos()

    // La inserción se realiza en un hilo que no sea UI, ya que sino la aplicación se bloqueará. Para
    // informar a los métodos de llamada debemos realizar la inserción en una función suspend.
    // De esta manera, Room garantiza que no se realicen operaciones de larga ejecución
    // en el hilo principal, bloqueando la interfaz de usuario.
    suspend fun insertar(alimento: Alimento){
        alimentoDAO.insertar(alimento)
    }

    fun getAlimentoByEncuestaAlimentoId(encuestaAlimentoId: Int): LiveData<Alimento> {
        return alimentoDAO.getAlimentoByEncuestaAlimentoId(encuestaAlimentoId)
    }
}