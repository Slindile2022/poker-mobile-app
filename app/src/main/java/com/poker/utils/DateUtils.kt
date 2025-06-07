package com.poker.utils

import android.os.Build
import java.time.OffsetDateTime
import java.time.format.DateTimeFormatter

object DateUtils {
    fun formatSessionDate(offsetDateTime: OffsetDateTime?): String {
        if (offsetDateTime == null) return "Recently created"

        return try {
            val formatter = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                DateTimeFormatter.ofPattern("MMM dd, HH:mm")
            } else {
                TODO("VERSION.SDK_INT < O")
            }
            "Created: ${offsetDateTime.format(formatter)}"
        } catch (e: Exception) {
            "Recently created"
        }
    }
}
