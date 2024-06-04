package com.binabola.app.presentation.login

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.binabola.app.R
import com.binabola.app.databinding.ActivityLoginBinding
import com.binabola.app.presentation.AppUtil
import com.binabola.app.presentation.MainActivity
import com.binabola.app.presentation.register.RegisterActivity

class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnRegister.setOnClickListener {
            startActivity(Intent(this@LoginActivity, RegisterActivity::class.java))
        }

        binding.btnLogin.setOnClickListener {
            startActivity(Intent(this@LoginActivity, MainActivity::class.java))
        }

        binding.btncontinue.setOnClickListener {
            AppUtil().showToast(this, "Dalam pengembangan")
        }
    }
}