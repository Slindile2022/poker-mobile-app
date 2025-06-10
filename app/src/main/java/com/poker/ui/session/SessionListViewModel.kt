package com.poker.ui.session

import android.content.SharedPreferences
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
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
    private val sharedPrefs: SharedPreferences
) : BaseListViewModel<SessionListItem>() {

    private val _isLoading = MutableLiveData(false)
    override val isLoading: LiveData<Boolean> = _isLoading

    private val _error = MutableLiveData<String?>(null)
    override val error: LiveData<String?> = _error

    private val _searchQuery = MutableLiveData("")
    val searchQuery: LiveData<String> = _searchQuery

    private val _sessions = MutableLiveData<List<SessionDto>>()
    val sessions: LiveData<List<SessionDto>> = _sessions

    private val _filteredItems = MediatorLiveData<List<SessionListItem>>()
    override val items: LiveData<List<SessionListItem>> = _filteredItems

    init {
        _filteredItems.addSource(_searchQuery) { query ->
            _sessions.value?.let { sessions -> _filteredItems.value = filterAndTransform(sessions, query) }
        }

        _filteredItems.addSource(_sessions) { sessions -> _filteredItems.value = filterAndTransform(sessions, _searchQuery.value ?: "") }
        refresh()
    }

    /**
     * Sets a new search query and triggers filtering
     */
    fun setSearchQuery(query: String) {
        if (_searchQuery.value != query) {
            _searchQuery.value = query
        }
    }

    /**
     * Clears the current search query
     */
    fun clearSearch() {
        setSearchQuery("")
    }

    /**
     * Filters and transforms sessions based on search query
     */
    private fun filterAndTransform(sessions: List<SessionDto>, query: String): List<SessionListItem> {
        if (sessions.isEmpty()) return handleEmptySessionsList()
        val filteredSessions = if (query.isBlank()) { sessions } else { sessions.filter { session -> session.name?.contains(query, ignoreCase = true) == true } }
        if (filteredSessions.isEmpty() && query.isNotBlank()) return listOf(SessionListItem.NoSearchResults(query))
        return filteredSessions.map { SessionListItem.SessionItem(it) }
    }

    /**
     * Handles the case when no sessions are available
     */
    private fun handleEmptySessionsList(): List<SessionListItem> {
        val isFirstLaunch = sharedPrefs.getBoolean(PREF_FIRST_LAUNCH, true)
        return if (isFirstLaunch) {
            sharedPrefs.edit().putBoolean(PREF_FIRST_LAUNCH, false).apply()
            listOf(SessionListItem.WelcomeMessage)
        } else {
            listOf(SessionListItem.EmptyState)
        }
    }

    override fun refresh() {
        loadSessions()
    }

    /**
     * Loads sessions from the repository
     */
    private fun loadSessions() {
        viewModelScope.launch {
            _isLoading.value = true
            _error.value = null

            try {
                val result = try {
                    sessionRepository.getSessions()
                } catch (e: java.time.format.DateTimeParseException) {
                    handleDateParsingError(e)
                }
                _sessions.value = result
            } catch (e: Exception) {
                _error.value = "Failed to load sessions: ${e.message}"
                _sessions.value = emptyList()
            } finally {
                _isLoading.value = false
            }
        }
    }

    /**
     * Fallback handler for date parsing errors
     */
    private fun handleDateParsingError(e: Exception): List<SessionDto> {
        Log.e("SessionListViewModel", "Date parsing error: ${e.message}")
        return emptyList()
    }

    /**
     * Creates a new session
     */
    fun createSession(name: String) {
        viewModelScope.launch {
            _isLoading.value = true
            _error.value = null

            try {
                sessionRepository.createSession(name)
                loadSessions()
            } catch (e: Exception) {
                _error.value = "Failed to create session: ${e.message}"
                _isLoading.value = false
            }
        }
    }

    companion object {
        private const val PREF_FIRST_LAUNCH = "first_launch"
    }
}
