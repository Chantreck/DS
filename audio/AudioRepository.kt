package com.example.testmic.audio

import android.content.Context
import android.media.MediaRecorder
import android.os.Environment
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.engine.okhttp.OkHttp
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.logging.ANDROID
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.client.request.headers
import io.ktor.client.request.parameter
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.HttpHeaders
import io.ktor.serialization.kotlinx.json.json
import io.ktor.util.cio.readChannel
import kotlinx.serialization.json.Json
import java.io.File
import java.io.IOException

class AudioRepository {
    private val fileName = "audio.ogg"
    private var file: File? = null
    private var mediaRecorder: MediaRecorder? = null

    private val client = HttpClient(OkHttp) {
        install(Logging) {
            logger = Logger.ANDROID
            level = LogLevel.ALL
        }
        install(ContentNegotiation) {
            json(Json {
                ignoreUnknownKeys = true
                prettyPrint = true
            })
        }
    }

    fun startRecording(context: Context) {
        if (mediaRecorder == null) {
            mediaRecorder = MediaRecorder().apply {
                setAudioSource(MediaRecorder.AudioSource.MIC)
                setOutputFormat(MediaRecorder.OutputFormat.OGG)
                setAudioEncoder(MediaRecorder.AudioEncoder.OPUS)
                setOutputFile(getFile(context).absolutePath)
            }
        }

        try {
            mediaRecorder!!.apply {
                prepare()
                start()
            }
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    suspend fun stopRecording(context: Context, lang: String): String {
        mediaRecorder?.let {
            it.stop()
            it.release()
            mediaRecorder = null
        }

        val file = getFile(context)

        val response = client.post("https://stt.api.cloud.yandex.net/speech/v1/stt:recognize") {
            parameter("folderId", "b1gnv94u104kldrtrgbu")
            parameter("lang", lang)
            headers {
                append(
                    HttpHeaders.Authorization,
                    "Bearer t1.9euelZqOx8qZms3Ky46eyp2UmYyVke3rnpWaj8_Hjc3Kls-aisqUx56Lmozl8_dZK1NX-e9zQylu_N3z9xlaUFf573NDKW78zef1656Vms2VyZbGyZWLj53JmMqSzcjG7_zF656Vms2VyZbGyZWLj53JmMqSzcjG.z5rMkSRvZ3djYvID1nKwTYF60SyKucHjdeBM-SJZNjnAPfRQnnjt0-uTm1SY_X4Zpj2QpiNcpHcDPdBmzKO-Dw"
                )
            }
            setBody(file.readChannel())
        }.body<RecognizeResponse>()

        return response.result
    }

    private fun getFile(context: Context): File {
        if (file == null) {
            createFile(context)
        }
        return file!!
    }

    private fun createFile(context: Context) {
        val dir = context.getExternalFilesDir(Environment.DIRECTORY_MUSIC)
        file = File(dir, fileName)
        file!!.createNewFile()
    }
}