package com.ahmetsirim.chat

import kotlinx.serialization.Serializable

@Serializable
data class ChatRoute(val sessionId: String? = null)