package com.example.myapplication

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.animation.AnimationUtils
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.myapplication.db.Connection

class MainActivity : AppCompatActivity() {

    private lateinit var dbHelper: Connection

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        dbHelper = Connection(this).apply {
            val db = readableDatabase
            val cursor = db.rawQuery("SELECT * FROM users LIMIT 1", null)
            if (cursor.count == 0) {
                writableDatabase.execSQL("INSERT INTO users (email, password) VALUES ('test@echoes.com', '123456')")
                Log.d("LOGIN_DEBUG", "Usuario de prueba insertado")
            }
            cursor.close()
        }


        val btnLogin = findViewById<Button>(R.id.btnLogin)
        val etEmail = findViewById<EditText>(R.id.etEmail)
        val etPassword = findViewById<EditText>(R.id.etPassword)

        btnLogin.setOnClickListener {
            val scaleAnimation = AnimationUtils.loadAnimation(this, R.anim.button_scale)
            btnLogin.startAnimation(scaleAnimation)

            Handler(Looper.getMainLooper()).postDelayed({
                val email = etEmail.text.toString().trim()
                val password = etPassword.text.toString().trim()

                when {
                    email.isEmpty() || password.isEmpty() -> {
                        Toast.makeText(this, "Email y contraseña son obligatorios", Toast.LENGTH_SHORT).show()
                    }
                    validarLogin(email, password) -> {
                        startActivity(Intent(this, HomeActivity::class.java))
                        finish()
                    }
                    else -> {
                        Log.d("LOGIN_DEBUG", "Fallo login con: $email / $password")
                        Log.d("LOGIN_DEBUG", dbHelper.debugUsers()) // Debug: ver usuarios en BD
                        Toast.makeText(this, "Credenciales incorrectas", Toast.LENGTH_LONG).show()
                    }
                }
            }, 100)
        }
    }

    private fun validarLogin(email: String, password: String): Boolean {
        val db = dbHelper.readableDatabase
        val query = "SELECT * FROM users WHERE email = ? AND password = ?"
        val cursor = db.rawQuery(query, arrayOf(email, password))
        val isValid = cursor.count > 0
        cursor.close()
        return isValid
    }


    private fun Connection.debugUsers(): String {
        val db = readableDatabase
        val cursor = db.rawQuery("SELECT * FROM users", null)
        return buildString {
            append("Usuarios en BD:\n")
            while (cursor.moveToNext()) {
                append("ID: ${cursor.getInt(0)}, Email: ${cursor.getString(1)}\n")
            }
            cursor.close()
        }
    }
}