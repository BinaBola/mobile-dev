package com.binabola.app.presentation.onboarding

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.binabola.app.databinding.ActivityOnboarding2Binding
import com.binabola.app.presentation.login.LoginActivity

class Onboarding2Activity : AppCompatActivity() {
    private lateinit var binding: ActivityOnboarding2Binding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityOnboarding2Binding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.skipButton.setOnClickListener {
            startActivity(Intent(this@Onboarding2Activity, LoginActivity::class.java))

        }
        binding.nextButton.setOnClickListener {
            startActivity(Intent(this@Onboarding2Activity, Onboarding3Activity::class.java))

        }
    }
}