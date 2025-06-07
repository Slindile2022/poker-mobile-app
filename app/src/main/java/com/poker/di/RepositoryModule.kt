package com.poker.di

import com.poker.data.repository.ParticipantRepository
import com.poker.data.implementation.ParticipantRepositoryImpl
import com.poker.data.repository.SessionRepository
import com.poker.data.implementation.SessionRepositoryImpl
import com.poker.data.repository.StoryRepository
import com.poker.data.implementation.StoryRepositoryImpl
import com.poker.data.repository.VoteRepository
import com.poker.data.implementation.VoteRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun bindSessionRepository(
        sessionRepositoryImpl: SessionRepositoryImpl
    ): SessionRepository

    @Binds
    @Singleton
    abstract fun bindParticipantRepository(
        participantRepositoryImpl: ParticipantRepositoryImpl
    ): ParticipantRepository

    @Binds
    @Singleton
    abstract fun bindStoryRepository(
        storyRepositoryImpl: StoryRepositoryImpl
    ): StoryRepository

    @Binds
    @Singleton
    abstract fun bindVoteRepository(
        voteRepositoryImpl: VoteRepositoryImpl
    ): VoteRepository
}