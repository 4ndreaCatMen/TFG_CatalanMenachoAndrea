package com.example.tidytask

import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.example.tidytask.database.TareaDBHelper

class AgregarTareaActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_agregar_tarea)

        val etTitulo = findViewById<EditText>(R.id.etTitulo)
        val etDescripcion = findViewById<EditText>(R.id.etDescripcion)
        val spinnerPrioridad = findViewById<Spinner>(R.id.spinnerPrioridad)
        val btnGuardar = findViewById<Button>(R.id.btnGuardarTarea)

        // Opciones del spinner
        val prioridades = arrayOf("Alta", "Media", "Baja")
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, prioridades)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerPrioridad.adapter = adapter

        val dbHelper = TareaDBHelper(this)

        btnGuardar.setOnClickListener {
            val titulo = etTitulo.text.toString()
            val descripcion = etDescripcion.text.toString()
            val prioridad = spinnerPrioridad.selectedItem.toString()

            if (titulo.isNotEmpty() && descripcion.isNotEmpty()) {
                val tarea = Tarea(
                    idTarea = 0,
                    titulo = titulo,
                    descripcion = descripcion,
                    fecha = "",  // Lo dejamos vacío por ahora
                    hora = "",
                    prioridad = prioridad,
                    completada = false,
                    idUsuario = 1 // Simulado por ahora
                )

                val insertada = dbHelper.insertarTarea(tarea)

                if (insertada) {
                    Toast.makeText(this, "Tarea guardada con éxito", Toast.LENGTH_SHORT).show()
                    finish()
                } else {
                    Toast.makeText(this, "Error al guardar la tarea", Toast.LENGTH_SHORT).show()
                }

            } else {
                Toast.makeText(this, "Por favor completa todos los campos", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
