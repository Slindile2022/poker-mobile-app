package com.poker.ui.viewholder

import androidx.recyclerview.widget.RecyclerView
import com.poker.databinding.ItemEmptyStateBinding

class EmptyStateViewHolder(
    private val binding: ItemEmptyStateBinding
) : RecyclerView.ViewHolder(binding.root) {

    fun bind() {
        // You can customize the empty state here if needed
        // For example, you might want to set different messages
        // based on search filters or other app states

        binding.emptyStateTitle.text = "No Sessions Available"
        binding.emptyStateDescription.text =
            "Create a new planning session using the + button below"

        // You could also set up click listeners for any actions
        // in the empty state view if needed
    }
}
