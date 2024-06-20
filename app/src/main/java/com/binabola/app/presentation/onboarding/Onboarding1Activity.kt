package com.binabola.app.presentation.onboarding

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.binabola.app.R
import com.binabola.app.databinding.ActivityOnboarding1Binding
import com.binabola.app.databinding.ActivityOnboarding3Binding
import com.binabola.app.databinding.ActivityWelcomeBinding
import com.binabola.app.presentation.login.LoginActivity

class Onboarding1Activity : AppCompatActivity() {
    private lateinit var binding: ActivityOnboarding1Binding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityOnboarding1Binding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.skipButton.setOnClickListener {
            startActivity(Intent(this@Onboarding1Activity, LoginActivity::class.java))

        }
        binding.nextButton.setOnClickListener {
            startActivity(Intent(this@Onboarding1Activity, Onboarding2Activity::class.java))

        }
    }
}