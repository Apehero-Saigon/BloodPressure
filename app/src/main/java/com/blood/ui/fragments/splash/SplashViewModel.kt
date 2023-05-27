package com.blood.ui.fragments.splash

import com.blood.base.BaseViewModel
import com.blood.data.repository.GlucoseRepository
import javax.inject.Inject

class SplashViewModel @Inject constructor() : BaseViewModel() {

    @Inject
    lateinit var glucoseRepository: GlucoseRepository
}