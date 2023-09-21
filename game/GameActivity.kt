package com.example.testmic.game

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.testmic.databinding.ActivityGameBinding
import com.example.testmic.postgrest
import com.example.testmic.realtime
import io.github.jan.supabase.realtime.RealtimeChannel
import io.github.jan.supabase.realtime.broadcast
import io.github.jan.supabase.realtime.broadcastFlow
import io.github.jan.supabase.realtime.createChannel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import splitties.toast.toast

class GameActivity : AppCompatActivity() {
    private val binding by lazy { ActivityGameBinding.inflate(layoutInflater) }
    private val viewModel = GameViewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        lifecycleScope.launch {
            realtime.connect()
        }

        binding.getAll.setOnClickListener {
            viewModel.findGame()
        }

        binding.create.setOnClickListener {
            viewModel.createGame()
        }

        binding.send.setOnClickListener {
            val text = binding.text.text.toString()
            viewModel.sendMessage(text)
        }

        binding.login.setOnClickListener {
            val email = binding.email.text.toString()
            val password = binding.password.text.toString()
            viewModel.login(email, password)
        }
    }
}