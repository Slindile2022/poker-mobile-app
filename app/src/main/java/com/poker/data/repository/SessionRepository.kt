package com.poker.data.repository

import com.apiclient.api.models.SessionDto
import java.util.UUID

interface SessionRepository {
    suspend fun getSessions(): List<SessionDto>
    suspend fun getSession(id: UUID): SessionDto
    suspend fun createSession(name: String): SessionDto
}