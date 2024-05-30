package com.example.gihubusertest.utils

import java.text.DateFormat
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.Locale
import java.util.TimeZone

class DateFormatter {
    fun formatDate(currentDate: String): String? {
        val currentFormat = "yyyy-MM-dd'T'hh:mm:ss'Z'"
        val targetFormat = "dd MMM yyyy | HH:mm"
        val timezone = "GMT"
        val currentDf: DateFormat = SimpleDateFormat(currentFormat, Locale.getDefault())
        currentDf.timeZone = TimeZone.getTimeZone(timezone)
        val targetDf: DateFormat = SimpleDateFormat(targetFormat, Locale.getDefault())
        return try {
            val date = currentDf.parse(currentDate)
            date?.let { targetDf.format(it) }
        } catch (ex: ParseException) {
            ex.printStackTrace()
            null
        }
    }
}
