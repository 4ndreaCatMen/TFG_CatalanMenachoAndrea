package com.example.tidytask

import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.example.tidytask.database.TareaDBHelper

class EditarTareaActivity : AppCompatActivity() {

    private lateinit var dbHelper: TareaDBHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_editar_tarea)

        val etTitulo = findViewById<EditText>(R.id.etTituloEditar)
        val etDescripcion = findViewById<EditText>(R.id.etDescripcionEditar)
        val spinnerPrioridad = findViewById<Spinner>(R.id.spinnerPrioridadEditar)
        val btnGuardar = findViewById<Button>(R.id.btnGuardarCambios)

        dbHelper = TareaDBHelper(this)

        val tareaId = intent.getIntExtra("idTarea", -1)
        val titulo = intent.getStringExtra("titulo")
        val descripcion = intent.getStringExtra("descripcion")
        val prioridad = intent.getStringExtra("prioridad")

        etTitulo.setText(titulo)
        etDescripcion.setText(descripcion)

        val prioridades = arrayOf("Alta", "Media", "Baja")
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, prioridades)
        spinnerPrioridad.adapter = adapter

        val indiceSeleccionado = prioridades.indexOf(prioridad)
        if (indiceSeleccionado != -1) {
            spinnerPrioridad.setSelection(indiceSeleccionado)
        }

        btnGuardar.setOnClickListener {
            val nuevoTitulo = etTitulo.text.toString()
            val nuevaDescripcion = etDescripcion.text.toString()
            val nuevaPrioridad = spinnerPrioridad.selectedItem.toString()

            if (nuevoTitulo.isBlank() || nuevaDescripcion.isBlank()) {
                Toast.makeText(this, "Completa todos los campos", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val actualizada = dbHelper.actualizarTarea(tareaId, nuevoTitulo, nuevaDescripcion, nuevaPrioridad)
            if (actualizada) {
                Toast.makeText(this, "Tarea actualizada", Toast.LENGTH_SHORT).show()
                finish()
            } else {
                Toast.makeText(this, "Error al actualizar", Toast.LENGTH_SHORT).show()
            }
        }

        val btnCancelar = findViewById<Button>(R.id.btnCancelar)
        btnCancelar.setOnClickListener {
            Toast.makeText(this, "Cambios descartados", Toast.LENGTH_SHORT).show()
            finish()
        }

    }
}
