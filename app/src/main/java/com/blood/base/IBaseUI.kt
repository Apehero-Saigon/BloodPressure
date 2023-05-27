package com.blood.base

interface IBaseUI {

    fun showLoading(content: String)

    fun hideLoading()

    fun isNetworkConnected(): Boolean

    fun hideSystemNavigationBar()

    fun toast(msg : String)
}