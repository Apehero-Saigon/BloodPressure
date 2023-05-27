package com.blood.ui.activities

import com.bloodpressure.pressuremonitor.bloodpressuretracker.R
import com.bloodpressure.pressuremonitor.bloodpressuretracker.databinding.ActivityMainBinding
import com.blood.base.BaseActivity
import com.blood.base.BaseViewModel

class MainActivity : BaseActivity<BaseViewModel, ActivityMainBinding>(
    R.layout.activity_main, BaseViewModel::class.java
) {

}