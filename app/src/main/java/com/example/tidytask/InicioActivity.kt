package com.example.tidytask

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity

class InicioActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_inicio)

        val btnIrARegistro = findViewById<Button>(R.id.btnIrARegistro)
        val btnIrALogin = findViewById<Button>(R.id.btnIrALogin)

        btnIrARegistro.setOnClickListener {
            startActivity(Intent(this, RegistroActivity::class.java))
        }

        btnIrALogin.setOnClickListener {
            startActivity(Intent(this, LoginActivity::class.java))
        }

    }
}
