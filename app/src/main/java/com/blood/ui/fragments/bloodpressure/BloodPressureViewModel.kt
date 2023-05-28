package com.blood.ui.fragments.bloodpressure

import androidx.lifecycle.MutableLiveData
import com.blood.base.BaseViewModel
import com.blood.data.BloodPressure
import com.blood.data.repository.BloodPressureRepository
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
            systole = 150,
            diastole = 70,
            pulse = 88,
            createAt = Calendar.getInstance().time
        )
    }
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
}