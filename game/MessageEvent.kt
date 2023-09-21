package com.example.testmic.game

import kotlinx.serialization.Serializable

@Serializable
data class MessageEvent(
    val userId: String,
    val text: String,
)
