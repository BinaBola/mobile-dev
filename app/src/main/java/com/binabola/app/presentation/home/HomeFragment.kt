package com.binabola.app.presentation.home

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.viewModels
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.binabola.app.data.Result
import com.binabola.app.data.remote.response.GetExerciseItem
import com.binabola.app.databinding.FragmentHomeBinding
import com.binabola.app.presentation.AppUtil
import com.binabola.app.presentation.MainViewModel
import com.binabola.app.presentation.ViewModelFactory
import com.binabola.app.presentation.adapter.CalendarAdapter
import com.binabola.app.presentation.adapter.ExerciseAdapter
import com.binabola.app.presentation.exercise.DetailExerciseActivity
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class HomeFragment : Fragment() {
    private var _binding: FragmentHomeBinding? = null
    private val viewModel by viewModels<MainViewModel> {
        ViewModelFactory.getInstance(requireActivity())
    }

    private val binding get() = _binding!!
    private lateinit var calAdapter: CalendarAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val view = binding.root

        initView()
        initExercise()
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

        updateCurrentMonth()
    }

    private fun initExercise() {
        val exAdapter = ExerciseAdapter{
            val id = it.id
            println("ID: $id")
            viewModel.getDetailExercise(id.toString())
            val intent = Intent(requireContext(), DetailExerciseActivity::class.java)
            intent.putExtra(DetailExerciseActivity.EXTRA_ID, id.toString())
            startActivity(intent)
        }

        viewModel.getExercises().observe(viewLifecycleOwner) {
            when (it) {
                is Result.Loading -> {
                    binding.progressBar.visibility = View.VISIBLE
                }
                is Result.Error -> {
                    binding.progressBar.visibility = View.GONE
                    AppUtil().showToast(requireActivity(), it.error)
                }
                is Result.Success -> {
                    binding.progressBar.visibility = View.GONE
                    val data = it.data
                    println("DATA: $data")
                    exAdapter.submitList(data)
                    println("ITEM COUNT: ${exAdapter.itemCount}")

                    binding.rvExercise.apply {
                        layoutManager = LinearLayoutManager(requireActivity())
                        setHasFixedSize(true)
                        this.adapter = exAdapter

                    }
                }
            }
        }
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
            AppUtil().showToast(requireActivity(),"No dates available")
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
            AppUtil().showToast(requireActivity(),"No dates available")
        }
    }

    private fun updateCurrentMonth(date: Calendar = Calendar.getInstance()) {
        val dateFormat = SimpleDateFormat("MMM yyyy", Locale.getDefault())
        val monthString = dateFormat.format(date.time)
        binding.tvMonth.text = monthString
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}