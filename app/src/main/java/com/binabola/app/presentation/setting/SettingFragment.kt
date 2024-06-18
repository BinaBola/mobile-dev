package com.binabola.app.presentation.setting

import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.findNavController
import com.binabola.app.R
import com.binabola.app.databinding.FragmentSettingBinding
import com.binabola.app.presentation.MainViewModel
import com.binabola.app.presentation.ViewModelFactory
import com.binabola.app.presentation.WelcomeActivity
import com.binabola.app.presentation.home.HomeFragment
import com.binabola.app.presentation.register.RegisterActivity


class SettingFragment : Fragment() {
    private var _binding: FragmentSettingBinding? = null
    private val binding get() = _binding!!
    private val viewModel by activityViewModels<MainViewModel> {
        ViewModelFactory.getInstance(requireActivity())
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.profile.observe(viewLifecycleOwner) {
            binding.tvUserName.text = it.name
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSettingBinding.inflate(inflater, container, false)
        val view = binding.root

        initActions()
        return view
    }

    private fun initActions() {
        binding.apply {
            btnChangeLanguange.setOnClickListener {
                startActivity(Intent(Settings.ACTION_LOCALE_SETTINGS))
            }

            btnlogout.setOnClickListener {
                showYesNoDialog(
                    title = getString(R.string.action_logout),
                    message = getString(R.string.message_logout),
                    onYes = {
                        viewModel.logout()
                        val intent = Intent(requireContext(), WelcomeActivity::class.java)
                        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                        startActivity(intent)
                        requireActivity().finish()
                    }
                )
            }
        }
    }

    private fun showYesNoDialog(title: String, message: String, onYes: ()->Unit) {
        AlertDialog.Builder(requireContext()).apply {
            setTitle(title)
            setMessage(message)
            setNegativeButton(getString(R.string.label_no)) { p0, _ ->
                p0.dismiss()
            }
            setPositiveButton(getString(R.string.label_yes)) { _, _ ->
                onYes()
            }
        }.create().show()

    }
}