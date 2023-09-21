package com.example.testmic.audio

import android.content.Context

class AudioInteractor {

    private val repository = AudioRepository()

    fun startRecording(context: Context) {
        repository.startRecording(context)
    }

    suspend fun stopRecording(context: Context, lang: String): String {
        return repository.stopRecording(context, lang)
    }
}