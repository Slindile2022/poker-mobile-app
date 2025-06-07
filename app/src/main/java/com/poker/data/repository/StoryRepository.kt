package com.poker.data.repository

import com.apiclient.api.models.StoryDto
import com.apiclient.api.models.StoryStatus
import java.util.UUID

interface StoryRepository {
    suspend fun getStoriesBySession(sessionId: UUID): List<StoryDto>
    suspend fun getStory(id: UUID): StoryDto
    suspend fun createStory(title: String, description: String, sessionId: UUID, moderatorToken: String): StoryDto
    suspend fun updateStoryStatus(id: UUID, status: StoryStatus, moderatorToken: String): Boolean
}