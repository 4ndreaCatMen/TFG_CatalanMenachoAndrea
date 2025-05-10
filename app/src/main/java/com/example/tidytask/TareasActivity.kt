package com.example.tidytask

import android.content.Intent
import android.os.Bundle
import android.widget.Button
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
    }

    override fun onResume() {
        super.onResume()
        mostrarTareas()
    }

    private fun mostrarTareas() {
        val listaTareas = dbHelper.obtenerTodasLasTareas()
        tareaAdapter = TareaAdapter(listaTareas) { tareaSeleccionada ->
            mostrarDialogoEliminar(tareaSeleccionada)
        }
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

}
