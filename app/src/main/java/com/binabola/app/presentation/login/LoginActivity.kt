package com.binabola.app.presentation.login

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.fragment.app.viewModels
import com.binabola.app.R
import com.binabola.app.data.Result
import com.binabola.app.databinding.ActivityLoginBinding
import com.binabola.app.presentation.AppUtil
import com.binabola.app.presentation.MainActivity
import com.binabola.app.presentation.ViewModelFactory
import com.binabola.app.presentation.register.RegisterActivity
import com.binabola.app.presentation.register.RegisterViewModel

class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding
    private val viewModel by viewModels<LoginViewModel> {
        ViewModelFactory.getInstance(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnRegister.setOnClickListener {
            startActivity(Intent(this@LoginActivity, RegisterActivity::class.java))
        }

        binding.btnLogin.setOnClickListener {
            loginAttempt()
        }

        binding.btncontinue.setOnClickListener {
            AppUtil().showToast(this, "Dalam pengembangan")
        }
    }

    private fun loginAttempt() {
        val email = binding.edtEmail.text.toString()
        val password = binding.edtPassword.text.toString()

        if (email.isEmpty()) {
            binding.edtEmail.error = "Email harus diisi"
            binding.edtEmail.requestFocus()
            return
        }

        if (password.isEmpty()) {
            binding.edtPassword.error = "Password harus diisi"
            binding.edtPassword.requestFocus()
            return
        }

        viewModel.login(email, password).observe(this) {
            if(it != null) {
                when (it) {
                    is Result.Loading -> {
                        loadingState(true)
                    }
                    is Result.Error -> {
                        loadingState(false)
                        AppUtil().showToast(this@LoginActivity, it.error)
                    }
                    is Result.Success -> {
                        loadingState(false)
                        val result = it.data

                        viewModel.saveSession(result)
                        startActivity(Intent(this@LoginActivity, MainActivity::class.java))
                        finish()
                    }
                }
            }
        }
    }


    private fun loadingState(show: Boolean) {
        if(show) {
            binding.btnLogin.visibility = View.GONE
            binding.loadingBar.visibility = View.VISIBLE
        } else {
            binding.btnLogin.visibility = View.VISIBLE
            binding.loadingBar.visibility = View.GONE
        }
    }
}