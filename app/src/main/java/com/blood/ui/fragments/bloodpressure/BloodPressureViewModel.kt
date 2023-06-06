package com.blood.ui.fragments.bloodpressure

import androidx.lifecycle.MutableLiveData
import com.blood.base.BaseViewModel
import com.blood.common.Constant
import com.blood.data.BloodPressure
import com.blood.data.repository.BloodPressureRepository
import com.blood.utils.FirebaseUtils
import com.blood.utils.listener.SingleLiveEvent
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext
import java.util.Calendar
import javax.inject.Inject

class BloodPressureViewModel @Inject constructor() : BaseViewModel() {

    @Inject
    lateinit var bloodPressureRepository: BloodPressureRepository

    val updateBloodPressureObserver = SingleLiveEvent<BloodPressure?>()
    val bloodPressureObserver = MutableLiveData<BloodPressure>().apply {
        value = BloodPressure(
            systole = 150, diastole = 70, pulse = 88, createAt = Calendar.getInstance().time
        )
    }
    val listBloodPressureObserver =
        MutableLiveData<List<BloodPressure>>().apply { value = mutableListOf() }
    val deleteBloodObserver = SingleLiveEvent<Boolean>()

    fun getTopBloodPressureByID(top: Int = 0) {
        launchOnUITryCatch({
            val listData = withContext(Dispatchers.IO) {
                bloodPressureRepository.getTopBloodPressureByID(Constant.PROFILE_ID_DEFAULT, top)
            }
            listBloodPressureObserver.postValue(listData)
        }, {
            listBloodPressureObserver.postValue(mutableListOf())
        })
    }

    fun getBloodPressureByID(id: Long) {
        launchOnUITryCatch({
            val bloodPressure = withContext(Dispatchers.IO) {
                bloodPressureRepository.getDetailById(id)
            }
            bloodPressureObserver.postValue(bloodPressure)
        }, {
            it.printStackTrace()
        })
    }

    fun updateBloodPressure(bloodPressure: BloodPressure) {
        launchOnUITryCatchWithLoading({
            val bloodPressureNew = if (bloodPressure.id == 0L) {
                FirebaseUtils.eventClickAddBloodHomeScreen()
                withContext(Dispatchers.IO) {
                    bloodPressureRepository.insertBloodPressure(bloodPressure)
                }
            } else {
                withContext(Dispatchers.IO) {
                    bloodPressureRepository.updateBloodPressure(bloodPressure)
                }
            }
            delay(500)

            updateBloodPressureObserver.postValue(bloodPressureNew)
        }, {
            updateBloodPressureObserver.postValue(null)
            it.printStackTrace()
        }, {

        }, true)
    }

    fun deleteBloodById(id: Long) {
        launchOnUITryCatchWithLoading({
            bloodPressureRepository.deleteBloodById(id)
            delay(300)

            deleteBloodObserver.postValue(true)
        }, {
            deleteBloodObserver.postValue(false)
        })
    }
}