package com.autoever.rightnow.activities

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.autoever.rightnow.R
import com.autoever.rightnow.models.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class SignupActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_signup)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val editTextEmail = findViewById<EditText>(R.id.editTextEmail)
        val editTextPassword = findViewById<EditText>(R.id.editTextPassword)
        val editTextPassword2 = findViewById<EditText>(R.id.editTextPassword2)
        val spinner: Spinner = findViewById(R.id.spinner)
        val btnComplete: TextView = findViewById(R.id.btnComplete)

        ArrayAdapter.createFromResource(
            this,
            R.array.age_array,
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            // 드롭다운 레이아웃 설정
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            // 어댑터 설정
            spinner.adapter = adapter
        }

        btnComplete.setOnClickListener {

            val selectedAge = spinner.selectedItem.toString().toInt()

            val user = User(
                "",
                editTextEmail.text.toString(),
                selectedAge
            )

            Log.d("유저 정보: ", user.toString())
            if (editTextPassword.text == editTextPassword2.text){
                signUp(user,editTextPassword.text.toString())
            }
        }
    }
    fun signUp(user: User, password: String) {
        val auth = FirebaseAuth.getInstance()
        auth.createUserWithEmailAndPassword(user.email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // 회원가입 성공
                    val userId = auth.currentUser?.uid
                    if (userId != null) {
                        // User 객체에 ID 설정
                        user.id = userId // id만 입력해준다.
                        // Firestore에 사용자 데이터 저장
                        saveUserData(user)
                    }

                } else {
                    // 에러 처리
                    Toast.makeText(this, task.exception?.message, Toast.LENGTH_LONG).show()
                }
            }
    }
    private fun saveUserData(user: User) {
        val firestore = FirebaseFirestore.getInstance()

        // 사용자 UID를 Firestore 문서 ID로 사용하여 저장
        firestore.collection("users")
            .document(user.id) // UID를 문서 ID로 사용
            .set(user) // 사용자 객체를 저장
            .addOnSuccessListener {
                Log.d("SignUpActivity", "User data successfully written!")

                val intent = Intent(this, MainActivity::class.java)
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
                startActivity(intent)
            }
            .addOnFailureListener { e ->
                Log.e("SignUpActivity", "Error writing document", e)
            }
    }
}