package com.example.socialx.ui.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.example.socialx.R
import com.example.socialx.databinding.FragmentMainBinding
import com.google.firebase.auth.FirebaseAuth
import com.ogaclejapan.smarttablayout.utils.v4.FragmentPagerItemAdapter
import com.ogaclejapan.smarttablayout.utils.v4.FragmentPagerItems

class MainFragment : Fragment() {
    private var _binding: FragmentMainBinding? = null
    private val binding get() = _binding!!
    private val firebaseAuth = FirebaseAuth.getInstance()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        _binding = FragmentMainBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val adapter = FragmentPagerItemAdapter(childFragmentManager,
            FragmentPagerItems.with(activity)
                .add("LOGIN", LoginFragment::class.java)
                .add("SIGNUP", SignupFragment::class.java)
                .create())

        binding.viewpager.adapter = adapter
        binding.viewpagerTab.setViewPager(binding.viewpager)

    }

    override fun onStart() {
        super.onStart()
        if (firebaseAuth.currentUser?.uid?.isNotEmpty() == true) {
            findNavController().navigate(R.id.action_mainFragment_to_newsActivity)
        }
    }

}