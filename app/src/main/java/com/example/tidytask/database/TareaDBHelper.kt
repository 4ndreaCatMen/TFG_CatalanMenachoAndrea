package com.example.tidytask.database

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.example.tidytask.Tarea

class TareaDBHelper(context: Context) :
    SQLiteOpenHelper(context, "tareas.db", null, 1) {

    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL(
            "CREATE TABLE tareas (" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "titulo TEXT," +
                    "descripcion TEXT," +
                    "fecha TEXT," +
                    "hora TEXT," +
                    "prioridad TEXT," +
                    "completada INTEGER," +
                    "idUsuario INTEGER)"
        )
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS tareas")
        onCreate(db)
    }

    fun insertarTarea(tarea: Tarea): Boolean {
        val db = writableDatabase
        val values = ContentValues().apply {
            put("titulo", tarea.titulo)
            put("descripcion", tarea.descripcion)
            put("fecha", tarea.fecha)
            put("hora", tarea.hora)
            put("prioridad", tarea.prioridad)
            put("completada", if (tarea.completada) 1 else 0)
            put("idUsuario", tarea.idUsuario)
        }

        val resultado = db.insert("tareas", null, values)
        db.close()
        return resultado != -1L
    }

    fun obtenerTodasLasTareas(): List<Tarea> {
        val lista = mutableListOf<Tarea>()
        val db = readableDatabase
        val cursor = db.rawQuery("SELECT * FROM tareas", null)

        if (cursor.moveToFirst()) {
            do {
                val tarea = Tarea(
                    idTarea = cursor.getInt(cursor.getColumnIndexOrThrow("id")),
                    titulo = cursor.getString(cursor.getColumnIndexOrThrow("titulo")),
                    descripcion = cursor.getString(cursor.getColumnIndexOrThrow("descripcion")),
                    fecha = cursor.getString(cursor.getColumnIndexOrThrow("fecha")),
                    hora = cursor.getString(cursor.getColumnIndexOrThrow("hora")),
                    prioridad = cursor.getString(cursor.getColumnIndexOrThrow("prioridad")),
                    completada = cursor.getInt(cursor.getColumnIndexOrThrow("completada")) == 1,
                    idUsuario = cursor.getInt(cursor.getColumnIndexOrThrow("idUsuario"))
                )
                lista.add(tarea)
            } while (cursor.moveToNext())
        }

        cursor.close()
        db.close()
        return lista
    }

    fun eliminarTareaPorId(id: Int): Boolean {
        val db = writableDatabase
        val resultado = db.delete("tareas", "id = ?", arrayOf(id.toString()))
        db.close()
        return resultado > 0
    }

    fun actualizarTarea(
        id: Int,
        nuevoTitulo: String,
        nuevaDescripcion: String,
        nuevaPrioridad: String,
        nuevaFecha: String?,
        nuevaHora: String?
    ): Boolean {
        val db = writableDatabase
        val valores = ContentValues().apply {
            put("titulo", nuevoTitulo)
            put("descripcion", nuevaDescripcion)
            put("prioridad", nuevaPrioridad)
            put("fecha", nuevaFecha)
            put("hora", nuevaHora)
        }
        val resultado = db.update("tareas", valores, "id = ?", arrayOf(id.toString()))
        db.close()
        return resultado > 0
    }

    fun actualizarEstadoCompletada(idTarea: Int, completada: Boolean): Boolean {
        val db = writableDatabase
        val valores = ContentValues().apply {
            put("completada", if (completada) 1 else 0)
        }
        val resultado = db.update("tareas", valores, "id = ?", arrayOf(idTarea.toString()))
        db.close()
        return resultado > 0
    }
    fun obtenerTareasConHora(): List<Tarea> {
        val lista = mutableListOf<Tarea>()
        val db = readableDatabase
        val cursor = db.rawQuery("SELECT * FROM tareas WHERE hora IS NOT NULL AND hora != ''", null)

        if (cursor.moveToFirst()) {
            do {
                val tarea = Tarea(
                    idTarea = cursor.getInt(cursor.getColumnIndexOrThrow("id")),
                    titulo = cursor.getString(cursor.getColumnIndexOrThrow("titulo")),
                    descripcion = cursor.getString(cursor.getColumnIndexOrThrow("descripcion")),
                    fecha = cursor.getString(cursor.getColumnIndexOrThrow("fecha")),
                    hora = cursor.getString(cursor.getColumnIndexOrThrow("hora")),
                    prioridad = cursor.getString(cursor.getColumnIndexOrThrow("prioridad")),
                    completada = cursor.getInt(cursor.getColumnIndexOrThrow("completada")) == 1,
                    idUsuario = cursor.getInt(cursor.getColumnIndexOrThrow("idUsuario"))
                )
                lista.add(tarea)
            } while (cursor.moveToNext())
        }

        cursor.close()
        db.close()
        return lista
    }


}
