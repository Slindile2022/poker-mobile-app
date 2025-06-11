package com.poker.ui.session

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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
        onSessionClicked = { _ -> },
        onClearSearch = { viewModel.clearSearch() }
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

    override val items: LiveData<List<SessionListItem>>
        get() = viewModel.items

    override fun createBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentSessionListBinding {
        return FragmentSessionListBinding.inflate(inflater, container, false)
    }

    override fun setupUI() {
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        recyclerView.adapter = adapter
        binding.createButton.setOnClickListener { showCreateSessionDialog() }
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
        viewModel.searchQuery.observe(viewLifecycleOwner) { query -> binding.searchView.setQuery(query, false) }

        viewModel.items.observe(viewLifecycleOwner) { items ->
            (adapter as SessionAdapter).updateItems(items)
            val showEmptyView = items.size == 1 && items[0] is SessionListItem.EmptyState
            emptyView.visibility = if (showEmptyView) View.VISIBLE else View.GONE
        }
    }

    private fun showCreateSessionDialog() {
        val bottomSheet = CreateSessionBottomSheet.newInstance()
        bottomSheet.setOnSessionCreatedListener { sessionName -> viewModel.createSession(sessionName) }
        bottomSheet.show(childFragmentManager, CreateSessionBottomSheet.TAG)
    }

}

