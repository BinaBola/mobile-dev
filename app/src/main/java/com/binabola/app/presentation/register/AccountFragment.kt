package com.binabola.app.presentation.register

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.binabola.app.R
import com.binabola.app.databinding.FragmentAccountBinding
import com.binabola.app.databinding.FragmentDataBinding

class AccountFragment : Fragment() {
    private var _binding: FragmentAccountBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAccountBinding.inflate(inflater, container, false)
        val view = binding.root

        initView()
        return view
    }

    private fun initView() {
        binding.btnRegister.setOnClickListener {
            regisAttempt()
        }
    }

    private fun regisAttempt() {
        val prefs = requireActivity().getSharedPreferences("REGISTER", Context.MODE_PRIVATE)
        val role = prefs.getString("role",null)
        val data = prefs.getString("data",null)

        val nama = binding.edtName.text.toString()
        val email = binding.edtEmail.text.toString()
        val password = binding.edtPassword.text.toString()

        if (nama.isEmpty()) {
            binding.edtName.error = "Nama tidak boleh kosong"
            return
        }
        if (email.isEmpty()) {
            binding.edtName.error = "Email tidak boleh kosong"
            return
        }
        if (password.isEmpty()) {
            binding.edtName.error = "Password tidak boleh kosong"
            return
        }

        println(role)
        println(data)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}