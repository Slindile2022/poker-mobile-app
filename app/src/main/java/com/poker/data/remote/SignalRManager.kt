package com.poker.data.remote

import com.google.gson.JsonObject
import com.microsoft.signalr.HubConnection
import com.microsoft.signalr.HubConnectionBuilder
import com.microsoft.signalr.HubConnectionState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SignalRManager @Inject constructor() {
    private val coroutineScope = CoroutineScope(SupervisorJob() + Dispatchers.IO)
    private var hubConnection: HubConnection? = null

    fun initialize(baseUrl: String) {
        val hubUrl = "$baseUrl/planningpokerhub"
        hubConnection = HubConnectionBuilder.create(hubUrl).build()
    }

    fun connect() {
        coroutineScope.launch {
            hubConnection?.let { hub ->
                if (hub.connectionState != HubConnectionState.CONNECTED) {
                    hub.start().blockingAwait()
                }
            }
        }
    }

    fun disconnect() {
        coroutineScope.launch {
            hubConnection?.let { hub ->
                if (hub.connectionState == HubConnectionState.CONNECTED) {
                    hub.stop().blockingAwait()
                }
            }
        }
    }

    fun joinSession(sessionId: String) {
        hubConnection?.invoke("JoinSession", sessionId)?.blockingAwait()
    }

    fun leaveSession(sessionId: String) {
        hubConnection?.invoke("LeaveSession", sessionId)?.blockingAwait()
    }

    fun onParticipantJoined(handler: (JsonObject) -> Unit) {
        hubConnection?.on("ParticipantJoined", handler, JsonObject::class.java)
    }

    fun onParticipantLeft(handler: (JsonObject) -> Unit) {
        hubConnection?.on("ParticipantLeft", handler, JsonObject::class.java)
    }

    fun onStoryAdded(handler: (JsonObject) -> Unit) {
        hubConnection?.on("StoryAdded", handler, JsonObject::class.java)
    }

    fun onStoryStatusChanged(handler: (JsonObject) -> Unit) {
        hubConnection?.on("StoryStatusChanged", handler, JsonObject::class.java)
    }

    fun onVoteSubmitted(handler: (JsonObject) -> Unit) {
        hubConnection?.on("VoteSubmitted", handler, JsonObject::class.java)
    }
}