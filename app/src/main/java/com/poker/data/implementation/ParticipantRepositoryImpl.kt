package com.poker.data.implementation

import com.apiclient.api.apis.ParticipantsApi
import com.apiclient.api.models.ParticipantCreateDto
import com.apiclient.api.models.ParticipantDto
import com.poker.data.repository.ParticipantRepository
import com.poker.utils.await
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.util.UUID
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ParticipantRepositoryImpl @Inject constructor(
    private val participantsApi: ParticipantsApi
) : ParticipantRepository {

    override suspend fun getParticipantsBySession(sessionId: UUID): List<ParticipantDto> = withContext(Dispatchers.IO) {
        participantsApi.apiParticipantsSessionSessionIdGet(sessionId).await()
    }

    override suspend fun getParticipant(id: UUID): ParticipantDto = withContext(Dispatchers.IO) {
        participantsApi.apiParticipantsIdGet(id).await()
    }

    override suspend fun addParticipant(name: String, sessionId: UUID): ParticipantDto = withContext(Dispatchers.IO) {
        val dto = ParticipantCreateDto(name, sessionId)
        participantsApi.apiParticipantsPost(dto).await()
    }

    override suspend fun removeParticipant(id: UUID) = withContext(Dispatchers.IO) {
        participantsApi.apiParticipantsIdDelete(id).await()
    }
}