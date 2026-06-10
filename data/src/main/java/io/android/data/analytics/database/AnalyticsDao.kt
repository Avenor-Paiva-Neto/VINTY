package io.android.data.analytics.database

import androidx.room.*
import io.android.data.analytics.model.AnalyticsEventEntity

@Dao
interface AnalyticsDao {
    @Insert
    suspend fun insertEvent(event: AnalyticsEventEntity)

    @Query("SELECT * FROM analytics_events WHERE isSynced = 0")
    suspend fun getUnsyncedEvents(): List<AnalyticsEventEntity>

    @Query("DELETE FROM analytics_events WHERE isSynced = 1")
    suspend fun clearSyncedEvents()

    @Update
    suspend fun markAsSynced(events: List<AnalyticsEventEntity>)
}