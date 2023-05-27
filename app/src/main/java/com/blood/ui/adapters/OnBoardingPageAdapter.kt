package com.blood.ui.adapters

import android.content.Context
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import com.blood.ui.fragments.onboarding.OnBoardingPageFragment
import com.bloodpressure.pressuremonitor.bloodpressuretracker.R

class OnBoardingPageAdapter(val context: Context, fm: FragmentManager) :
    FragmentStatePagerAdapter(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {

    companion object {
        val PAGE_INDEX_1 = 0
        val PAGE_INDEX_2 = 1
        val PAGE_INDEX_3 = 2

        val NUMBER_PAGE = 3
    }

    override fun getItem(position: Int): Fragment {
        return when (position) {
            PAGE_INDEX_1 -> OnBoardingPageFragment.newInstance(
                R.drawable.ic_intro_1,
                context.getString(R.string.intro_title_1),
                context.getString(R.string.intro_content_1)
            )

            PAGE_INDEX_2 -> OnBoardingPageFragment.newInstance(
                R.drawable.ic_intro_2,
                context.getString(R.string.intro_title_2),
                context.getString(R.string.intro_content_2)
            )

            else -> OnBoardingPageFragment.newInstance(
                R.drawable.ic_intro_3,
                context.getString(R.string.intro_title_3),
                context.getString(R.string.intro_content_3)
            )
        }
    }

    override fun getCount(): Int {
        return NUMBER_PAGE
    }
}