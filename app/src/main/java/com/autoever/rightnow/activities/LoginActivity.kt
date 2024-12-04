package com.autoever.rightnow.activities

import android.content.Intent
import android.os.Bundle
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.autoever.rightnow.R
import com.google.firebase.auth.FirebaseAuth

class LoginActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_login)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val editTextEmail = findViewById<EditText>(R.id.editTextEmail)
        val editTextPassword = findViewById<EditText>(R.id.editTextPassword)
        val btnLogin = findViewById<TextView>(R.id.btnLogin)
        val btnSignup = findViewById<TextView>(R.id.btnSignup)

        btnLogin.setOnClickListener {

            val email = editTextEmail.text.toString().trim() // 이메일 공백 제거
            val password = editTextPassword.text.toString().trim()

            // 입력값 검증
            if (email.isEmpty()) {
                Toast.makeText(this, "이메일을 입력해주세요.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (password.isEmpty()) {
                Toast.makeText(this, "비밀번호를 입력해주세요.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            login(editTextEmail.text.toString(),editTextPassword.text.toString())
        }
        btnSignup.setOnClickListener {
            val intent = Intent(this, SignupActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
            startActivity(intent)
            finish()
        }
    }

    fun login(email: String, password: String){
        val auth = FirebaseAuth.getInstance()
        auth.signInWithEmailAndPassword(email,password).addOnCompleteListener(this) { task ->
            if (task.isSuccessful) {
                val intent = Intent(this, MainActivity::class.java)
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
                startActivity(intent)
            } else {
                // 로그인 실패
                Toast.makeText(baseContext, "로그인 실패: ${task.exception?.message}", Toast.LENGTH_SHORT).show()
            }
        }
    }
}