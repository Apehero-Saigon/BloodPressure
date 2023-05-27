package com.blood.ui.fragments.profile

import com.bloodpressure.pressuremonitor.bloodpressuretracker.R
import com.bloodpressure.pressuremonitor.bloodpressuretracker.databinding.FragmentProfileEditBinding
import com.blood.base.BaseFragment
import com.blood.base.BaseViewModel

class ProfileDetailFragment : BaseFragment<BaseViewModel, FragmentProfileEditBinding>(
    R.layout.fragment_profile_edit, BaseViewModel::class.java
) {

}