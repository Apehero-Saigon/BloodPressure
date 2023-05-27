package com.blood.network.api

import com.blood.data.Profile
import com.blood.network.ApiService
import com.blood.network.api.interfaceapi.IProfileAPI
import javax.inject.Inject

class ProfileAPI @Inject constructor(private val api: ApiService) : IProfileAPI {
    override suspend fun getAllProfile(): List<Profile> {
        return api.getAllProfile()
    }
}