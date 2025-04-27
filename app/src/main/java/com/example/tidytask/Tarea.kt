package com.example.tidytask

class Tarea(
    var idTarea: Int,
    var titulo: String,
    var descripcion: String,
    var fecha: String,
    var hora: String,
    var prioridad: String,
    var completada: Boolean = false,
    var idUsuario: Int,
    var idCategoria: Int? = null,
    var idRecordatorio: Int? = null
) {
    fun crearTarea(): Boolean {

        return true
    }

    fun editarTarea(nuevoTitulo: String, nuevaDescripcion: String): Boolean {
        titulo = nuevoTitulo
        descripcion = nuevaDescripcion
        return true
    }

    fun eliminarTarea(): Boolean {

        return true
    }

    fun marcarCompletada(): Boolean {
        completada = true
        return true
    }
}
