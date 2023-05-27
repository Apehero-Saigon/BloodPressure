package com.blood.ui.fragments.onboarding

import android.os.Bundle
import com.bloodpressure.pressuremonitor.bloodpressuretracker.R
import com.bloodpressure.pressuremonitor.bloodpressuretracker.databinding.FragmentOnboardingPageBinding
import com.blood.base.BaseFragment
import com.blood.base.BaseViewModel

class OnBoardingPageFragment : BaseFragment<BaseViewModel, FragmentOnboardingPageBinding>(
    R.layout.fragment_onboarding_page, BaseViewModel::class.java
) {
    companion object {
        private const val KEY_PHOTO = "KEY_PHOTO"
        private const val KEY_TITLE = "KEY_TITLE"
        private const val KEY_CONTENT = "KEY_CONTENT"

        fun newInstance(photo: Int, title: String, content: String): OnBoardingPageFragment {
            return OnBoardingPageFragment().apply {
                arguments = Bundle().apply {
                    putInt(KEY_PHOTO, photo)
                    putString(KEY_TITLE, title)
                    putString(KEY_CONTENT, content)
                }
            }
        }
    }

    override fun initView() {
        arguments?.let { args ->
            binding.run {
                ivPhoto.setImageResource(args.getInt(KEY_PHOTO))
                tvTitle.text = args.getString(KEY_TITLE)
                tvContent.text = args.getString(KEY_CONTENT)
            }
        }
    }
}