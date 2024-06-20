package com.binabola.app.presentation

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.activity.enableEdgeToEdge
import com.binabola.app.R
import com.binabola.app.databinding.ActivityWelcomeBinding
import com.binabola.app.presentation.login.LoginActivity
import com.binabola.app.presentation.onboarding.Onboarding1Activity
import com.binabola.app.presentation.register.RegisterActivity

class WelcomeActivity : AppCompatActivity() {
    private lateinit var binding: ActivityWelcomeBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityWelcomeBinding.inflate(layoutInflater)
        setContentView(binding.root)
        enableEdgeToEdge()

        Handler(Looper.getMainLooper()).postDelayed({
            val intent = Intent(this, Onboarding1Activity::class.java)
            startActivity(intent)
            finish()
        }, SPLASHTIMEOUT.toLong())
    }

    companion object {
        const val SPLASHTIMEOUT = 1500
    }

}