package com.binabola.app.presentation.register

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.binabola.app.data.Result
import com.binabola.app.databinding.FragmentAccountBinding
import com.binabola.app.presentation.AppUtil
import com.binabola.app.presentation.ViewModelFactory
import com.google.gson.Gson

class AccountFragment : Fragment() {
    private var _binding: FragmentAccountBinding? = null
    private val binding get() = _binding!!
    private val viewModel by viewModels<RegisterViewModel> {
        ViewModelFactory.getInstance(requireActivity())
    }

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
        val decodedData = Gson().fromJson<Map<String, String?>>(data, Map::class.java)

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


        val mappedData = mapOf(
            "nama" to nama,
            "email" to email,
            "password" to password,
            "role" to role,
            "tb" to decodedData["tinggi"],
            "bb" to decodedData["berat"],
            "dob" to decodedData["dob"],
            "gender" to decodedData["gender"],
        )

        viewModel.register(mappedData).observe(viewLifecycleOwner) {
            if(it != null) {
                when (it) {
                    is Result.Loading -> {
                        loadingState(true)
                    }
                    is Result.Error -> {
                        loadingState(false)
                        AppUtil().showToast(requireContext(), it.error)
                    }
                    is Result.Success -> {
                        loadingState(false)
                        AppUtil().showToast(requireContext(), it.data.message ?: "Registrasi berhasil. Silakan Login")
                        requireActivity().finish()
                    }
                }
            }
        }
    }

    private fun loadingState(show: Boolean) {
        if(show) {
            binding.btnRegister.visibility = View.GONE
            binding.loadingBar.visibility = View.VISIBLE
        } else {
            binding.btnRegister.visibility = View.VISIBLE
            binding.loadingBar.visibility = View.GONE
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}