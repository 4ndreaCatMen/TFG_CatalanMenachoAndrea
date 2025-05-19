package com.example.tidytask

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.example.tidytask.database.TareaDBHelper
import java.util.*

class AgregarTareaActivity : AppCompatActivity() {

    private var horaSeleccionada: String? = null
    private var fechaSeleccionada: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_agregar_tarea)

        val etTitulo = findViewById<EditText>(R.id.etTitulo)
        val etDescripcion = findViewById<EditText>(R.id.etDescripcion)
        val spinnerPrioridad = findViewById<Spinner>(R.id.spinnerPrioridad)
        val btnGuardar = findViewById<Button>(R.id.btnGuardarTarea)
        val btnSeleccionarHora = findViewById<Button>(R.id.btnSeleccionarHora)
        val btnSeleccionarFecha = findViewById<Button>(R.id.btnSeleccionarFecha)
        val tvHoraSeleccionada = findViewById<TextView>(R.id.tvHoraSeleccionada)
        val tvFechaSeleccionada = findViewById<TextView>(R.id.tvFechaSeleccionada)

        val prioridades = arrayOf("Alta", "Media", "Baja")
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, prioridades)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerPrioridad.adapter = adapter

        val dbHelper = TareaDBHelper(this)

        btnSeleccionarHora.setOnClickListener {
            val calendar = Calendar.getInstance()
            val hora = calendar.get(Calendar.HOUR_OF_DAY)
            val minuto = calendar.get(Calendar.MINUTE)

            val timePicker = TimePickerDialog(this, { _, hourOfDay, minuteOfHour ->
                horaSeleccionada = String.format("%02d:%02d", hourOfDay, minuteOfHour)
                tvHoraSeleccionada.text = horaSeleccionada
            }, hora, minuto, true)

            timePicker.show()
        }

        btnSeleccionarFecha.setOnClickListener {
            val calendar = Calendar.getInstance()
            val year = calendar.get(Calendar.YEAR)
            val month = calendar.get(Calendar.MONTH)
            val day = calendar.get(Calendar.DAY_OF_MONTH)

            val datePicker = DatePickerDialog(this, { _, y, m, d ->
                fechaSeleccionada = String.format("%02d/%02d/%04d", d, m + 1, y)
                tvFechaSeleccionada.text = fechaSeleccionada
            }, year, month, day)

            datePicker.show()
        }

        btnGuardar.setOnClickListener {
            val titulo = etTitulo.text.toString()
            val descripcion = etDescripcion.text.toString()
            val prioridad = spinnerPrioridad.selectedItem.toString()

            if (titulo.isNotEmpty() && descripcion.isNotEmpty()) {
                val tarea = Tarea(
                    idTarea = 0,
                    titulo = titulo,
                    descripcion = descripcion,
                    fecha = fechaSeleccionada ?: "",
                    hora = horaSeleccionada ?: "",
                    prioridad = prioridad,
                    completada = false,
                    idUsuario = 1
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
