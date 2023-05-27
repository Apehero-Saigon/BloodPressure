package com.blood.ui.fragments.home

import androidx.lifecycle.MutableLiveData
import com.blood.base.BaseViewModel
import com.blood.data.BloodPressure
import com.blood.data.Profile
import com.blood.data.repository.BloodPressureRepository
import kotlinx.coroutines.delay
import javax.inject.Inject

class HomeViewModel @Inject constructor() : BaseViewModel() {

    @Inject
    lateinit var bloodPressureRepository: BloodPressureRepository

    val insertBloodPressureObserver = MutableLiveData<BloodPressure?>()

    fun insertBloodPressure(bloodPressure: BloodPressure) {
        isLoading.postValue(true)
        launchOnUITryCatch({
            val bloodPressureNew = bloodPressureRepository.insertBloodPressure(bloodPressure)
            delay(500)

            isLoading.postValue(false)
            insertBloodPressureObserver.postValue(bloodPressureNew)
        }, {
            isLoading.postValue(false)
            insertBloodPressureObserver.postValue(null)
        }, {

        }, true)
    }
}