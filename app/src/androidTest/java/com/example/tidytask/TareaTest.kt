package com.example.tidytask

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import com.example.tidytask.database.TareaDBHelper
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test

class TareaTest {

    private lateinit var dbHelper: TareaDBHelper

    @Before
    fun setup() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        dbHelper = TareaDBHelper(context)
    }

    @Test
    fun testInsertarYObtenerTarea() {
        val tarea = Tarea(
            idTarea = 0,
            titulo = "Probar test",
            descripcion = "Ver si se guarda bien",
            fecha = "20/05/2025",
            hora = "19:00",
            prioridad = "Alta",
            completada = false,
            idUsuario = 1
        )

        val insertada = dbHelper.insertarTarea(tarea)
        assertTrue("La tarea debería insertarse correctamente", insertada)

        val tareas = dbHelper.obtenerTodasLasTareas()
        val tareaInsertada = tareas.find { it.titulo == "Probar test" }

        assertNotNull("Debería encontrarse la tarea recién insertada", tareaInsertada)
        assertEquals("La descripción debe coincidir", "Ver si se guarda bien", tareaInsertada?.descripcion)
    }

    @Test
    fun testActualizarEstadoCompletada() {
        val tarea = Tarea(
            idTarea = 0,
            titulo = "Tarea para completar",
            descripcion = "Debemos completarla",
            fecha = "20/05/2025",
            hora = "21:00",
            prioridad = "Media",
            completada = false,
            idUsuario = 1
        )

        val insertada = dbHelper.insertarTarea(tarea)
        assertTrue("La tarea debería insertarse antes de actualizar", insertada)

        val tareas = dbHelper.obtenerTodasLasTareas()
        val idUltima = tareas.last().idTarea
        val actualizado = dbHelper.actualizarEstadoCompletada(idUltima, true)
        assertTrue("El estado debería actualizarse", actualizado)
    }


    @Test
    fun testEliminarTarea() {
        val tarea = Tarea(
            idTarea = 0,
            titulo = "Tarea a eliminar",
            descripcion = "Esto se eliminará",
            fecha = "20/05/2025",
            hora = "20:00",
            prioridad = "Baja",
            completada = false,
            idUsuario = 1
        )

        val insertada = dbHelper.insertarTarea(tarea)
        assertTrue(insertada)

        val tareas = dbHelper.obtenerTodasLasTareas()
        val idNueva = tareas.last().idTarea
        val eliminada = dbHelper.eliminarTareaPorId(idNueva)
        assertTrue("La tarea debería eliminarse", eliminada)
    }
    @Test
    fun testActualizarTarea() {
        val tarea = Tarea(
            idTarea = 0,
            titulo = "Tarea original",
            descripcion = "Descripción original",
            fecha = "21/05/2025",
            hora = "10:00",
            prioridad = "Alta",
            completada = false,
            idUsuario = 1
        )
        dbHelper.insertarTarea(tarea)

        val lista = dbHelper.obtenerTodasLasTareas()
        assertTrue("No se insertó la tarea correctamente", lista.isNotEmpty())
        val tareaInsertada = lista.last()

        val actualizado = dbHelper.actualizarTarea(
            id = tareaInsertada.idTarea,
            nuevoTitulo = "Tarea actualizada",
            nuevaDescripcion = "Descripción actualizada",
            nuevaPrioridad = "Media",
            nuevaFecha = "22/05/2025",
            nuevaHora = "12:00"
        )

        assertTrue("La tarea no se actualizó correctamente", actualizado)

        val tareaModificada = dbHelper.obtenerTodasLasTareas().find { it.idTarea == tareaInsertada.idTarea }
        assertEquals("Tarea actualizada", tareaModificada?.titulo)
        assertEquals("Descripción actualizada", tareaModificada?.descripcion)
        assertEquals("Media", tareaModificada?.prioridad)
        assertEquals("22/05/2025", tareaModificada?.fecha)
        assertEquals("12:00", tareaModificada?.hora)
    }
    @Test
    fun testObtenerTareasConHora() {
        val tareaConHora = Tarea(
            idTarea = 0,
            titulo = "Tarea con hora",
            descripcion = "Recordatorio importante",
            fecha = "21/05/2025",
            hora = "15:30",
            prioridad = "Alta",
            completada = false,
            idUsuario = 1
        )
        dbHelper.insertarTarea(tareaConHora)

        val tareasConHora = dbHelper.obtenerTareasConHora()
        assertTrue("No se encontró ninguna tarea con hora asignada", tareasConHora.any { it.titulo == "Tarea con hora" })
    }

}
