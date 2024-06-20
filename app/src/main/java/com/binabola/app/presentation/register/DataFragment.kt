package com.binabola.app.presentation.register

import android.app.DatePickerDialog
import android.content.Context
import android.content.SharedPreferences
import android.icu.util.Calendar
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.binabola.app.R
import com.binabola.app.databinding.FragmentDataBinding
import com.binabola.app.databinding.FragmentHomeBinding
import com.google.gson.Gson

class DataFragment : Fragment() {
    private var _binding: FragmentDataBinding? = null
    private val binding get() = _binding!!
    private lateinit var calendar: Calendar

    private var currentDay: Int? = null
    private var currentMonth: Int? = null
    private var currentYear: Int? = null

    private var dobString = ""
    private var dataMap = mutableMapOf<String, String>()
    private var prefs: SharedPreferences? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDataBinding.inflate(inflater, container, false)
        val view = binding.root

        prefs= requireActivity().getSharedPreferences("REGISTER",Context.MODE_PRIVATE)

        calendar = Calendar.getInstance()
        currentDay = calendar.get(Calendar.DAY_OF_MONTH)
        currentMonth = calendar.get(Calendar.MONTH)
        currentYear = calendar.get(Calendar.YEAR)

        initView()
        return view
    }

    private fun initView() {
        binding.chooseDate.setOnClickListener {
            val dateDialog = DatePickerDialog(
                requireActivity(),
                { _, year, monthOfYear, dayOfMonth ->
                    currentDay = dayOfMonth
                    currentMonth = monthOfYear+1
                    currentYear = year

                    var strMonth = if(currentMonth.toString().length > 1) {
                        currentMonth.toString()
                    } else {
                        "0$currentMonth"
                    }

                    dobString = "$currentYear-$strMonth-$currentDay"
                    binding.tvDate.text = dobString

                    dataMap["dob"] = dobString
                    mapToJsonString()
                },
                currentYear!!,
                currentMonth!!,
                currentDay!!
            )
            dateDialog.datePicker.maxDate = calendar.timeInMillis

            dateDialog.show()
        }

        binding.radioGender.setOnCheckedChangeListener { _, checkedId ->
            when(checkedId) {
                R.id.rb_male -> {
                    dataMap["gender"] = "L"
                }
                R.id.rb_female -> {
                    dataMap["gender"] = "P"
                }
            }

            mapToJsonString()
        }

        binding.etBerat.addTextChangedListener(object : TextWatcher{
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                val berat = s.toString()

                dataMap["berat"] = berat
                mapToJsonString()
            }

            override fun afterTextChanged(s: Editable?) {
            }

        })

        binding.etTinggi.addTextChangedListener(object : TextWatcher{
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                val tinggi = s.toString()

                dataMap["tinggi"] = tinggi
                mapToJsonString()
            }

            override fun afterTextChanged(s: Editable?) {
            }

        })
    }

    private fun mapToJsonString() {
        val gson = Gson()
        val convert = gson.toJson(dataMap)

        prefs!!.edit().putString("data",convert).apply()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}