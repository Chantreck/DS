package com.example.testmic.game

import android.util.Log
import com.example.testmic.gotrue
import com.example.testmic.postgrest
import com.example.testmic.realtime
import io.github.jan.supabase.gotrue.providers.builtin.Email
import io.github.jan.supabase.realtime.RealtimeChannel
import io.github.jan.supabase.realtime.broadcast
import io.github.jan.supabase.realtime.broadcastFlow
import io.github.jan.supabase.realtime.createChannel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class GameRepository {

    private var channel: RealtimeChannel? = null
    private var oppositeUserId: String? = null

    suspend fun getAllGames(): List<Room> {
        return postgrest["rooms"].select().decodeList()
    }

    suspend fun createGame() {
        val userId = getUserId()

        postgrest["rooms"].insert(
            NewRoom(roomId = userId),
            upsert = true,
            onConflict = "roomId",
        )
        channel = realtime.createChannel(channelId = userId)
        channel!!.join(true)

        coroutineScope {
            launch(Dispatchers.IO) {
                channel!!.broadcastFlow<JoinEvent>("Joined").collectLatest {
                    postgrest["rooms"].delete { eq("roomId", userId) }
                    oppositeUserId = it.userId
                    Log.i("JoinEvent", "User ${oppositeUserId} joined - callback in createGame")

                    channel!!.broadcast("Joined", JoinEvent(userId))
                }
            }

            launch(Dispatchers.IO) {
                channel!!.broadcastFlow<MessageEvent>("Message").collectLatest {
                    Log.i("MessageEvent", "Received message: ${it.text} - callback in createGame")
                }
            }
        }
    }

    suspend fun joinGame(channelId: String) {
        channel = realtime.createChannel(channelId)
        channel!!.join(true)
        channel!!.broadcast("Joined", JoinEvent(getUserId()))

        coroutineScope {
            launch(Dispatchers.IO) {
                channel!!.broadcastFlow<JoinEvent>("Joined").collectLatest {
                    oppositeUserId = it.userId
                    Log.i("JoinEvent", "User ${oppositeUserId} created channel - callback in joinGame")
                }
            }

            launch(Dispatchers.IO) {
                channel!!.broadcastFlow<MessageEvent>("Message").collectLatest {
                    Log.i("MessageEvent", "Received message: ${it.text} - callback in joinGame")
                }
            }
        }
    }

    suspend fun login(email: String, password: String) {
        gotrue.loginWith(Email) {
            this.email = email
            this.password = password
        }
    }

    suspend fun sendMessage(message: String) {
        channel!!.broadcast(
            "Message", MessageEvent(
                userId = getUserId(),
                text = message,
            )
        )
    }

    fun getUserId(): String {
        return gotrue.currentUserOrNull()?.id!!
    }
}