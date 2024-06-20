package com.binabola.app.presentation.onboarding

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.binabola.app.R
import com.binabola.app.databinding.ActivityOnboarding2Binding
import com.binabola.app.databinding.ActivityOnboarding3Binding
import com.binabola.app.presentation.login.LoginActivity
import com.binabola.app.presentation.register.RegisterActivity

class Onboarding3Activity : AppCompatActivity() {
    private lateinit var binding: ActivityOnboarding3Binding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityOnboarding3Binding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.loginButton.setOnClickListener {
            startActivity(Intent(this@Onboarding3Activity, LoginActivity::class.java))

        }
        binding.skipButton.setOnClickListener {
            startActivity(Intent(this@Onboarding3Activity, LoginActivity::class.java))

        }

    }
}