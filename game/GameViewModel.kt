package com.example.testmic.game

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class GameViewModel : ViewModel() {
    private val repository = GameRepository()
    private val coroutineContext = Dispatchers.IO

    fun findGame() {
        viewModelScope.launch(coroutineContext) {
            val games = repository.getAllGames()
            val userId = repository.getUserId()
            val otherRoom = games.find { it.roomId != userId } ?: return@launch
            repository.joinGame(otherRoom.roomId)
        }
    }

    fun createGame() {
        viewModelScope.launch(coroutineContext) {
            repository.createGame()
        }
    }

    fun login(email: String, password: String) {
        viewModelScope.launch(coroutineContext) {
            repository.login(email, password)
        }
    }

    fun sendMessage(message: String) {
        viewModelScope.launch(coroutineContext) {
            repository.sendMessage(message)
        }
    }
}