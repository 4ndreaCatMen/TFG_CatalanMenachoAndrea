package com.example.tidytask

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.tidytask.database.TareaDBHelper

class TareasActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var tareaAdapter: TareaAdapter
    private lateinit var dbHelper: TareaDBHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tareas)

        val tvBienvenida = findViewById<TextView>(R.id.tvBienvenida)
        val btnAgregarTarea = findViewById<Button>(R.id.btnAgregarTarea)
        recyclerView = findViewById(R.id.recyclerViewTareas)

        dbHelper = TareaDBHelper(this)

        btnAgregarTarea.setOnClickListener {
            startActivity(Intent(this, AgregarTareaActivity::class.java))
        }
        val iconoNotificacion = findViewById<ImageView>(R.id.iconoNotificacion)
        iconoNotificacion.setOnClickListener {
            mostrarRecordatorios()
        }
    }

    override fun onResume() {
        super.onResume()
        mostrarTareas()
    }

    private fun mostrarTareas() {
        val listaTareas = dbHelper.obtenerTodasLasTareas()
        tareaAdapter = TareaAdapter(
            listaTareas,
            onTareaClick = { tarea ->
                val intent = Intent(this, EditarTareaActivity::class.java).apply {
                    putExtra("idTarea", tarea.idTarea)
                    putExtra("titulo", tarea.titulo)
                    putExtra("descripcion", tarea.descripcion)
                    putExtra("prioridad", tarea.prioridad)
                }
                startActivity(intent)
            },
            onTareaLongClick = { tarea ->
                mostrarDialogoEliminar(tarea)
            }
        )

        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = tareaAdapter
    }
    private fun mostrarDialogoEliminar(tarea: Tarea) {
        val builder = android.app.AlertDialog.Builder(this)
        builder.setTitle("Eliminar tarea")
        builder.setMessage("¿Estás segura de que quieres eliminar esta tarea?")
        builder.setPositiveButton("Sí") { _, _ ->
            val eliminada = dbHelper.eliminarTareaPorId(tarea.idTarea)
            if (eliminada) {
                Toast.makeText(this, "Tarea eliminada", Toast.LENGTH_SHORT).show()
                mostrarTareas()
            } else {
                Toast.makeText(this, "Error al eliminar", Toast.LENGTH_SHORT).show()
            }
        }
        builder.setNegativeButton("Cancelar", null)
        builder.show()
    }

    private fun mostrarRecordatorios() {
        val tareasConHora = dbHelper.obtenerTareasConHora()

        if (tareasConHora.isEmpty()) {
            Toast.makeText(this, "No hay recordatorios asignados", Toast.LENGTH_SHORT).show()
            return
        }

        val mensajes = tareasConHora.map { "${it.titulo} - ${it.hora}" }.toTypedArray()

        android.app.AlertDialog.Builder(this)
            .setTitle("Tareas con recordatorio")
            .setItems(mensajes, null)
            .setPositiveButton("Cerrar", null)
            .show()
    }

}
