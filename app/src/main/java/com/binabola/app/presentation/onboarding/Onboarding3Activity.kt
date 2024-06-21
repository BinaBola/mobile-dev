package com.binabola.app.presentation.onboarding

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.binabola.app.databinding.ActivityOnboarding3Binding
import com.binabola.app.presentation.login.LoginActivity

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