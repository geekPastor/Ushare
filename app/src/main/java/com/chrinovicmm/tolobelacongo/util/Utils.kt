package com.chrinovicmm.tolobelacongo.util

import android.icu.text.SimpleDateFormat
import androidx.compose.ui.graphics.Color
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Date
import java.util.Locale

fun Color.Companion.fromHex(colorString: String): Color {
    val color = if (colorString.startsWith("#")) colorString.substring(1) else colorString
    return Color(android.graphics.Color.parseColor("#$color"))
}

val Date.dayOfMonth: String
    get() = SimpleDateFormat("dd", Locale.FRENCH).format(this)

val Date.monthName: String
    get() = SimpleDateFormat("MMM", Locale.FRENCH).format(this)

val Date.readableDate: String
    get() = SimpleDateFormat("dd MMM yyyy", Locale.FRENCH).format(this)

val Date.readableDateWithDayName: String
    get() = SimpleDateFormat("EEEE, dd MMM yyyy", Locale.FRENCH).format(this)

val Date.DateAndTime: String
    get() = SimpleDateFormat("dd MMM yyyy HH:mm", Locale.FRENCH).format(this)

val Date.hoursAndMins: String
    get() = SimpleDateFormat("HH:mm", Locale.FRENCH).format(this)
