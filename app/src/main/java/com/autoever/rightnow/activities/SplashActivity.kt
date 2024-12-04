package com.autoever.rightnow.activities

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.autoever.rightnow.R
import com.google.firebase.auth.FirebaseAuth

class SplashActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_splash)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        Handler(Looper.getMainLooper()).postDelayed({
            val auth = FirebaseAuth.getInstance()
            val currentUser = auth.currentUser

            var intent: Intent? = null
            if (currentUser != null) {
                // 사용자가 로그인된 상태
                Log.d("IntroActivity야 안녕!!!!!!!", currentUser.toString())
                intent = Intent(this, MainActivity::class.java)
            } else {
                // 사용자가 로그인 안 된 상태
                Log.d("else", "이건 아니지")
                intent = Intent(this, LoginActivity::class.java)
            }
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
            startActivity(intent)
        }, 3000)
    }
}