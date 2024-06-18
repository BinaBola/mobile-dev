package com.binabola.app.presentation

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.fragment.app.Fragment
import com.binabola.app.R
import com.binabola.app.data.Result
import com.binabola.app.databinding.ActivityMainBinding
import com.binabola.app.presentation.food.FoodFragment
import com.binabola.app.presentation.home.HomeFragment
import com.binabola.app.presentation.profile.ProfileFragment
import com.binabola.app.presentation.setting.SettingFragment
import java.util.Calendar

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private val viewModel by viewModels<MainViewModel> {
        ViewModelFactory.getInstance(this)
    }

    private val homeFragment = HomeFragment()
    private val foodFragment = FoodFragment()
    private val profileFragment = SettingFragment()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel.getSession().observe(this) {
            if(!it.isLogin) {
                val intent = Intent(this, WelcomeActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                startActivity(intent)
                finish()
            } else {
                viewModel.getProfile(it.userId.toString()).observe(this) {profile ->
                    if(profile != null && profile is Result.Success) {
                        println("GET PROFILE: $profile")
                        viewModel.setProfile(profile.data)

                        val calendar = Calendar.getInstance()
                        val selectedDate = calendar.get(Calendar.DATE).toString()
                        var selectedMonth = (calendar.get(Calendar.MONTH) + 1).toString()
                        val selectedYear = calendar.get(Calendar.YEAR).toString()

                        if(selectedMonth.length == 1) {
                            selectedMonth = "0$selectedMonth"
                        }

                        val dateString  = "$selectedYear-$selectedMonth-$selectedDate"
                        viewModel.setDate(dateString)

                        viewModel.getDailyCalories(profile.data.id.toString(),dateString).observe(this) {food ->
                            when(food) {
                                is Result.Loading -> {

                                }
                                is Result.Success -> {
                                    println("GET DAILY CALORIES: ${food.data}")
                                    viewModel.setListFood(food.data)
                                }
                                is Result.Error -> {
                                    AppUtil().showToast(this@MainActivity, food.error)
                                }
                            }
                        }
                    }
                }
                viewModel.getExercises()
            }
        }

        setCurrentFragment(homeFragment)

        binding.bottomNavigationView.setOnItemSelectedListener {
            when(it.itemId) {
                R.id.home -> setCurrentFragment(homeFragment)
                R.id.food -> setCurrentFragment(foodFragment)
                R.id.profile -> setCurrentFragment(profileFragment)
            }
            true
        }

    }

    private fun setCurrentFragment(fragment: Fragment)=
        supportFragmentManager.beginTransaction().apply {
            replace(R.id.flFragment,fragment)
            commit()
        }
}