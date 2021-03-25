package com.mindorks.notesapp.onboarding.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.mindorks.notesapp.onboarding.OnBoardingOneFragment
import com.mindorks.notesapp.onboarding.OnBoardingTwoFragment

class FragmentAdapter(fm: FragmentActivity) : FragmentStateAdapter(fm) {

    override fun getItemCount(): Int {
        return 2
    }

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> OnBoardingOneFragment()
            else -> OnBoardingTwoFragment()
        }
    }

}