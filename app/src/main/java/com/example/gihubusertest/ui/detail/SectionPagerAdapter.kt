package com.example.gihubusertest.ui.detail

import android.content.Context
import android.os.Bundle
import androidx.annotation.StringRes
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.example.gihubusertest.R

class SectionPagerAdapter(private val context: Context, fm: FragmentManager, data: Bundle) :
    FragmentPagerAdapter(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {

    private val fragmentBundle: Bundle = data

    @StringRes
    private val tabTitles = arrayOf(R.string.tab_1, R.string.tab_2)

    override fun getCount(): Int = tabTitles.size

    override fun getItem(position: Int): Fragment {
        return when (position) {
            0 -> FollowingFragment().apply { arguments = fragmentBundle }
            1 -> FollowersFragment().apply { arguments = fragmentBundle }
            else -> throw IllegalStateException("Invalid position: $position")
        }
    }

    override fun getPageTitle(position: Int): CharSequence {
        return context.resources.getString(tabTitles[position])
    }
}
