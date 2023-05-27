package com.blood.db.entity

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.blood.data.Profile
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity(tableName = "table_profile")
class ProfileEntity(
    @PrimaryKey(autoGenerate = true) var id: Long = 0,
    @ColumnInfo(name = "birthYear") val birthYear: Int?,
    @ColumnInfo(name = "gender") val gender: Int?,
    @ColumnInfo(name = "height") val height: Int?,
    @ColumnInfo(name = "weight") val weight: Int?,
) : Parcelable {
    companion object {
        fun fromProfile(profile: Profile): ProfileEntity {
            return ProfileEntity(
                profile.id,
                profile.birthYear,
                profile.gender,
                profile.height,
                profile.weight
            )
        }
    }
}