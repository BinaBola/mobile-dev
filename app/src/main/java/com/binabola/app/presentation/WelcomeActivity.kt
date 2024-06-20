package com.binabola.app.presentation

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.binabola.app.R
import com.binabola.app.databinding.ActivityWelcomeBinding
import com.binabola.app.presentation.login.LoginActivity
import com.binabola.app.presentation.register.RegisterActivity

class WelcomeActivity : AppCompatActivity() {
    private lateinit var binding: ActivityWelcomeBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityWelcomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.login.setOnClickListener {
            startActivity(Intent(this@WelcomeActivity, LoginActivity::class.java))

        }
        binding.register.setOnClickListener {
            startActivity(Intent(this@WelcomeActivity, RegisterActivity::class.java))

        }
    }
}