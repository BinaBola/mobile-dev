package com.binabola.app.presentation.setting

import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.navigation.findNavController
import com.binabola.app.R
import com.binabola.app.databinding.FragmentHomeBinding
import com.binabola.app.databinding.FragmentSettingBinding
import com.binabola.app.presentation.adapter.CalendarAdapter
import com.binabola.app.utils.PreferenceManager


class SettingFragment : Fragment() {
    private var _binding: FragmentSettingBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSettingBinding.inflate(inflater, container, false)
        val view = binding.root

        initUI()
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
                        PreferenceManager.clearAllPreferences()
                    }
                )
            }
        }
    }

    private fun showYesNoDialog(title: String, message: String, onYes: Any) {
        AlertDialog.Builder(requireContext()).apply {
            setTitle(title)
            setMessage(message)
            setNegativeButton(getString(R.string.label_no)) { p0, _ ->
                p0.dismiss()
            }
            setPositiveButton(getString(R.string.label_yes)) { _, _ ->
                onYes.invoke()
            }
        }.create().show()

    }

    fun initUI() {
        binding.toolBar.apply {
            title = getString(R.string.settings)
            setNavigationOnClickListener {
                it.findNavController().popBackStack()
            }
        }
    }
}