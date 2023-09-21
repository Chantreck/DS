package com.example.testmic.audio

import android.animation.ValueAnimator
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.example.testmic.R
import com.example.testmic.databinding.ActivityAudioBinding

class AudioActivity : AppCompatActivity() {
    private val binding by lazy { ActivityAudioBinding.inflate(layoutInflater) }
    private val viewModel = AudioViewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        binding.startRecording.setOnClickListener {
            if (viewModel.isRecording.value == true) {
                return@setOnClickListener
            }

            var animator = ValueAnimator.ofFloat(1.0f, 1.2f)
            animator.addUpdateListener {
                binding.mic.scaleX = it.animatedValue as Float
                binding.mic.scaleY = it.animatedValue as Float
            }
            animator.repeatCount = ValueAnimator.INFINITE
            animator.repeatMode = ValueAnimator.REVERSE
            animator.start()

            viewModel.startRecording(this@AudioActivity)
            binding.mic.visibility = View.VISIBLE
        }

        binding.mic.setOnClickListener {
            viewModel.stopRecording(this, "en-US")
            binding.mic.visibility = View.INVISIBLE
        }

        viewModel.result.observe(this) {
            binding.result.text = it
        }
    }
}