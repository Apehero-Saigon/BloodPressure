package com.blood.data

import com.blood.base.BaseData
import com.bloodpressure.pressuremonitor.bloodpressuretracker.R
import java.io.Serializable

data class InfoKnowledge(val id: Int, val photo: Int, val title: Int, val content: Int) :
    Serializable, BaseData {
    companion object {
        fun getListBloodPressure() = mutableListOf(
            InfoKnowledge(
                1, R.drawable.img_post_01, R.string.post_01_title, R.string.post_01_content
            ), InfoKnowledge(
                2, R.drawable.img_post_03, R.string.post_03_title, R.string.post_03_content
            ), InfoKnowledge(
                3, R.drawable.img_post_04, R.string.post_04_title, R.string.post_04_content
            ), InfoKnowledge(
                4, R.drawable.img_post_06, R.string.post_06_title, R.string.post_06_content
            ), InfoKnowledge(
                5, R.drawable.img_post_07, R.string.post_07_title, R.string.post_07_content
            ), InfoKnowledge(
                6, R.drawable.img_post_09, R.string.post_09_title, R.string.post_09_content
            ), InfoKnowledge(
                8, R.drawable.img_post_10, R.string.post_10_title, R.string.post_10_content
            ), InfoKnowledge(
                9, R.drawable.img_post_11, R.string.post_11_title, R.string.post_11_content
            ), InfoKnowledge(
                10, R.drawable.img_post_12, R.string.post_12_title, R.string.post_12_content
            )
        )
    }
}