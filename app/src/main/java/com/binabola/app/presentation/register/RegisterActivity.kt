package com.binabola.app.presentation.register

import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.activity.OnBackPressedCallback
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatActivity
import com.binabola.app.R
import com.binabola.app.databinding.ActivityRegisterBinding
import com.binabola.app.presentation.AppUtil
import com.binabola.app.presentation.adapter.SectionPagerAdapter

class RegisterActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRegisterBinding
    private val onBackPressedCallback = object : OnBackPressedCallback(true) {
        override fun handleOnBackPressed() {

            if(currentPage > 0) {
                currentPage -= 1
                binding.progressBar.progress = currentPage+1
                binding.viewPager.setCurrentItem(currentPage)

                binding.tvTitle.setText(PAGE_TITLES[currentPage])
                binding.btnNext.visibility = View.VISIBLE
            } else {
                finish()
            }
        }
    }
    private var currentPage = 0

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
            if(currentPage > 0) {
                currentPage -= 1
                binding.progressBar.progress = currentPage+1
                binding.viewPager.setCurrentItem(currentPage)

                binding.tvTitle.setText(PAGE_TITLES[currentPage])
                binding.btnNext.visibility = View.VISIBLE
            } else {
                finish()
            }
        }

        binding.btnNext.setOnClickListener {
            if(!isComplete()) {
                return@setOnClickListener
            }

            if(currentPage + 1 < adapter.itemCount) {
                currentPage += 1
                binding.progressBar.progress = currentPage+1

                binding.btnNext.visibility = View.VISIBLE
                binding.viewPager.setCurrentItem(currentPage)

                binding.tvTitle.setText(PAGE_TITLES[currentPage])
            }

            if(currentPage + 1 == adapter.itemCount) {
                binding.btnNext.visibility = View.GONE
            }
        }
    }

    private fun isComplete(): Boolean {
        val prefs = getSharedPreferences("REGISTER", Context.MODE_PRIVATE)
        val role = prefs.getString("role",null)
        val data = prefs.getString("data",null)
        val account = prefs.getString("account",null)

        if(currentPage == 0 && role == null) {
            AppUtil().showToast(this, "Silakan pilih role")
            return false
        }

        if (currentPage == 1 && data == null) {
            AppUtil().showToast(this, "Silakan lengkapi form")
            return false
        }

        if (currentPage == 2 && account == null) {
            AppUtil().showToast(this, "Silakan lengkapi data akun")
            return false
        }

        return true
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
