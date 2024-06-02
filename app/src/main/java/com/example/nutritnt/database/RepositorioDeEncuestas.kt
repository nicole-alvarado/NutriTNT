package com.example.nutritnt.database

import androidx.lifecycle.LiveData
import com.example.nutritnt.database.dao.EncuestaDAO
import com.example.nutritnt.database.entities.Encuesta

class RepositorioDeEncuestas (private val encuestaDAO: EncuestaDAO) {
    // LiveData observada va a notificar a sus observadores cuando los datos cambien.
    val todasLasEncuestas: LiveData<List<Encuesta>> = encuestaDAO.getEncuestas()

    // La inserción se realiza en un hilo que no sea UI, ya que sino la aplicación se bloqueará. Para
    // informar a los métodos de llamada debemos realizar la inserción en una función suspend.
    // De esta manera, Room garantiza que no se realicen operaciones de larga ejecución
    // en el hilo principal, bloqueando la interfaz de usuario.
    suspend fun insertar(encuesta: Encuesta){
        encuestaDAO.insertar(encuesta)
    }

    suspend fun getZonas(): List<String> {
        return encuestaDAO.getAllZonas()
    }
}