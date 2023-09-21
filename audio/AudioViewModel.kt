package com.example.testmic.audio

import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

class AudioViewModel : ViewModel() {

    private val interactor = AudioInteractor()
    val isRecording = MutableLiveData<Boolean>()
    val result = MutableLiveData<String>()

    fun startRecording(context: Context) {
        interactor.startRecording(context)
        isRecording.value = true
    }

    fun stopRecording(context: Context, lang: String) {
        isRecording.value = false
        viewModelScope.launch {
            result.value = interactor.stopRecording(context, lang)
        }
    }
}