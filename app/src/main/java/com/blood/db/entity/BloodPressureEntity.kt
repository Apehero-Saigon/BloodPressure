package com.blood.db.entity

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.blood.data.BloodPressure
import kotlinx.parcelize.Parcelize
import java.util.Date

@Parcelize
@Entity(tableName = "table_blood_pressure")
class BloodPressureEntity(
    @PrimaryKey(autoGenerate = true) var id: Long = 0,
    @ColumnInfo(name = "profileId") val profileId: Long,
    @ColumnInfo(name = "systole") val systole: Int,
    @ColumnInfo(name = "diastole") val diastole: Int,
    @ColumnInfo(name = "pulse") val pulse: Int,
    @ColumnInfo(name = "createdAt") var createAt: Date
) : Parcelable {
    companion object {
        fun fromBloodPressure(bloodPressure: BloodPressure): BloodPressureEntity {
            return BloodPressureEntity(
                bloodPressure.id,
                bloodPressure.profileId,
                bloodPressure.systole,
                bloodPressure.diastole,
                bloodPressure.pulse,
                bloodPressure.createAt
            )
        }
    }
}