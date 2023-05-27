package com.blood.db.entity

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity(tableName = "glucose")
data class GlucoseEntity(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    var id: Long?,

    @ColumnInfo(name = "unit")
    var unit: String?,

    @ColumnInfo(name = "profileId")
    val profileId: Long,
) : Parcelable