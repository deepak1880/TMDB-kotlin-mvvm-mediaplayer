package com.example.tmdbapp.login

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.tmdbapp.R
import com.example.tmdbapp.home.MainActivity
import com.example.tmdbapp.model.User

class LoginActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        val emailEditText = findViewById<EditText>(R.id.login_email)
        val passwordEditText = findViewById<EditText>(R.id.login_password)
        val buttonSubmit = findViewById<Button>(R.id.login_btnSubmit)

        // Authentication
        buttonSubmit.setOnClickListener {
            val inputEmail = emailEditText.text.toString()
            val inputPassword = passwordEditText.text.toString()
            if (User.EMAIL == inputEmail && User.PASSWORD == inputPassword) {
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
            }
            else {
                val errorTextView = findViewById<TextView>(R.id.login_errorMessage)
                errorTextView.text = getString(R.string.login_invalidCredentials)
            }
        }

    }
}