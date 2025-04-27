package com.example.tidytask

class Usuario(
    var idUsuario: Int,
    var nombre: String,
    var correo: String,
    var contrasena: String
) {
    fun registrarUsuario(): Boolean {

        return true
    }

    fun iniciarSesion(): Boolean {

        return true
    }
}
