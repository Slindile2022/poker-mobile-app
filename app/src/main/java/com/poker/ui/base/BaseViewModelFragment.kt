package com.poker.ui.base

import android.os.Bundle
import android.view.View
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.viewbinding.ViewBinding

/**
 * Base fragment that adds ViewModel integration to BaseFragment
 */
abstract class BaseViewModelFragment<T : ViewBinding, VM : ViewModel> : BaseFragment<T>() {

    // The ViewModel associated with this fragment
    protected abstract val viewModel: VM

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // Set up ViewModel-specific observers
        setupViewModelObservers()
    }

    /**
     * Set up observers for ViewModel LiveData
     * Should be implemented by subclasses to observe specific ViewModel data
     */
    protected open fun setupViewModelObservers() {
        // Default implementation does nothing
        // Subclasses should override to set up specific observers
    }

    /**
     * Utility method to observe loading state
     */
    protected fun <T> LiveData<T>.observeWithLoading(
        loadingView: View,
        onChanged: (T) -> Unit
    ) {
        this.observe(viewLifecycleOwner) { data ->
            hideLoading(loadingView)
            onChanged(data)
        }
    }

    /**
     * Utility method to handle common loading states
     */
    protected fun observeLoadingState(loadingView: View, loadingState: LiveData<Boolean>) {
        loadingState.observe(viewLifecycleOwner) { isLoading ->
            if (isLoading) {
                showLoading(loadingView)
            } else {
                hideLoading(loadingView)
            }
        }
    }

    /**
     * Utility method to handle common error states
     */
    protected fun observeErrorState(errorView: View, errorState: LiveData<String?>) {
        errorState.observe(viewLifecycleOwner) { errorMessage ->
            showError(errorView, errorMessage)
        }
    }

    /**
     * Utility method to handle empty state
     */
    protected fun observeEmptyState(
        emptyView: View,
        contentView: View,
        dataState: LiveData<List<*>>
    ) {
        dataState.observe(viewLifecycleOwner) { items ->
            val isEmpty = items == null || items.isEmpty()
            emptyView.visibility = if (isEmpty) View.VISIBLE else View.GONE
            contentView.visibility = if (isEmpty) View.GONE else View.VISIBLE
        }
    }
}

