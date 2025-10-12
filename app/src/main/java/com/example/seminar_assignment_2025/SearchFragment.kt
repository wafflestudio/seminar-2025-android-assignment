package com.example.seminar_assignment_2025

import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment

class SearchFragment : Fragment() {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        val textView = TextView(requireContext())
        textView.text = "Search 화면"
        textView.textSize = 24f
        textView.gravity = Gravity.CENTER
        return textView
    }
}
