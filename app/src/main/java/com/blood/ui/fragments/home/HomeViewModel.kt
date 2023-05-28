package com.blood.ui.fragments.home

import com.blood.base.BaseViewModel
import com.blood.data.repository.BloodPressureRepository
import javax.inject.Inject

class HomeViewModel @Inject constructor() : BaseViewModel() {

    @Inject
    lateinit var bloodPressureRepository: BloodPressureRepository
}