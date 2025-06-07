package com.poker.data.implementation
import com.apiclient.api.apis.StoriesApi
import com.apiclient.api.models.StoryCreateDto
import com.apiclient.api.models.StoryDto
import com.apiclient.api.models.StoryStatus
import com.apiclient.api.models.StoryStatusUpdateDto
import com.poker.data.repository.StoryRepository
import com.poker.utils.await
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.util.UUID
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class StoryRepositoryImpl @Inject constructor(
    private val storiesApi: StoriesApi
) : StoryRepository {

    override suspend fun getStoriesBySession(sessionId: UUID): List<StoryDto> = withContext(Dispatchers.IO) {
        storiesApi.apiStoriesSessionSessionIdGet(sessionId).await()
    }

    override suspend fun getStory(id: UUID): StoryDto = withContext(Dispatchers.IO) {
        storiesApi.apiStoriesIdGet(id).await()
    }

    override suspend fun createStory(title: String, description: String, sessionId: UUID, moderatorToken: String): StoryDto = withContext(Dispatchers.IO) {
        val dto = StoryCreateDto(title, description, sessionId, moderatorToken)
        storiesApi.apiStoriesPost(dto).await()
    }

    override suspend fun updateStoryStatus(id: UUID, status: StoryStatus, moderatorToken: String): Boolean = withContext(Dispatchers.IO) {
        val dto = StoryStatusUpdateDto(status, moderatorToken)
        storiesApi.apiStoriesIdStatusPatch(id, dto).await()
        true // If no exception was thrown, consider it successful
    }
}