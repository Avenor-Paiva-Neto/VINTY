package io.android.data.analytics.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "analytics_events")
data class AnalyticsEventEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val animeId: String,
    val tags: String, // Salvaremos como String separada por vírgulas
    val weight: Int,
    val timestamp: Long = System.currentTimeMillis(),
    val isSynced: Boolean = false
)