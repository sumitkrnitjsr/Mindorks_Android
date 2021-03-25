package com.mindorks.notesapp.onboarding

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.mindorks.notesapp.R


class OnBoardingOneFragment : Fragment() {

    private lateinit var onNextClick: OnNextClick
    private lateinit var buttonNext: Button

    override fun onAttach(context: Context) {
        super.onAttach(context)
        onNextClick = context as OnNextClick
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_on_boarding_one, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        bindViews(view)
        setupClickListener()
    }

    private fun setupClickListener() {
        buttonNext.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View?) {
                onNextClick.onClick()
            }
        })
    }

    private fun bindViews(view: View) {
        buttonNext = view.findViewById<Button>(R.id.buttonNext)
    }

    interface OnNextClick {
        fun onClick()
    }
}
