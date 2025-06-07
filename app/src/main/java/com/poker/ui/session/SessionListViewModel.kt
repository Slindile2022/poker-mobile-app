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

    // Implementation of abstract properties from BaseListViewModel
    private val _isLoading = MutableLiveData<Boolean>(false)
    override val isLoading: LiveData<Boolean> = _isLoading

    private val _error = MutableLiveData<String?>(null)
    override val error: LiveData<String?> = _error

    // Search query property
    private val _searchQuery = MutableLiveData<String>("")
    val searchQuery: LiveData<String> = _searchQuery

    // Raw session data
    private val _sessions = MutableLiveData<List<SessionDto>>()
    val sessions: LiveData<List<SessionDto>> = _sessions

    // Filtered items based on search and session state
    private val _filteredItems = MediatorLiveData<List<SessionListItem>>()
    override val items: LiveData<List<SessionListItem>> = _filteredItems

    init {
        // Set up mediator to react to both search query and sessions changes
        _filteredItems.addSource(_searchQuery) { query ->
            _sessions.value?.let { sessions ->
                _filteredItems.value = filterAndTransform(sessions, query)
            }
        }

        _filteredItems.addSource(_sessions) { sessions ->
            _filteredItems.value = filterAndTransform(sessions, _searchQuery.value ?: "")
        }

        // Initial data load
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
        // Handle empty sessions list first
        if (sessions.isEmpty()) {
            return handleEmptySessionsList()
        }

        // Apply search filter if query is not empty
        val filteredSessions = if (query.isBlank()) {
            sessions
        } else {
            sessions.filter { session ->
                session.name?.contains(query, ignoreCase = true) == true
            }
        }

        // Handle no results after filtering
        if (filteredSessions.isEmpty() && query.isNotBlank()) {
            return listOf(SessionListItem.NoSearchResults(query))
        }

        // Transform filtered sessions to list items
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
                // Load sessions with date parsing error handling
                val result = try {
                    sessionRepository.getSessions()
                } catch (e: java.time.format.DateTimeParseException) {
                    handleDateParsingError(e)
                }

                // Store raw sessions data
                _sessions.value = result

                // Note: We don't need to explicitly update _filteredItems here
                // since the MediatorLiveData observer will trigger when _sessions changes

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
                loadSessions() // Reload sessions after creating a new one
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
