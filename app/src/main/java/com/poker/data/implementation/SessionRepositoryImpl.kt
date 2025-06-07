package com.poker.data.implementation

import com.apiclient.api.apis.SessionsApi
import com.apiclient.api.models.SessionCreateDto
import com.apiclient.api.models.SessionDto
import com.poker.data.repository.SessionRepository
import com.poker.utils.await
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.util.UUID
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SessionRepositoryImpl @Inject constructor(
    private val sessionsApi: SessionsApi
) : SessionRepository {

    override suspend fun getSessions(): List<SessionDto> = withContext(Dispatchers.IO) {
        sessionsApi.apiSessionsGet().await()
    }

    override suspend fun getSession(id: UUID): SessionDto = withContext(Dispatchers.IO) {
        sessionsApi.apiSessionsIdGet(id).await()
    }

    override suspend fun createSession(name: String): SessionDto = withContext(Dispatchers.IO) {
        val dto = SessionCreateDto(name)
        sessionsApi.apiSessionsPost(dto).await()
    }
}
