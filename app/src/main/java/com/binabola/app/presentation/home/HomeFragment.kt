package com.binabola.app.presentation.home

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.binabola.app.R
import com.binabola.app.data.Result
import com.binabola.app.data.remote.response.GetExerciseItem
import com.binabola.app.databinding.FragmentHomeBinding
import com.binabola.app.presentation.AppUtil
import com.binabola.app.presentation.MainViewModel
import com.binabola.app.presentation.ViewModelFactory
import com.binabola.app.presentation.adapter.CalendarAdapter
import com.binabola.app.presentation.adapter.ExerciseAdapter
import com.binabola.app.presentation.exercise.DetailExerciseActivity
import com.binabola.app.presentation.setting.SettingFragment
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class HomeFragment : Fragment() {
    private var _binding: FragmentHomeBinding? = null
    private val viewModel by activityViewModels<MainViewModel> {
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

        initCalendar()
        initCalorie()
        initExercise()
        return view
    }

    private fun openSetting() {
        val fragmentManager = requireActivity().supportFragmentManager
        val transaction = fragmentManager.beginTransaction()
        val settingFragment = SettingFragment()
        transaction.replace(R.id.flFragment, settingFragment)
//        transaction.addToBackStack(null)
        transaction.commit()
    }

    private fun initCalendar() {
        val listDate = generateDates()

        calAdapter = CalendarAdapter { calendar ->
            println("SELECTED DATE: ${calendar.get(Calendar.DATE)}")

            val selectedDate = calendar.get(Calendar.DATE).toString()
            var selectedMonth = (calendar.get(Calendar.MONTH) + 1).toString()
            val selectedYear = calendar.get(Calendar.YEAR).toString()

            if(selectedMonth.length == 1) {
                selectedMonth = "0$selectedMonth"
            }

            val dateString  = "$selectedYear-$selectedMonth-$selectedDate"
            viewModel.setDate(dateString)
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

    private fun initCalorie() {
        viewModel.profile.observe(viewLifecycleOwner) {user ->
            if(user != null) {
                println(user.toString())
                binding.tvTargetCalories.text = user.calorie
                binding.progressBar.max = user.calorie!!.toDouble().toInt()

                viewModel.getTotalCalories().observe(viewLifecycleOwner) {
                    println("TOTAL CALORIES: ${user.calorie}")
                    binding.tvCurrentCalories.text = "$it kcal"

                    val percentage = it.toInt()
                    println("PROGRESS = $percentage")
                    binding.progressBar.progress = percentage
                }

                viewModel.currentDate.observe(viewLifecycleOwner) {
                    viewModel.getDailyCalories(user.id.toString(), it)
                }
            }
        }
    }

    private fun initExercise() {
        val exAdapter = ExerciseAdapter{
            val id = it.id
            println("ID: $id")
//            viewModel.getDetailExercise(id.toString())
            val intent = Intent(requireContext(), DetailExerciseActivity::class.java)
            intent.putExtra(DetailExerciseActivity.EXTRA_ID, id.toString())
            startActivity(intent)
        }

        viewModel.getExercises().observe(viewLifecycleOwner) {
            when (it) {
                is Result.Loading -> {

                }
                is Result.Error -> {
                    AppUtil().showToast(requireActivity(), it.error)
                }
                is Result.Success -> {
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