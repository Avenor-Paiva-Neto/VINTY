package io.android.domain.player.model

sealed class PlaybackState {
    object Idle : PlaybackState()
    object Buffering : PlaybackState()
    data class Playing(val currentPosition: Long, val duration: Long) : PlaybackState()
    object Paused : PlaybackState()
    object Ended : PlaybackState()
    data class Error(val message: String) : PlaybackState()
}