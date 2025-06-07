package com.poker.data.repository

import com.apiclient.api.models.ParticipantDto
import java.util.UUID

interface ParticipantRepository {
    suspend fun getParticipantsBySession(sessionId: UUID): List<ParticipantDto>
    suspend fun getParticipant(id: UUID): ParticipantDto
    suspend fun addParticipant(name: String, sessionId: UUID): ParticipantDto
    suspend fun removeParticipant(id: UUID)
}