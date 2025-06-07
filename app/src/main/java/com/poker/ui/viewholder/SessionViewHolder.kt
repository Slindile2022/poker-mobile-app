package com.poker.ui.viewholder

import androidx.recyclerview.widget.RecyclerView
import com.apiclient.api.models.SessionDto
import com.poker.databinding.ItemSessionBinding
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
            val formatter = SimpleDateFormat("MMM dd, yyyy HH:mm", Locale.getDefault())
            binding.createdAt.text = "Created: ${formatter.format(date)}"
        } ?: run {
            binding.createdAt.text = "Recently created"
        }

        // Set click listener for the entire item
        binding.root.setOnClickListener {
            onSessionClicked(session)
        }
    }
}
