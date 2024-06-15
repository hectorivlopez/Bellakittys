package com.movil.bellakkitys

import android.content.Context
import android.media.MediaPlayer

object MediaPlayerSingleton {
    private var mediaPlayer = MediaPlayer()

    // Methods to control the MediaPlayer's state
    fun getInstance(): MediaPlayer {
        if (mediaPlayer == null) {
            mediaPlayer = MediaPlayer()
        }
        return mediaPlayer!!
    }

    fun play(context: Context, resourceId: Int, listener: MediaPlayerPreparedListener) {
        mediaPlayer?.apply {
            reset()
            val assetFileDescriptor = context.resources.openRawResourceFd(resourceId)
            if (assetFileDescriptor != null) {
                setDataSource(assetFileDescriptor.fileDescriptor, assetFileDescriptor.startOffset, assetFileDescriptor.length)
                assetFileDescriptor.close()
                prepareAsync()
                setOnPreparedListener {
                    start()
                    listener.onMediaPlayerPrepared(it.duration)
                }
            }
        }
    }

    fun pause() {
        mediaPlayer?.pause()
    }

    fun resume() {
        mediaPlayer?.start()
    }

    fun stop() {
        mediaPlayer?.stop()
    }

    fun getCurrentPosition(): Int {
        return mediaPlayer?.currentPosition ?: 0
    }

    fun seekTo(position: Int) {
        mediaPlayer?.seekTo(position)
    }

    fun getDuration(): Int? {
        return mediaPlayer?.duration
    }
}