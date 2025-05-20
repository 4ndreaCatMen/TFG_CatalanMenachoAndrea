package com.example.tidytask

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import com.example.tidytask.database.UsuarioDBHelper
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test

class UsuarioTest {

    private lateinit var dbHelper: UsuarioDBHelper

    @Before
    fun setup() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        dbHelper = UsuarioDBHelper(context)
    }

    @Test
    fun testRegistrarYValidarUsuario() {
        val usuario = Usuario(0, "Josemi", "Rebollo", "jtr@gmail.com", "2222")

        val registrado = dbHelper.registrarUsuario(usuario)
        assertTrue(registrado)

        val valido = dbHelper.validarUsuario("jtr@gmail.com", "2222")
        assertTrue(valido)
    }

    @Test
    fun testValidarUsuarioExistente() {
        val valido = dbHelper.validarUsuario("acm@gmail.com", "1111")
        assertTrue("El usuario acm@gmail.com ya existe", valido)
    }

    @Test
    fun testValidarUsuarioContrasenaIncorrecta() {
        val valido = dbHelper.validarUsuario("acm@gmail.com", "contramal")
        assertFalse("La contraseña es incorrecta", valido)
    }
}
