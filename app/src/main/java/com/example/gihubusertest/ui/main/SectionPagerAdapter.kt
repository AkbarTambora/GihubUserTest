package com.example.gihubusertest.ui.main

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter

class SectionPagerAdapter internal constructor(activity: AppCompatActivity) :
    FragmentStateAdapter(activity) {
    override fun createFragment(position: Int): Fragment {
        val fragment = UserFragment()
        val bundle = Bundle()
        if (position == 0) {
            bundle.putString(UserFragment.ARG_TAB, UserFragment.TAB_USER)
        } else {
            bundle.putString(UserFragment.ARG_TAB, UserFragment.TAB_FAVORITE)
        }
        fragment.arguments = bundle
        return fragment
    }

    override fun getItemCount(): Int {
        return 2
    }
}