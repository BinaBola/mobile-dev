package com.binabola.app.presentation.adapter

import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.binabola.app.presentation.register.AccountFragment
import com.binabola.app.presentation.register.DataFragment
import com.binabola.app.presentation.register.RoleFragment

class SectionPagerAdapter(activity: AppCompatActivity): FragmentStateAdapter(activity) {
    override fun getItemCount(): Int {
        return 3
    }

    override fun createFragment(position: Int): Fragment {

        return when(position) {
            0 -> RoleFragment()
            1 -> DataFragment()
            else -> AccountFragment()
        }
    }
}