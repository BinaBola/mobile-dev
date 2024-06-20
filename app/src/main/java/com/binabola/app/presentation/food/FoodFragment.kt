package com.binabola.app.presentation.food


import android.app.Activity.RESULT_OK
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.binabola.app.R
import com.binabola.app.databinding.FragmentFoodBinding
import com.binabola.app.presentation.adapter.CalendarAdapter
import com.binabola.app.presentation.register.RegisterActivity
import com.binabola.app.presentation.MainViewModel
import com.binabola.app.presentation.ViewModelFactory
import com.binabola.app.presentation.adapter.FoodAdapter
import com.binabola.app.presentation.foodscan.FoodScanActivity
import com.binabola.app.presentation.foodscan.FoodScanActivity.Companion.EXTRA_FOOD_VALUE
import com.binabola.app.presentation.foodscan.FoodScanActivity.Companion.RESULT_CODE_SCAN
import com.binabola.app.presentation.setting.SettingFragment
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class FoodFragment : Fragment() {
    private var _binding: FragmentFoodBinding? = null
    private val binding get() = _binding!!
    private lateinit var calAdapter: CalendarAdapter
    private val viewModel by activityViewModels<MainViewModel> {
        ViewModelFactory.getInstance(requireActivity())
    }
    private val resultLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if(result.resultCode == RESULT_OK) {
            lifecycleScope.launch {
                viewModel.currentDate.observe(viewLifecycleOwner) { date ->
                    viewModel.getSession().observe(viewLifecycleOwner) {user->
                        val uid = user.userId
                        viewModel.getDailyCalories(uid.toString(),date)
                    }
                }
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.listFood.observe(viewLifecycleOwner) {
            println("GET LIST FOOD: $it")
            if(it.isNotEmpty()) {
                binding.rvfood.visibility = View.VISIBLE
                binding.nodata.visibility = View.GONE
                val fAdapter  = FoodAdapter()
                val data = it

                println("DATA: $data")
                fAdapter.submitList(data)
                println("ITEM COUNT: ${fAdapter.itemCount}")

                binding.rvfood.apply {
                    layoutManager = LinearLayoutManager(requireContext())
                    setHasFixedSize(true)
                    adapter = fAdapter
                }
            } else {
                binding.rvfood.visibility = View.GONE
                binding.nodata.visibility = View.VISIBLE
            }
        }

        viewModel.getTotalCalories().observe(viewLifecycleOwner) {
            println("TOTAL CALORIES: $it")
            binding.manyfood.text = "Today's Calorie Intake: $it kcal"
        }

        initView()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFoodBinding.inflate(inflater, container, false)
        return binding.root
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
//            navController.navigate(R.id.action_foodFragment_to_foodScanFragment)
            openAddFood()
        }

        updateCurrentMonth()
    }

    private fun openAddFood() {
        val intent = Intent(requireContext(),FoodScanActivity::class.java)
        resultLauncher.launch(intent)
    }

    private fun generateDates(startDate: Calendar = Calendar.getInstance()): List<Calendar> {
        val dates = mutableListOf<Calendar>()

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
}


