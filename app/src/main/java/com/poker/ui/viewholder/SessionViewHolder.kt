package com.poker.ui.viewholder

import android.graphics.Color
import androidx.recyclerview.widget.RecyclerView
import com.apiclient.api.models.SessionDto
import com.poker.databinding.ItemSessionBinding
import com.poker.utils.formattedDate
import com.poker.utils.getAccentColor
import com.poker.utils.isNew
import com.poker.utils.safeName

class SessionViewHolder(
    private val binding: ItemSessionBinding,
    private val onSessionClicked: (SessionDto) -> Unit
) : RecyclerView.ViewHolder(binding.root) {

    // List of colors for the accent bar
    private val accentColors = listOf(
        "#4285F4", "#EA4335", "#FBBC05", "#34A853", "#8E24AA", "#F4511E"
    )

    fun bind(session: SessionDto) {
        // Use the extension properties for safer access
        binding.sessionName.text = session.safeName

        // Set accent bar color using extension function
        val accentColor = session.getAccentColor(accentColors)
        binding.accentBar.setBackgroundColor(Color.parseColor(accentColor))

        // Set participant count (0 is a placeholder)
        val participantCount = 0
        binding.participantsCount.text = when (participantCount) {
            0 -> "No participants yet"
            1 -> "1 Participant"
            else -> "$participantCount Participants"
        }

        // Use the formatted date extension
        binding.createdAt.text = session.formattedDate

        // Set status chip based on session state
        val (statusText, statusColor, bgColor) = when {
            session.isNew -> Triple("New", "#2196F3", "#E3F2FD")
            else -> Triple("Active", "#4CAF50", "#E8F5E9")
        }

        binding.statusChip.text = statusText
        binding.statusChip.setTextColor(Color.parseColor(statusColor))
        binding.statusChip.chipBackgroundColor = android.content.res.ColorStateList.valueOf(
            Color.parseColor(bgColor)
        )

        // Set click listener
        binding.root.setOnClickListener {
            onSessionClicked(session)
        }
    }
}
