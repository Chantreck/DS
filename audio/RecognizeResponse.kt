package com.example.testmic.audio

import kotlinx.serialization.Serializable

@Serializable
data class RecognizeResponse(
    val result: String,
)
