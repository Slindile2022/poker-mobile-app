package com.poker.utils

import android.content.Context
import android.text.Editable
import android.text.TextWatcher
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.EditText
import android.widget.FrameLayout
import android.widget.ImageView
import com.poker.databinding.ViewModernSearchBinding

class ModernSearchView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {

    private val binding: ViewModernSearchBinding
    private val searchEditText: EditText
    private val clearButton: ImageView

    private var onQueryTextListener: OnQueryTextListener? = null

    init {
        binding = ViewModernSearchBinding.inflate(LayoutInflater.from(context), this, true)
        searchEditText = binding.searchEditText
        clearButton = binding.clearSearchButton

        // Set up clear button
        clearButton.setOnClickListener {
            searchEditText.text.clear()
            clearButton.visibility = View.GONE
            onQueryTextListener?.onQueryTextChange("")
        }

        // Set up edit text listeners
        searchEditText.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                clearButton.visibility = if (s.isNullOrEmpty()) View.GONE else View.VISIBLE
                onQueryTextListener?.onQueryTextChange(s?.toString() ?: "")
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })

        searchEditText.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                onQueryTextListener?.onQueryTextSubmit(searchEditText.text.toString())
                return@setOnEditorActionListener true
            }
            false
        }
    }

    fun setOnQueryTextListener(listener: OnQueryTextListener) {
        this.onQueryTextListener = listener
    }

    fun setQuery(query: String, submit: Boolean) {
        searchEditText.setText(query)
        clearButton.visibility = if (query.isEmpty()) View.GONE else View.VISIBLE
        if (submit) {
            onQueryTextListener?.onQueryTextSubmit(query)
        }
    }

    interface OnQueryTextListener {
        fun onQueryTextSubmit(query: String): Boolean
        fun onQueryTextChange(newText: String): Boolean
    }
}
