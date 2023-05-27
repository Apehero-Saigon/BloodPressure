package com.blood.utils

import android.text.format.DateFormat
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

object DateUtils {

    fun getCurrentYear(): Int {
        val calendar = Calendar.getInstance()
        return calendar.get(Calendar.YEAR)
    }

    fun getTime24(timestamp: Int, format: String): String {
        val calendar = Calendar.getInstance(Locale.ENGLISH)
        calendar.timeInMillis = timestamp.times(1000L)
        val time12 = DateFormat.format("dd/MM hh:mm a", calendar).toString()
        val df = SimpleDateFormat("dd/MM hh:mm aa", Locale.getDefault())
        val outputFormat = SimpleDateFormat(format, Locale.getDefault())
        val date: Date?
        val output: String?
        return try {
            date = df.parse(time12)
            output = outputFormat.format(date!!)
            output
        } catch (pe: ParseException) {
            pe.printStackTrace()
            time12
        }
    }

    fun getCurrentHour(): String {
        val format = SimpleDateFormat("HH", Locale.getDefault())
        return format.format(Date())
    }

    fun getDate(milliSeconds: Long, dateFormat: String): String? {
        val formatter = SimpleDateFormat(dateFormat, Locale.getDefault())
        val calendar = Calendar.getInstance()
        calendar.timeInMillis = milliSeconds
        return formatter.format(calendar.time)
    }
}