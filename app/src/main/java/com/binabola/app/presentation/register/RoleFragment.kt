package com.binabola.app.presentation.register

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

    private val viewModel by viewModels<RegisterViewModel> {
        ViewModelFactory.getInstance(requireContext())
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentRoleBinding.inflate(inflater,container,false)
        val view = binding.root
        initView()
        return view
    }

    private fun initView() {
        binding.roleGroup.setOnCheckedChangeListener { group, checkedId ->
            when(checkedId) {
                R.id.radioPlayer -> {
                    viewModel.storeRole("player")
                }
                else -> {
                    AppUtil().showToast(requireContext(),"Dalam pengembangan")
                }
            }
        }

        viewModel.getRole().observe(viewLifecycleOwner) {
            when(it) {
                "player" -> {
                    binding.radioPlayer.isChecked = true
                }
                "coach" -> {
                    binding.radioCoach.isChecked = true
                }
                "club" -> {
                    binding.radioClub.isChecked = true
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}