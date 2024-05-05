package com.example.gihubusertest.ui.main

import android.content.Context
import android.os.Bundle
import androidx.annotation.StringRes
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.example.gihubusertest.R
import com.example.gihubusertest.ui.detail.FollowersFragment
import com.example.gihubusertest.ui.detail.FollowingFragment

class SectionPagerAdapter(private val mCtx: Context, fm: FragmentManager, data: Bundle) :
    FragmentPagerAdapter(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {

    private var fragmentBundle: Bundle

    init {
        fragmentBundle = data
    }

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
        return mCtx.resources.getString(tabTitles.getOrNull(position) ?: R.string.tab_unknown)
    }
}