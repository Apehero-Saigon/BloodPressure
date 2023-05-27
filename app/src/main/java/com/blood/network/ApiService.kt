package com.blood.network

import com.blood.data.Profile
import retrofit2.http.GET

interface ApiService {

    @GET("profile")
    suspend fun getAllProfile(): List<Profile>

}