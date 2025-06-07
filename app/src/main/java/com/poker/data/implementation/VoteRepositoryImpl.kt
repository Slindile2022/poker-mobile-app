package com.poker.data.implementation

import com.apiclient.api.apis.VotesApi
import com.apiclient.api.models.VoteResultsDto
import com.apiclient.api.models.VoteSubmitDto
import com.poker.data.repository.VoteRepository
import com.poker.utils.await
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.util.UUID
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class VoteRepositoryImpl @Inject constructor(
    private val votesApi: VotesApi
) : VoteRepository {

    override suspend fun submitVote(storyId: UUID, participantId: UUID, value: Int) = withContext(Dispatchers.IO) {
        val dto = VoteSubmitDto(storyId, participantId, value)
        votesApi.apiVotesPost(dto).await()
    }

    override suspend fun getVoteResults(storyId: UUID, moderatorToken: String?): VoteResultsDto = withContext(Dispatchers.IO) {
        votesApi.apiVotesStoryStoryIdGet(storyId, moderatorToken).await()
    }
}