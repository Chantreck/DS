package com.example.testmic.game

import kotlinx.serialization.Serializable

@Serializable
data class Room(
    val id: Int,
    val roomId: String,
)
