package com.example.tidytask

import org.junit.Assert.assertEquals
import org.junit.Test

class UsuarioTest {

    @Test
    fun testIniciarSesion() {
        val usuario = Usuario(
            idUsuario = 1,
            nombre = "Andrea",
            correo = "andrea@email.com",
            contrasena = "empanada"
        )

        val resultado = usuario.iniciarSesion()

        assertEquals(true, resultado)
    }
}
