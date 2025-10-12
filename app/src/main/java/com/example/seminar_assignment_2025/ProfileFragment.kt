package com.example.seminar_assignment_2025

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class ProfileFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.activity_profile, container, false)

        val toolbarBackButton = view.findViewById<ImageView>(R.id.toolbar_back_button)
        toolbarBackButton.setOnClickListener {
            requireActivity().onBackPressedDispatcher.onBackPressed()
        }

        val slackUrlTextView = view.findViewById<TextView>(R.id.slack_url_text)
        slackUrlTextView.text = "your-workspace.slack.com"

        val githubIdList = List(10) { "rlacjfals110" }
        val recyclerView = view.findViewById<RecyclerView>(R.id.github_recycler_view)
        recyclerView.adapter = GithubIdAdapter(githubIdList)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        return view
    }
}
