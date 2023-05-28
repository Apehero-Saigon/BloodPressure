package com.blood.ui.fragments.insight

import androidx.lifecycle.MutableLiveData
import com.blood.base.BaseViewModel
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


    val listBloodObserver = MutableLiveData<List<BloodPressure>>()

    fun loadListBlood() {
        launchOnUITryCatch({
            val listBloods = withContext(Dispatchers.IO) {
                bloodPressureRepository.getListBloodPressureByID(prefUtils.profile!!.id)
            }
            listBloodObserver.postValue(listBloods)
        }, {
            listBloodObserver.postValue(mutableListOf())
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