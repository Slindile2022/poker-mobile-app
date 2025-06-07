package com.poker.ui.viewholder

import androidx.recyclerview.widget.RecyclerView
import com.poker.databinding.ItemNoResultsBinding

class NoResultsViewHolder(
    private val binding: ItemNoResultsBinding,
    private val onClearSearch: () -> Unit
) : RecyclerView.ViewHolder(binding.root) {

    fun bind() {
        // Update message to include the search query
        binding.noResultsMessage.text = "Try a different search term or browse all sessions"

        // Set up clear search button
        binding.clearSearchButton.setOnClickListener {
            onClearSearch()
        }
    }
}
