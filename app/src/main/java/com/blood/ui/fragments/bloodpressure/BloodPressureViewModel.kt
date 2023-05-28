package com.blood.ui.fragments.bloodpressure

import androidx.lifecycle.MutableLiveData
import com.blood.base.BaseViewModel
import com.blood.data.BloodPressure
import com.blood.data.repository.BloodPressureRepository
import com.blood.utils.listener.SingleLiveEvent
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext
import javax.inject.Inject

class BloodPressureViewModel @Inject constructor() : BaseViewModel() {

    @Inject
    lateinit var bloodPressureRepository: BloodPressureRepository

    val insertBloodPressureObserver = SingleLiveEvent<BloodPressure?>()
    val bloodPressureObserver = MutableLiveData<BloodPressure>().apply { value = BloodPressure() }
    val listBloodPressureObserver =
        MutableLiveData<List<BloodPressure>>().apply { value = mutableListOf() }

    fun getListBloodPressure(top: Int = 0) {
        launchOnUITryCatch({
            val listData = withContext(Dispatchers.IO) {
                bloodPressureRepository.getListBloodPressureByID(prefUtils.profile!!.id, top)
            }
            listBloodPressureObserver.postValue(listData)
        }, {
            listBloodPressureObserver.postValue(mutableListOf())
        }, {

        }, true)
    }

    fun getBloodPressureByID(id: Long) {
        launchOnUITryCatch({
            val bloodPressure = withContext(Dispatchers.IO) {
                bloodPressureRepository.getDetailById(id)
            }
            bloodPressureObserver.postValue(bloodPressure)
        }, {
            it.printStackTrace()
        }, {

        }, true)
    }

    fun insertBloodPressure(bloodPressure: BloodPressure) {
        launchOnUITryCatchWithLoading({
            val bloodPressureNew = withContext(Dispatchers.IO) {
                bloodPressureRepository.insertBloodPressure(bloodPressure)
            }
            delay(500)

            insertBloodPressureObserver.postValue(bloodPressureNew)
        }, {
            insertBloodPressureObserver.postValue(null)
            it.printStackTrace()
        }, {

        }, true)
    }
}