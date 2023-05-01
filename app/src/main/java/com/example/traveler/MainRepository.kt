package com.example.traveler

import com.example.traveler.DB.Track
import com.example.traveler.DB.TrackDAO
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

class MainRepository @Inject constructor(private val trackDao: TrackDAO) {
    suspend fun insertTrack(track: Track) = trackDao.insertTrack(track)

    suspend fun deleteTrack(track: Track) = trackDao.deleteTrack(track)

    fun getAllTracksSortedByDate() = trackDao.getAllTracksSortedByDate()
    fun getAllTracksSortedByDistance() = trackDao.getAllTracksSortedByDistance()
    fun getAllTracksSortedByTime() = trackDao.getAllTracksSortedByTime()
    fun getAllTracksSortedByAvgSpeed() = trackDao.getAllTracksSortedBySpeed()

    fun getTotalAvgSpeed() = trackDao.getTotalAvgSpeed()
    fun getTotalDistance() = trackDao.getTotalDistance()
    fun getTotalTimeInMillis() = trackDao.getTotalTimeInMillis()

}