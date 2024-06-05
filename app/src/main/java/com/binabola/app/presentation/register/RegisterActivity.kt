package com.binabola.app.presentation.register

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.window.OnBackInvokedDispatcher
import androidx.activity.OnBackPressedCallback
import androidx.activity.viewModels
import androidx.annotation.StringRes
import com.binabola.app.R
import com.binabola.app.databinding.ActivityRegisterBinding
import com.binabola.app.presentation.AppUtil
import com.binabola.app.presentation.ViewModelFactory
import com.binabola.app.presentation.adapter.SectionPagerAdapter

class RegisterActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRegisterBinding
    private val viewModel by viewModels<RegisterViewModel> {
        ViewModelFactory.getInstance(this)
    }
    private val onBackPressedCallback = object : OnBackPressedCallback(true) {
        override fun handleOnBackPressed() {
            val currentPage = binding.viewPager.currentItem

            if(currentPage > 0) {
                binding.viewPager.setCurrentItem(currentPage - 1)
                binding.progressBar.progress = currentPage - 1

                binding.tvTitle.setText(PAGE_TITLES[currentPage-1])
                binding.btnNext.visibility = View.VISIBLE
            } else {
                finish()
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val adapter = SectionPagerAdapter(this)
        binding.viewPager.adapter = adapter
        binding.viewPager.isUserInputEnabled = false

        binding.tvTitle.setText(PAGE_TITLES[binding.viewPager.currentItem])

        onBackPressedDispatcher.addCallback(this,onBackPressedCallback)
        binding.btnBack.setOnClickListener {
            val currentPage = binding.viewPager.currentItem

            if(currentPage > 0) {
                binding.viewPager.setCurrentItem(currentPage - 1)
                binding.progressBar.progress = currentPage - 1

                binding.tvTitle.setText(PAGE_TITLES[currentPage-1])
                binding.btnNext.visibility = View.VISIBLE
            } else {
                finish()
            }
        }

        binding.btnNext.setOnClickListener {
            val currentPage = binding.viewPager.currentItem
            println("PAGE:$currentPage")


            if(currentPage == 0 && viewModel.getRole().isEmpty()) {
                AppUtil().showToast(this, "Silakan pilih role")
                return@setOnClickListener
            }
//
//            if(currentPage == 1 && viewModel.getUserData().value == null) {
//                AppUtil().showToast(this, "Silakan lengkapi data")
//                return@setOnClickListener
//            }

            if(currentPage < adapter.itemCount - 1) {
                binding.btnNext.visibility = View.VISIBLE
                binding.viewPager.setCurrentItem(currentPage + 1)
                binding.progressBar.progress = currentPage + 1

                binding.tvTitle.setText(PAGE_TITLES[currentPage+1])
            }

            if(currentPage + 1 == adapter.itemCount-1) {
                binding.btnNext.visibility = View.GONE
            }
        }
    }

    companion object {
        @StringRes
        private val PAGE_TITLES = intArrayOf(
            R.string.regis_text_1,
            R.string.regis_text_2,
            R.string.regis_text_3
        )
    }
}
