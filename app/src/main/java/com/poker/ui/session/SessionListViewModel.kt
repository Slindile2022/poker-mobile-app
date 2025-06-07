package com.poker.ui.session

import android.content.SharedPreferences
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.apiclient.api.models.SessionDto
import com.poker.data.repository.SessionRepository
import com.poker.ui.base.BaseListViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SessionListViewModel @Inject constructor(
    private val sessionRepository: SessionRepository,
    private val sharedPrefs: SharedPreferences  // Inject shared preferences
) : BaseListViewModel<SessionListItem>() {

    // Implementation of abstract properties from BaseListViewModel
    private val _isLoading = MutableLiveData<Boolean>(false)
    override val isLoading: LiveData<Boolean> = _isLoading

    private val _error = MutableLiveData<String?>(null)
    override val error: LiveData<String?> = _error

    private val _items = MutableLiveData<List<SessionListItem>>(emptyList())
    override val items: LiveData<List<SessionListItem>> = _items

    // Additional property for raw session data
    private val _sessions = MutableLiveData<List<SessionDto>>()
    val sessions: LiveData<List<SessionDto>> = _sessions

    init {
        refresh()
    }

    override fun refresh() {
        loadSessions()
    }

    private fun loadSessions() {
        viewModelScope.launch {
            _isLoading.value = true
            _error.value = null

            try {
                val result = sessionRepository.getSessions()
                _sessions.value = result
                _items.value = transformToListItems(result)
            } catch (e: Exception) {
                _error.value = "Failed to load sessions: ${e.message}"
                _items.value = emptyList()
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun createSession(name: String) {
        viewModelScope.launch {
            _isLoading.value = true
            _error.value = null

            try {
                sessionRepository.createSession(name)
                loadSessions() // Reload sessions after creating a new one
            } catch (e: Exception) {
                _error.value = "Failed to create session: ${e.message}"
                _isLoading.value = false
            }
        }
    }

    private fun transformToListItems(sessions: List<SessionDto>?): List<SessionListItem> {
        if (sessions.isNullOrEmpty()) {
            // Decide whether to show welcome or empty state
            val isFirstLaunch = sharedPrefs.getBoolean(PREF_FIRST_LAUNCH, true)
            return if (isFirstLaunch) {
                sharedPrefs.edit().putBoolean(PREF_FIRST_LAUNCH, false).apply()
                listOf(SessionListItem.WelcomeMessage)
            } else {
                listOf(SessionListItem.EmptyState)
            }
        }

        // Map sessions to SessionListItems
        return sessions.map { SessionListItem.SessionItem(it) }
    }

    companion object {
        private const val PREF_FIRST_LAUNCH = "first_launch"
    }
}


