package com.mindorks.notesapp.onboarding

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager2.widget.ViewPager2
import com.mindorks.notesapp.R
import com.mindorks.notesapp.login.LoginActivity
import com.mindorks.notesapp.data.local.pref.PrefConstant
import com.mindorks.notesapp.data.local.pref.StoreSession
import com.mindorks.notesapp.onboarding.adapter.FragmentAdapter

class OnBoardingActivity : AppCompatActivity(), OnBoardingOneFragment.OnNextClick, OnBoardingTwoFragment.OnOptionClick {

    companion object {
        private const val FIRST_ITEM = 0
        private const val LAST_ITEM = 1
    }

    private lateinit var viewPager: ViewPager2


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_on_boarding)
        bindView()
        setupSharedPreference()
    }

    private fun setupSharedPreference() {
        StoreSession.init(this)
    }

    private fun bindView() {
        viewPager = findViewById(R.id.viewPager)
        val pageAdapter = FragmentAdapter(this)
        viewPager.adapter = pageAdapter
    }

    override fun onClick() {
        viewPager.currentItem = LAST_ITEM
    }

    override fun onOptionBack() {
        viewPager.currentItem = FIRST_ITEM
    }

    override fun onOptionDone() {
        StoreSession.write(PrefConstant.ON_BOARDED_SUCCESSFULLY, true)
        navigateToLogin()
    }

    private fun navigateToLogin() {
        val intent = Intent(this@OnBoardingActivity, LoginActivity::class.java)
        startActivity(intent)
    }
}
