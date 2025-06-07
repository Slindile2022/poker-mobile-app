package com.poker.ui.viewholder

import androidx.recyclerview.widget.RecyclerView
import com.apiclient.api.models.SessionDto
import com.poker.databinding.ItemSessionBinding
import com.poker.utils.DateUtils
import java.text.SimpleDateFormat
import java.util.Locale

class SessionViewHolder(
    private val binding: ItemSessionBinding,
    private val onSessionClicked: (SessionDto) -> Unit
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(session: SessionDto) {
        // Set session name
        binding.sessionName.text = session.name

        // Set participant count
        val participantCount = 0
        binding.participantsCount.text = "Participants: $participantCount"

        // Format and set creation date
        session.createdAt?.let { date ->
            binding.createdAt.text = DateUtils.formatSessionDate(date)
        }

        // Set click listener for the entire item
        binding.root.setOnClickListener {
            onSessionClicked(session)
        }
    }
}
