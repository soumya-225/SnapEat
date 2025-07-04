package com.sks225.snapeat

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.viewpager2.widget.ViewPager2
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.sks225.snapeat.adapter.AdapterViewPager
import com.sks225.snapeat.databinding.FragmentMainBinding
import com.sks225.snapeat.viewModel.MainFragmentViewModel

class MainFragment : Fragment() {

    private lateinit var binding: FragmentMainBinding
    private lateinit var viewModel: MainFragmentViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (Firebase.auth.currentUser == null) {
            findNavController().navigate(R.id.action_mainFragment_to_onboardingFragment2)
        } else {
            viewModel = ViewModelProvider(requireActivity())[MainFragmentViewModel::class.java]
            viewModel.fetchUser()
            viewModel.fetchBmiData()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentMainBinding.inflate(inflater, container, false)
        setupViewPagerAndBottomNav()
        return binding.root
    }

    private fun setupViewPagerAndBottomNav() {
        val fragments = arrayListOf(
            HomeFragment(),
            ReportFragment(),
            ProfileFragment()
        )

        binding.pagerMain.adapter = AdapterViewPager(requireActivity(),
            fragments
        )

        binding.pagerMain.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                binding.bottomNavigationView.selectedItemId = when (position) {
                    0 -> R.id.home
                    1 -> R.id.report
                    2 -> R.id.profile
                    else -> R.id.home
                }
            }
        })

        binding.bottomNavigationView.setOnApplyWindowInsetsListener { view, insets ->
            view.setPadding(0, 0, 0, 0)
            insets
        }

        binding.bottomNavigationView.setOnItemSelectedListener { item ->
            binding.pagerMain.currentItem = when (item.itemId) {
                R.id.home -> 0
                R.id.report -> 1
                R.id.profile -> 2
                else -> 0
            }
            true
        }
    }
}
