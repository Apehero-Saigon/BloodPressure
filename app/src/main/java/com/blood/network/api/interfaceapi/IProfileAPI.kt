package com.blood.network.api.interfaceapi

import com.blood.data.Profile

interface IProfileAPI {

    suspend fun getAllProfile(): List<Profile>
}