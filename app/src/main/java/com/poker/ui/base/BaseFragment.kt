package com.poker.ui.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.viewbinding.ViewBinding

/**
 * Base fragment that handles common functionality for all fragments
 */
abstract class BaseFragment<T : ViewBinding> : Fragment() {

    // ViewBinding reference
    private var _binding: T? = null
    protected val binding: T get() = _binding!!

    // Title to be displayed in the action bar
    abstract val screenTitle: String

    // Factory method to create the binding
    abstract fun createBinding(inflater: LayoutInflater, container: ViewGroup?): T

    // Abstract method for setting up UI elements
    abstract fun setupUI()

    // Abstract method for observing ViewModel data
    abstract fun observeData()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = createBinding(inflater, container)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        requireActivity().title = screenTitle
        setupUI()
        observeData()
    }

    /**
     * Shows a loading indicator
     */
    protected fun showLoading(view: View) {
        view.visibility = View.VISIBLE
    }

    /**
     * Hides a loading indicator
     */
    protected fun hideLoading(view: View) {
        view.visibility = View.GONE
    }

    /**
     * Shows an error message
     */
    protected fun showError(errorView: View, message: String?) {
        if (message != null) {
            errorView.visibility = View.VISIBLE
            if (errorView is android.widget.TextView) {
                errorView.text = message
            }
        } else {
            errorView.visibility = View.GONE
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}