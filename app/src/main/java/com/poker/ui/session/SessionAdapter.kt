package com.poker.ui.session

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.apiclient.api.models.SessionDto
import com.poker.databinding.ItemEmptyStateBinding
import com.poker.databinding.ItemSessionBinding
import com.poker.databinding.ItemWelcomeBinding
import com.poker.ui.viewholder.EmptyStateViewHolder
import com.poker.ui.viewholder.SessionViewHolder
import com.poker.ui.viewholder.WelcomeViewHolder

class SessionAdapter(
    private val onSessionClicked: (SessionDto) -> Unit
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var items: ArrayList<SessionListItem> = arrayListOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return when (viewType) {
            VIEW_TYPE_SESSION -> {
                val binding = ItemSessionBinding.inflate(inflater, parent, false)
                SessionViewHolder(binding, onSessionClicked)
            }
            VIEW_TYPE_EMPTY -> {
                val binding = ItemEmptyStateBinding.inflate(inflater, parent, false)
                EmptyStateViewHolder(binding)
            }
            VIEW_TYPE_WELCOME -> {
                val binding = ItemWelcomeBinding.inflate(inflater, parent, false)
                WelcomeViewHolder(binding)
            }
            else -> throw IllegalArgumentException("Unsupported view type: $viewType")
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (val item = items[position]) {
            is SessionListItem.SessionItem -> (holder as SessionViewHolder).bind(item.session)
            is SessionListItem.EmptyState -> (holder as EmptyStateViewHolder).bind()
            is SessionListItem.WelcomeMessage -> (holder as WelcomeViewHolder).bind()
        }
    }

    override fun getItemCount() = items.size

    override fun getItemViewType(position: Int): Int {
        return when (items[position]) {
            is SessionListItem.SessionItem -> VIEW_TYPE_SESSION
            is SessionListItem.EmptyState -> VIEW_TYPE_EMPTY
            is SessionListItem.WelcomeMessage -> VIEW_TYPE_WELCOME
        }
    }

    fun updateItems(newItems: List<SessionListItem>) {
        val diffCallback = SessionListDiffCallback(this.items, newItems)
        val diffResult = DiffUtil.calculateDiff(diffCallback)
        this.items.clear()
        this.items.addAll(newItems)
        diffResult.dispatchUpdatesTo(this)
    }

    fun clearData() {
        val diffCallback = SessionListDiffCallback(this.items, arrayListOf())
        val diffResult = DiffUtil.calculateDiff(diffCallback)
        items.clear()
        diffResult.dispatchUpdatesTo(this)
    }

    // DiffUtil implementation
    private class SessionListDiffCallback(
        private val oldList: List<SessionListItem>,
        private val newList: List<SessionListItem>
    ) : DiffUtil.Callback() {

        override fun getOldListSize() = oldList.size

        override fun getNewListSize() = newList.size

        override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            val oldItem = oldList[oldItemPosition]
            val newItem = newList[newItemPosition]

            if (oldItem::class != newItem::class) return false

            return when {
                oldItem is SessionListItem.SessionItem && newItem is SessionListItem.SessionItem ->
                    oldItem.session.id == newItem.session.id
                oldItem is SessionListItem.EmptyState && newItem is SessionListItem.EmptyState -> true
                oldItem is SessionListItem.WelcomeMessage && newItem is SessionListItem.WelcomeMessage -> true
                else -> false
            }
        }

        override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            val oldItem = oldList[oldItemPosition]
            val newItem = newList[newItemPosition]

            return when {
                oldItem is SessionListItem.SessionItem && newItem is SessionListItem.SessionItem ->
                    oldItem.session == newItem.session
                oldItem is SessionListItem.EmptyState && newItem is SessionListItem.EmptyState -> true
                oldItem is SessionListItem.WelcomeMessage && newItem is SessionListItem.WelcomeMessage -> true
                else -> false
            }
        }
    }

    companion object {
        private const val VIEW_TYPE_SESSION = 0
        private const val VIEW_TYPE_EMPTY = 1
        private const val VIEW_TYPE_WELCOME = 2
    }
}