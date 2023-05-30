package com.blood.utils

import android.app.AlertDialog
import android.app.DatePickerDialog
import android.app.DatePickerDialog.OnDateSetListener
import android.app.TimePickerDialog
import android.content.Context
import android.text.format.DateFormat
import com.blood.common.Constant
import com.blood.utils.AppUtils.isNotNull
import com.bloodpressure.pressuremonitor.bloodpressuretracker.R
import java.text.ParseException
import java.text.SimpleDateFormat
import java.time.Instant
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*

object DateUtils {

    fun getCurrentYear(): Int {
        val calendar = Calendar.getInstance()
        return calendar.get(Calendar.YEAR)
    }

    fun getCurrentDate(): Date {
        return Calendar.getInstance().time
    }

    fun getDateBefore(before: Int): Date {
        val c = Calendar.getInstance()
        c.timeInMillis = c.timeInMillis - (36000000 * before)
        return c.time
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

    fun getDateStr(milliSeconds: Long, dateFormat: String): String? {
        val formatter = SimpleDateFormat(dateFormat, Locale.getDefault())
        val calendar = Calendar.getInstance()
        calendar.timeInMillis = milliSeconds
        return formatter.format(calendar.time)
    }

    fun format(dateStr: String, dateFormat: String): Date? {
        return try {
            val formatter = SimpleDateFormat(dateFormat, Locale.getDefault())
            formatter.parse(dateStr)
        } catch (ex: Exception) {
            null
        }
    }

    fun Date.strTime() = try {
        val c = Calendar.getInstance()
        c.time = this
        String.format("%2d:%2d", c.get(Calendar.MINUTE), c.get(Calendar.HOUR_OF_DAY))
    } catch (ex: java.lang.Exception) {
        ""
    }

    fun Date.strDateTime(dateFormat: String) = try {
        val formatter = SimpleDateFormat(dateFormat, Locale.getDefault())
        formatter.format(this)
    } catch (ex: java.lang.Exception) {
        ""
    }

    fun strTime(hour: Int, minute: Int) = try {
        String.format("%02d:%02d", hour, minute)
    } catch (ex: java.lang.Exception) {
        ""
    }

    fun openDatePicker(
        context: Context, date: Date? = null, listener: SelectDatetimeListener? = null
    ) {
        val c = Calendar.getInstance()
        if (date.isNotNull()) {
            c.time = date!!
        }
        val year = c.get(Calendar.YEAR)
        val month = c.get(Calendar.MONTH)
        val day = c.get(Calendar.DAY_OF_MONTH)


        val dpd = DatePickerDialog(
            context, { _, yearSelected, monthOfYear, dayOfMonth ->
                listener?.onDateSelected(dayOfMonth, monthOfYear + 1, yearSelected)
            }, year, month, day
        )

        dpd.show()
    }

    fun openTimePicker(context: Context, date: Date?, listener: SelectDatetimeListener? = null) {
        val c = Calendar.getInstance()
        if (date.isNotNull()) {
            c.time = date!!
        }
        val hour = c.get(Calendar.HOUR_OF_DAY)
        val minute = c.get(Calendar.MINUTE)
        val timeSetListener =
            TimePickerDialog.OnTimeSetListener { _, hourSelected, minuteSelected ->
                listener?.onTimeSelected(minuteSelected, hourSelected)
            }
        val timePickerDialog = TimePickerDialog(
            context, AlertDialog.THEME_HOLO_LIGHT, timeSetListener, hour, minute, true
        )
        timePickerDialog.show()
    }

    interface SelectDatetimeListener {
        fun onDateSelected(day: Int, month: Int, year: Int) {

        }

        fun onTimeSelected(minute: Int, hour: Int) {

        }
    }
}