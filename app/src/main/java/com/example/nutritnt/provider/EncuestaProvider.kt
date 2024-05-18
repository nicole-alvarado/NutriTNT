package com.example.nutritnt.provider

import com.example.nutritnt.models.Encuest
import kotlin.random.Random

class EncuestaProvider {
    companion object{
        private fun generateRandomId(): String {
            return "ID-${Random.nextInt(1000)}"
        }

        val encuestaList = listOf<Encuest>(
            Encuest(generateRandomId(),"22-02-2024","ACTIVA"),
            Encuest(generateRandomId(),"23-02-2024","ACTIVA"),
            Encuest(generateRandomId(),"24-02-2024","ACTIVA"))
    }
}