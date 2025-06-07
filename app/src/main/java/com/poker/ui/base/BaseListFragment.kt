package com.poker.ui.base

import android.os.Bundle
import android.view.View
import androidx.lifecycle.LiveData
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import androidx.viewbinding.ViewBinding
import com.poker.ui.base.BaseListViewModel

/**
 * Base fragment specifically for list displays
 */
abstract class BaseListFragment<T : ViewBinding, VM : BaseListViewModel<I>, I : Any> :
    BaseViewModelFragment<T, VM>() {

    // The RecyclerView in this fragment
    protected abstract val recyclerView: RecyclerView

    // The adapter for the RecyclerView
    protected abstract val adapter: RecyclerView.Adapter<*>

    // Loading view (e.g., ProgressBar)
    protected abstract val loadingView: View

    // Error display view (e.g., TextView)
    protected open val errorView: View? = null

    // Optional SwipeRefreshLayout
    protected open val swipeRefreshLayout: SwipeRefreshLayout? = null

    // Optional empty view
    protected open val emptyView: View? = null

    // The LiveData containing the list items
    protected abstract val items: LiveData<List<I>>

    // Add this helper property with explicit cast
    private val itemsWildcard: LiveData<List<*>>
        get() {
            @Suppress("UNCHECKED_CAST")
            return items as LiveData<List<*>>
        }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Set up SwipeRefreshLayout if present
        swipeRefreshLayout?.setOnRefreshListener {
            viewModel.refresh()
            swipeRefreshLayout?.isRefreshing = false
        }
    }

    override fun setupViewModelObservers() {
        super.setupViewModelObservers()

        // Observe loading and error states
        observeLoadingState(loadingView, viewModel.isLoading)

        if (errorView != null) {
            observeErrorState(errorView!!, viewModel.error)
        }

        // Observe items
        emptyView?.let { observeEmptyState(it, recyclerView, itemsWildcard) }

    }
}