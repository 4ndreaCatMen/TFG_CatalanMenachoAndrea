package com.example.tidytask

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.example.tidytask.database.TareaDBHelper
import java.util.*

class EditarTareaActivity : AppCompatActivity() {

    private lateinit var dbHelper: TareaDBHelper
    private var fechaSeleccionada: String? = null
    private var horaSeleccionada: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_editar_tarea)

        val etTitulo = findViewById<EditText>(R.id.etTituloEditar)
        val etDescripcion = findViewById<EditText>(R.id.etDescripcionEditar)
        val spinnerPrioridad = findViewById<Spinner>(R.id.spinnerPrioridadEditar)
        val btnGuardar = findViewById<Button>(R.id.btnGuardarCambios)
        val btnCancelar = findViewById<Button>(R.id.btnCancelar)
        val btnSeleccionarHora = findViewById<Button>(R.id.btnSeleccionarHora)
        val btnSeleccionarFecha = findViewById<Button>(R.id.btnSeleccionarFecha)
        val tvHoraSeleccionada = findViewById<TextView>(R.id.tvHoraSeleccionada)
        val tvFechaSeleccionada = findViewById<TextView>(R.id.tvFechaSeleccionada)


        dbHelper = TareaDBHelper(this)

        val tareaId = intent.getIntExtra("idTarea", -1)
        val titulo = intent.getStringExtra("titulo")
        val descripcion = intent.getStringExtra("descripcion")
        val prioridad = intent.getStringExtra("prioridad")
        fechaSeleccionada = intent.getStringExtra("fecha")
        horaSeleccionada = intent.getStringExtra("hora")
        tvHoraSeleccionada.text = horaSeleccionada ?: "Hora no seleccionada"
        tvFechaSeleccionada.text = fechaSeleccionada ?: "Fecha no seleccionada"

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
            val fechaFinal = fechaSeleccionada ?: intent.getStringExtra("fecha")
            val horaFinal = horaSeleccionada ?: intent.getStringExtra("hora")

            if (nuevoTitulo.isBlank() || nuevaDescripcion.isBlank()) {
                Toast.makeText(this, "Completa todos los campos", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val actualizada = dbHelper.actualizarTarea(tareaId, nuevoTitulo, nuevaDescripcion, nuevaPrioridad, fechaFinal, horaFinal)
            if (actualizada) {
                Toast.makeText(this, "Tarea actualizada", Toast.LENGTH_SHORT).show()
                finish()
            } else {
                Toast.makeText(this, "Error al actualizar", Toast.LENGTH_SHORT).show()
            }
        }

        btnCancelar.setOnClickListener {
            Toast.makeText(this, "Cambios descartados", Toast.LENGTH_SHORT).show()
            finish()
        }

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
            val año = calendar.get(Calendar.YEAR)
            val mes = calendar.get(Calendar.MONTH)
            val dia = calendar.get(Calendar.DAY_OF_MONTH)

            val datePicker = DatePickerDialog(this, { _, year, month, dayOfMonth ->
                fechaSeleccionada = String.format("%02d/%02d/%04d", dayOfMonth, month + 1, year)
                tvFechaSeleccionada.text = fechaSeleccionada
            }, año, mes, dia)

            datePicker.show()
        }
        val btnBack = findViewById<ImageButton>(R.id.btnBack)
        btnBack.setOnClickListener {
            finish()
        }

    }
}
