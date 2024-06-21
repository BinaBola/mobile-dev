package com.binabola.app.presentation.foodscan

import android.content.Intent
import android.icu.util.Calendar
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.binabola.app.R
import com.binabola.app.data.Result
import com.binabola.app.databinding.ActivityFoodScanBinding
import com.binabola.app.presentation.AppUtil
import com.binabola.app.presentation.ViewModelFactory
import com.binabola.app.presentation.predictfood.PredictFoodActivity
import com.binabola.app.presentation.predictfood.PredictFoodViewModel
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class FoodScanActivity : AppCompatActivity() {
    private lateinit var binding: ActivityFoodScanBinding
    private val viewModel by viewModels<PredictFoodViewModel> {
        ViewModelFactory.getInstance(this)
    }

    private val resultLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if(result.resultCode == RESULT_CODE_SCAN && result.data != null) {
            val foodValue = result.data?.getStringExtra(EXTRA_FOOD_VALUE)
            calorie = result.data?.getStringExtra(EXTRA_CALORIE_VALUE)
            val foodImgUri = result.data?.getStringExtra(EXTRA_IMG_URI)

            if(foodImgUri != null) {
                binding.img.setImageURI(Uri.parse(foodImgUri))
                binding.img.visibility = View.VISIBLE
                binding.imgHolder.visibility = View.GONE
            }

            binding.edtfoodname.setText(foodValue.toString())
            binding.tvCalorie.text = "Kalori: $calorie kcal"
        }
    }
    private var category: String? = null
    private var calorie: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFoodScanBinding.inflate(layoutInflater)
        setContentView(binding.root)

        lifecycleScope.launch {
            viewModel.submitResult.collect{ result ->
                when(result){
                    is Result.Loading -> {}
                    is Result.Error -> {
                        AppUtil().showToast(this@FoodScanActivity,result.error)
                    }
                    is Result.Success -> {
                        AppUtil().showToast(this@FoodScanActivity,result.data.message.toString())
                        finish()
                    }
                }
            }
        }

        initView()
    }

    private fun initView() {
        binding.imgHolder.setOnClickListener {
            val intent = Intent(this@FoodScanActivity, PredictFoodActivity::class.java)
            resultLauncher.launch(intent)
        }
        binding.img.setOnClickListener {
            val intent = Intent(this@FoodScanActivity, PredictFoodActivity::class.java)
            resultLauncher.launch(intent)
        }

        binding.toolbar.setNavigationOnClickListener{
            finish()
        }

        binding.btnaddfoodmanual.setOnClickListener {
            submitData()
        }

        val categories = resources.getStringArray(R.array.food_categories)
        val cAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, categories)
        binding.dropdownCategory.adapter = cAdapter
        binding.dropdownCategory.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                category = categories[position]
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {

            }

        }
    }

    private fun submitData() {
        val calendar = Calendar.getInstance()
        val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        val date = Date(calendar.timeInMillis)
        val dateString = dateFormat.format(date)

        val foods = binding.edtfoodname.text.toString()
        val amount= binding.edtfoodjumlah.text.toString()

        if(foods.isEmpty()) {
            AppUtil().showToast(this@FoodScanActivity,"Makanan tidak boleh kosong")
            return
        }
        if(amount.isEmpty()) {
            AppUtil().showToast(this@FoodScanActivity,"Jumlah tidak boleh kosong")
            return
        }

        if(category.isNullOrEmpty()) {
            AppUtil().showToast(this@FoodScanActivity,"Silakan pilih kategori")
            return
        }

        lifecycleScope.launch {

            viewModel.submitFood(
                dateString,foods,calorie.toString(),amount, category.toString().lowercase(Locale.ROOT)
            )
        }
    }

    companion object {
        const val RESULT_CODE_SCAN = 110
        const val EXTRA_FOOD_VALUE = "EXTRA_FOOD_VALUE"
        const val EXTRA_CALORIE_VALUE = "EXTRA_CALORIE_VALUE"
        const val EXTRA_IMG_URI = "EXTRA_IMG_URI"
    }
}