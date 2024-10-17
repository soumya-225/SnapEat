package com.sks225.snapeat

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.sks225.snapeat.adapter.AdapterViewPager
import com.sks225.snapeat.databinding.ActivityMainBinding
import com.sks225.snapeat.databinding.FragmentMainBinding


class MainFragment : Fragment() {
    private lateinit var binding: FragmentMainBinding
    private lateinit var bottomNav: BottomNavigationView
    private lateinit var pagerMain: ViewPager2
    private var fragmentArrList: ArrayList<Fragment> = ArrayList()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentMainBinding.inflate(layoutInflater,container,false)
        bottomNav = binding.bottomNavigationView
        pagerMain = binding.pagerMain

        fragmentArrList.add(HomeFragment())
        fragmentArrList.add(ReportFragment())
        fragmentArrList.add(ProfileFragment())

        val adapterViewPager = AdapterViewPager(requireActivity(), fragmentArrList)
        pagerMain.adapter = adapterViewPager


        pagerMain.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                when (position) {
                    0 -> bottomNav.selectedItemId = R.id.home
                    1 -> bottomNav.selectedItemId = R.id.report
                    2 -> bottomNav.selectedItemId = R.id.profile
                    else -> {}
                }
                super.onPageSelected(position)
            }
        })

        bottomNav.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.home -> pagerMain.currentItem = 0
                R.id.report -> pagerMain.currentItem = 1
                R.id.profile -> pagerMain.currentItem = 2
                else -> {}
            }
            true
        }
        return binding.root
    }
}