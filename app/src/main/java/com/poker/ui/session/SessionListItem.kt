package com.poker.ui.session

import com.apiclient.api.models.SessionDto

sealed class SessionListItem {
    data class SessionItem(val session: SessionDto) : SessionListItem()
    object EmptyState : SessionListItem()
    object WelcomeMessage : SessionListItem()
    data class NoSearchResults(val query: String) : SessionListItem()
}