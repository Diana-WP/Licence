package com.example.traveler.DB

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.lifecycle.LiveData
import androidx.room.*
import dagger.Provides

@Dao
interface TrackDAO {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTrack(track: Track)

    @Delete
    suspend fun deleteTrack(track: Track)

    @Query("SELECT * FROM track_table ORDER BY timestamp DESC")
    fun getAllTracksSortedByDate(): LiveData<List<Track>>

    @Query("SELECT * FROM track_table ORDER BY avgSpeedInKMH DESC")
    fun getAllTracksSortedBySpeed(): LiveData<List<Track>>

    @Query("SELECT * FROM track_table ORDER BY distanceInMeters DESC")
    fun getAllTracksSortedByDistance(): LiveData<List<Track>>

    @Query("SELECT * FROM track_table ORDER BY timeInMillis DESC")
    fun getAllTracksSortedByTime(): LiveData<List<Track>>

    @Query("SELECT SUM(timeInMillis) FROM track_table")
    fun getTotalTimeInMillis(): LiveData<Long>

    @Query("SELECT AVG(avgSpeedInKMH) FROM track_table")
    fun getTotalAvgSpeed(): LiveData<Float>

    @Query("SELECT SUM(distanceInMeters) FROM track_table")
    fun getTotalDistance(): LiveData<Int>


}