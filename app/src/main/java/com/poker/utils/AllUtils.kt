package com.poker.utils

import com.apiclient.api.models.SessionDto
import java.time.OffsetDateTime
import java.time.format.DateTimeFormatter
import java.util.UUID

/**
 * Utility class with extension functions for SessionDto
 */
object DateUtils {
    /**
     * Formats an OffsetDateTime for display in the UI
     */
    fun formatSessionDate(date: OffsetDateTime?): String {
        if (date == null) return "Recently created"

        return try {
            val formatter = DateTimeFormatter.ofPattern("MMM dd, HH:mm")
            "Created: ${date.format(formatter)}"
        } catch (e: Exception) {
            "Recently created"
        }
    }
}

/**
 * Extension functions for SessionDto to provide safer access and additional functionality
 */
// Safe access to ID with fallback
val SessionDto.safeId: UUID
    get() = this.id ?: UUID.randomUUID()

// Safe access to name with fallback
val SessionDto.safeName: String
    get() = this.name ?: "Unnamed Session"

// Get formatted creation date
val SessionDto.formattedDate: String
    get() = DateUtils.formatSessionDate(this.createdAt)

// Determine if session is new (created within the last hour)
val SessionDto.isNew: Boolean
    get() = this.createdAt?.isAfter(OffsetDateTime.now().minusHours(1)) == true

// Get a color based on session ID for consistent coloring
fun SessionDto.getAccentColor(colorPalette: List<String>): String {
    val index = this.safeId.hashCode().rem(colorPalette.size).let {
        if (it < 0) it + colorPalette.size else it
    }
    return colorPalette[index]
}