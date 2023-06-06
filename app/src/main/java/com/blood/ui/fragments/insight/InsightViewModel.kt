package com.blood.ui.fragments.insight

import androidx.lifecycle.MutableLiveData
import com.blood.base.BaseViewModel
import com.blood.common.Constant
import com.blood.common.enumdata.FilterType
import com.blood.data.BloodPressure
import com.blood.data.repository.BloodPressureRepository
import com.blood.utils.DateUtils
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

    fun loadListBlood(filterType: FilterType, withLoading: Boolean = false) {
        if (withLoading) {
            isLoading.postValue(true)
        }
        checkCount()
        launchOnUITryCatch({
            val listBloods = withContext(Dispatchers.IO) {
                if (filterType == FilterType.ALL) {
                    bloodPressureRepository.getTopBloodPressureByID(1)
                } else {
                    val startDate =
                        DateUtils.getDateBefore(if (filterType == FilterType.WEEK) 7 else 3600)
                    bloodPressureRepository.getListBloodPressureByFilterDate(
                        Constant.PROFILE_ID_DEFAULT, startDate, DateUtils.getCurrentDate()
                    )
                }
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
                bloodPressureRepository.countBloodPressureByProfileID(Constant.PROFILE_ID_DEFAULT)
            }
            countBloodObserver.postValue(countBlood)
        }, {
            countBloodObserver.postValue(0)
            it.printStackTrace()
        })
    }
}