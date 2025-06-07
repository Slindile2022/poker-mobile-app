package com.poker.ui.base

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel

abstract class BaseListViewModel<T> : ViewModel() {
    abstract val isLoading: LiveData<Boolean>
    abstract val error: LiveData<String?>
    abstract val items: LiveData<List<T>>
    abstract fun refresh()
}
