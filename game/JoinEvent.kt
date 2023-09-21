package com.example.testmic.game

import kotlinx.serialization.Serializable

@Serializable
data class JoinEvent(
    val userId: String,
)
