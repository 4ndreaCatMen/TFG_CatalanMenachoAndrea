package com.example.tidytask

import org.junit.Assert.assertEquals
import org.junit.Test

class TareaTest {

    @Test
    fun testCrearTarea() {
        val tarea = Tarea(
            idTarea = 1,
            titulo = "Hacer ejercicio",
            descripcion = "Ir al gimnasio por la tarde",
            fecha = "2025-04-27",
            hora = "18:00",
            prioridad = "Alta",
            idUsuario = 1
        )

        val resultado = tarea.crearTarea()

        assertEquals(true, resultado)
    }
}
