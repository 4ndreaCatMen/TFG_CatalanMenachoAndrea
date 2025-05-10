package com.example.tidytask

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
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
        tareaAdapter = TareaAdapter(listaTareas)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = tareaAdapter
    }
}
