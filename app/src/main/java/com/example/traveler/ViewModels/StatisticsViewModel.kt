package com.example.traveler.ViewModels

import androidx.lifecycle.ViewModel
import com.example.traveler.MainRepository
import javax.inject.Inject

class StatisticsViewModel @Inject constructor(
    val mainRepository: MainRepository
):ViewModel() {

    val totalTimeTrack = mainRepository.getTotalTimeInMillis()
    val totalDistance = mainRepository.getTotalDistance()
    val totalAvgSpeed = mainRepository.getTotalAvgSpeed()
}