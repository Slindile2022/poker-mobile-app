package com.poker.di
import com.apiclient.api.apis.ParticipantsApi
import com.apiclient.api.apis.SessionsApi
import com.apiclient.api.apis.StoriesApi
import com.apiclient.api.apis.VotesApi
import com.apiclient.api.infrastructure.ApiClient
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Provides
    @Singleton
    fun provideApiClient(): ApiClient {
        return ApiClient(BASE_URL).apply {
            setLogger { message ->
                // You can route this to your logging system
                println("API: $message")
            }
        }
    }

    @Provides
    @Singleton
    fun provideSessionsApi(apiClient: ApiClient): SessionsApi {
        return apiClient.createService(SessionsApi::class.java)
    }

    @Provides
    @Singleton
    fun provideParticipantsApi(apiClient: ApiClient): ParticipantsApi {
        return apiClient.createService(ParticipantsApi::class.java)
    }

    @Provides
    @Singleton
    fun provideStoriesApi(apiClient: ApiClient): StoriesApi {
        return apiClient.createService(StoriesApi::class.java)
    }

    @Provides
    @Singleton
    fun provideVotesApi(apiClient: ApiClient): VotesApi {
        return apiClient.createService(VotesApi::class.java)
    }

    private const val BASE_URL = "https://your-actual-api-url/" // Change to your actual API URL
}
