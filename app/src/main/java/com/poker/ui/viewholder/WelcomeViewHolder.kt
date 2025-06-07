package com.poker.ui.viewholder

import androidx.recyclerview.widget.RecyclerView
import com.poker.databinding.ItemWelcomeBinding

class WelcomeViewHolder(
    private val binding: ItemWelcomeBinding
) : RecyclerView.ViewHolder(binding.root) {

    fun bind() {
        // Set up the welcome message and any onboarding UI elements
        binding.welcomeTitle.text = "Welcome to Planning Poker!"
        binding.welcomeDescription.text =
            "Streamline your agile estimation process with collaborative " +
                    "planning poker sessions. Create a session to get started!"

        // Set up the get started button
        binding.getStartedButton.setOnClickListener {
            // You could scroll to the bottom where the FAB is
            // or trigger the create session dialog directly

            // Example:
            // (itemView.context as? MainActivity)?.showCreateSessionDialog()
        }
    }
}
