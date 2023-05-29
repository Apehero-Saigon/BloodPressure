package com.blood.ui.fragments.insight

import androidx.lifecycle.MutableLiveData
import com.blood.base.BaseViewModel
import com.blood.common.enumdata.FilterType
import com.blood.data.BloodPressure
import com.blood.data.repository.BloodPressureRepository
import com.blood.utils.listener.SingleLiveEvent
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext
import javax.inject.Inject

class InsightViewModel @Inject constructor() : BaseViewModel() {

    @Inject
    lateinit var bloodPressureRepository: BloodPressureRepository

    val countBloodObserver = MutableLiveData<Int>().apply { value = 0 }
    val countHeartObserver = MutableLiveData<Int>().apply { value = 0 }
    val countWeightBMIObserver = MutableLiveData<Int>().apply { value = 0 }


    val listBloodObserver = MutableLiveData<List<BloodPressure>>().apply { value = mutableListOf() }
    val listFilterBloodObserver =
        MutableLiveData<List<BloodPressure>>().apply { value = mutableListOf() }

    fun loadListBlood(filterType: FilterType, withLoading: Boolean = false) {
        if (withLoading) {
            isLoading.postValue(true)
        }
        launchOnUITryCatch({
            val listBloods = withContext(Dispatchers.IO) {
                bloodPressureRepository.getListBloodPressureByID(prefUtils.profile!!.id)
            }

            if (withLoading) {
                delay(300)
            }
            listBloodObserver.postValue(listBloods)

            if (withLoading) {
                isLoading.postValue(false)
            }
        }, {
            listBloodObserver.postValue(mutableListOf())

            if (withLoading) {
                isLoading.postValue(false)
            }
        })
    }

    fun checkCount() {
        launchOnUITryCatch({
            val countBlood = withContext(Dispatchers.IO) {
                bloodPressureRepository.countBloodPressureByProfileID(prefUtils.profile!!.id)
            }
            countBloodObserver.postValue(countBlood)
        }, {
            countBloodObserver.postValue(0)
            it.printStackTrace()
        })
    }
}