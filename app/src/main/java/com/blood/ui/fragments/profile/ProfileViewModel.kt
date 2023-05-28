package com.blood.ui.fragments.profile

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.blood.base.BaseViewModel
import com.blood.data.Profile
import com.blood.data.repository.ProfileRepository
import com.blood.utils.AppUtils.isNull
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject


class ProfileViewModel @Inject constructor() : BaseViewModel() {

    @Inject
    lateinit var profileRepository: ProfileRepository

    val insertProfileObserver = MutableLiveData<Profile?>()

    fun insertProfile(profile: Profile) {
        launchOnUITryCatchWithLoading({
            val profileNew = profileRepository.insertProfile(profile)
            delay(500)
            insertProfileObserver.postValue(profileNew)
        }, {
            insertProfileObserver.postValue(null)
        }, {

        }, true)
    }
}