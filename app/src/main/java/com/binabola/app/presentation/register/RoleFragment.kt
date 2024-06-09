package com.binabola.app.presentation.register

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.viewModels
import androidx.fragment.app.viewModels
import com.binabola.app.R
import com.binabola.app.databinding.FragmentRoleBinding
import com.binabola.app.presentation.AppUtil
import com.binabola.app.presentation.ViewModelFactory

class RoleFragment : Fragment() {
    private var _binding: FragmentRoleBinding? = null
    private val binding get() = _binding!!
    private var preferences: SharedPreferences? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentRoleBinding.inflate(inflater,container,false)
        val view = binding.root
        preferences = requireActivity().getSharedPreferences("REGISTER",Context.MODE_PRIVATE)
        initView()
        return view
    }

    private fun initView() {
        binding.roleGroup.setOnCheckedChangeListener { group, checkedId ->
            when(checkedId) {
                R.id.radioPlayer -> {
                    preferences!!.edit().putString("role","player").apply()
                }
                else -> {
                    preferences!!.edit().remove("role").apply()
                    AppUtil().showToast(requireActivity(),"Dalam pengembangan")
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}