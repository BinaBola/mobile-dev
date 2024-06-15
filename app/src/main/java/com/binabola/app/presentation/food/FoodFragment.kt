package com.binabola.app.presentation.food

import android.content.Intent
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.GridLayoutManager
import com.binabola.app.R
import com.binabola.app.databinding.FragmentFoodBinding
import com.binabola.app.databinding.FragmentHomeBinding
import com.binabola.app.presentation.adapter.CalendarAdapter
import com.binabola.app.presentation.foodscan.FoodScanFragment
import com.binabola.app.presentation.register.RegisterActivity
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class FoodFragment : Fragment() {
    private var _binding: FragmentFoodBinding? = null
    private val binding get() = _binding!!
    private lateinit var calAdapter: CalendarAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFoodBinding.inflate(inflater, container, false)
        val view = binding.root

        initView()
        return view
    }

    private fun initView() {
        val listDate = generateDates()

        calAdapter = CalendarAdapter { calendar ->
            println("SELECTED DATE: ${calendar.get(Calendar.DATE)}")
        }

        calAdapter.submitList(listDate)

        binding.rvCalendar.apply {
            layoutManager = GridLayoutManager(requireActivity(), 7, GridLayoutManager.VERTICAL, false)
            setHasFixedSize(true)
            this.adapter = calAdapter
        }

        binding.btnNextWeek.setOnClickListener {
            onNextButtonClick()
        }

        binding.btnPrevWeek.setOnClickListener {
            onPrevButtonClick()
        }

        binding.btnaddfood.setOnClickListener {
            val intent = Intent(requireContext(), FoodScanFragment::class.java)
            startActivity(intent)
        }

        updateCurrentMonth()
    }

    private fun generateDates(startDate: Calendar = Calendar.getInstance()): List<Calendar> {
        val dates = mutableListOf<Calendar>()

        // Set the calendar to the first day of the current week
//        calendar.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY)

        // Add the next 7 days
        repeat(7) {
            val date = startDate.clone() as Calendar
            dates.add(date)
            startDate.add(Calendar.DAY_OF_MONTH, 1)
        }

        return dates
    }

    private fun onPrevButtonClick() {
        val firstDate = calAdapter.currentList.firstOrNull()
        if (firstDate != null) {
            val newStartDate = firstDate.clone() as Calendar
            newStartDate.add(Calendar.DAY_OF_MONTH, -7)
            calAdapter.submitList(generateDates(newStartDate))
            updateCurrentMonth(newStartDate)
        } else {
            showToast("No dates available")
        }
    }

    private fun onNextButtonClick() {
        val lastDate = calAdapter.currentList.lastOrNull()
        if (lastDate != null) {
            val newStartDate = lastDate.clone() as Calendar
            newStartDate.add(Calendar.DAY_OF_MONTH, 1)
            calAdapter.submitList(generateDates(newStartDate))
            updateCurrentMonth(newStartDate)
        } else {
            showToast("No dates available")
        }
    }

    private fun updateCurrentMonth(date: Calendar = Calendar.getInstance()) {
        val dateFormat = SimpleDateFormat("MMM yyyy", Locale.getDefault())
        val monthString = dateFormat.format(date.time)
        binding.tvMonth.text = monthString
    }

    private fun showToast(message: String) {
        Toast.makeText(requireActivity(), message, Toast.LENGTH_SHORT).show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}