package com.poker.data.repository

import com.apiclient.api.models.VoteResultsDto
import java.util.UUID

interface VoteRepository {
    suspend fun submitVote(storyId: UUID, participantId: UUID, value: Int)
    suspend fun getVoteResults(storyId: UUID, moderatorToken: String? = null): VoteResultsDto
}