package com.poker.ui.session

import android.app.AlertDialog
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.LinearLayout
import androidx.fragment.app.viewModels
import androidx.lifecycle.LiveData
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.poker.databinding.FragmentSessionListBinding
import com.poker.ui.base.BaseListFragment
import com.poker.utils.ModernSearchView
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SessionListFragment : BaseListFragment<FragmentSessionListBinding, SessionListViewModel, SessionListItem>() {

    override val screenTitle: String = "Planning Poker"

    override val viewModel: SessionListViewModel by viewModels()

    override val recyclerView: RecyclerView
        get() = binding.recyclerView

    private val sessionAdapter = SessionAdapter(
        onSessionClicked = { session ->
            // Handle session click
        },
        onClearSearch = {
            // Clear the search
            viewModel.clearSearch()
        }
    )


    override val adapter: RecyclerView.Adapter<*>
        get() = sessionAdapter

    override val loadingView: View
        get() = binding.progressBar

    override val errorView: View
        get() = binding.errorText

    override val swipeRefreshLayout: SwipeRefreshLayout
        get() = binding.swipeRefresh

    override val emptyView: View
        get() = binding.emptyView

    // Use the items property from the BaseListViewModel
    override val items: LiveData<List<SessionListItem>>
        get() = viewModel.items

    override fun createBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentSessionListBinding {
        return FragmentSessionListBinding.inflate(inflater, container, false)
    }

    override fun setupUI() {
        // Setup RecyclerView
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        recyclerView.adapter = adapter

        // Setup create button
        binding.createButton.setOnClickListener {
            showCreateSessionDialog()
        }

        // Setup modern search view
        binding.searchView.setOnQueryTextListener(object : ModernSearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                viewModel.setSearchQuery(query)
                return true
            }

            override fun onQueryTextChange(newText: String): Boolean {
                viewModel.setSearchQuery(newText)
                return true
            }
        })
    }

    override fun observeData() {
        // Observe search query to sync UI state
        viewModel.searchQuery.observe(viewLifecycleOwner) { query ->
            binding.searchView.setQuery(query, false)
        }


        // Observe items changes and update the adapter
        viewModel.items.observe(viewLifecycleOwner) { items ->
            (adapter as SessionAdapter).updateItems(items)

            // Update empty view visibility
            val showEmptyView = items.size == 1 && items[0] is SessionListItem.EmptyState
            emptyView.visibility = if (showEmptyView) View.VISIBLE else View.GONE
        }
    }

    private fun showCreateSessionDialog() {
        val bottomSheet = CreateSessionBottomSheet.newInstance()

        bottomSheet.setOnSessionCreatedListener { sessionName ->
            viewModel.createSession(sessionName)
        }

        bottomSheet.show(childFragmentManager, CreateSessionBottomSheet.TAG)
    }

}

