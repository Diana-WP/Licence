package com.example.traveler.ViewModels

import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.traveler.DB.Track
import com.example.traveler.MainRepository
import com.example.traveler.SortType
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    val mainRepository: MainRepository
):ViewModel() {

    private val tracksSortedByDate = mainRepository.getAllTracksSortedByDate()
    private val tracksSortedByDistance = mainRepository.getAllTracksSortedByDistance()
    private val tracksSortedByTime = mainRepository.getAllTracksSortedByTime()
    private val tracksSortedByAvgSpeed = mainRepository.getAllTracksSortedByAvgSpeed()

    val tracks = MediatorLiveData<List<Track>>()

    var sortType = SortType.DATE

    init {
        tracks.addSource(tracksSortedByDate) { result ->
            if (sortType == SortType.DATE) {
                result?.let { tracks.value = it }
            }
        }
        tracks.addSource(tracksSortedByTime) { result ->
            if (sortType == SortType.TRACKING_TIME) {
                result?.let { tracks.value = it }
            }
        }
        tracks.addSource(tracksSortedByAvgSpeed) { result ->
            if (sortType == SortType.AVG_SPEED) {
                result?.let { tracks.value = it }
            }
        }
        tracks.addSource(tracksSortedByDistance) { result ->
            if (sortType == SortType.DISTANCE) {
                result?.let { tracks.value = it }
            }
        }
    }

    fun sortTracks(sortType: SortType) = when (sortType) {
        SortType.DATE -> tracksSortedByDate.value?.let { tracks.value = it }
        SortType.TRACKING_TIME -> tracksSortedByTime.value?.let { tracks.value = it }
        SortType.DISTANCE -> tracksSortedByDistance.value?.let { tracks.value = it }
        SortType.AVG_SPEED -> tracksSortedByAvgSpeed.value?.let { tracks.value = it }

    }.also {
        this.sortType = sortType
    }

    fun insertTrack(track: Track) = viewModelScope.launch {
        mainRepository.insertTrack(track)
    }

    fun deleteTrack(track: Track) {
        viewModelScope.launch {
            mainRepository.deleteTrack(track)
        }
    }
}