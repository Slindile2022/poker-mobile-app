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
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SessionListFragment : BaseListFragment<FragmentSessionListBinding, SessionListViewModel, SessionListItem>() {

    override val screenTitle: String = "Planning Poker"

    override val viewModel: SessionListViewModel by viewModels()

    override val recyclerView: RecyclerView
        get() = binding.recyclerView

    private val sessionAdapter = SessionAdapter { session ->
        // Handle session click - navigate to session detail

    }

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
    }

    override fun observeData() {
        // Observe items changes and update the adapter
        viewModel.items.observe(viewLifecycleOwner) { items ->
            (adapter as SessionAdapter).updateItems(items)
        }
    }

    private fun showCreateSessionDialog() {
        val editText = EditText(requireContext()).apply {
            hint = "Session Name"
            layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            ).apply {
                setMargins(32, 16, 32, 16)
            }
        }

        AlertDialog.Builder(requireContext())
            .setTitle("Create New Session")
            .setView(editText)
            .setPositiveButton("Create") { _, _ ->
                val sessionName = editText.text.toString().trim()
                if (sessionName.isNotEmpty()) {
                    viewModel.createSession(sessionName)
                }
            }
            .setNegativeButton("Cancel", null)
            .show()
    }
}

