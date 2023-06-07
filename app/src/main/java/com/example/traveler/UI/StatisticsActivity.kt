package com.example.traveler.UI

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.activity.viewModels
import androidx.lifecycle.Observer
import com.example.traveler.*
import com.example.traveler.ViewModels.StatisticsViewModel
import com.example.traveler.databinding.ActivityStatisticsBinding
import com.github.mikephil.charting.charts.LineChart
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.ktx.Firebase
import dagger.hilt.android.AndroidEntryPoint
import java.lang.Math.round

@Suppress("DEPRECATION")
@AndroidEntryPoint
class StatisticsActivity : AppCompatActivity() {
    private lateinit var binding: ActivityStatisticsBinding
    private lateinit var prefManager: PrefManager
    private lateinit var database: DatabaseReference
    private lateinit var lineChart: LineChart
    private lateinit var tvTotalDistance: TextView
    private lateinit var tvTotalTime: TextView
    private lateinit var tvAverageSpeed: TextView
    private val viewModel: StatisticsViewModel by viewModels()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityStatisticsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        prefManager = PrefManager(this)
        lineChart = findViewById(R.id.lineChart)
        tvTotalTime = findViewById(R.id.tvTotalTime) // add this line to initialize tvTotalTime
        tvTotalDistance = findViewById(R.id.tvTotalDistance) // add this line to initialize tvTotalDistance
        tvAverageSpeed = findViewById(R.id.tvAverageSpeed) // add this line to initialize tvAverageSpeed

        subscribeToObservers()

        // Initialize and assign variable
        val bottomNavigationView = findViewById<BottomNavigationView>(com.example.traveler.R.id.bottomNavigationView)

        binding.buttonRead.setOnClickListener {readData()}
        // Set Home selected
        bottomNavigationView.selectedItemId = R.id.statistics
        // Perform item selected listener
        bottomNavigationView.setOnNavigationItemSelectedListener(BottomNavigationView.OnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.feed -> {
                    val a = Intent(applicationContext, FeedActivity::class.java)
                    startActivity(a)
                    finish()
                    overridePendingTransition(0, 0)
                    return@OnNavigationItemSelectedListener true
                }
                R.id.statistics -> return@OnNavigationItemSelectedListener true
                R.id.activities -> {
                    val b = Intent(applicationContext, MapActivity::class.java)
                    startActivity(b)
                    finish()
                    overridePendingTransition(0, 0)
                    return@OnNavigationItemSelectedListener true
                }
                R.id.track -> {
                    val b = Intent(applicationContext, TrackingActivity::class.java)
                    startActivity(b)
                    finish()
                    overridePendingTransition(0, 0)
                    return@OnNavigationItemSelectedListener true
                }

            }
            false
        })
    }


    private fun readData(){
        database = FirebaseDatabase.getInstance().getReference("FirebaseTraveler")
        database.child("Temperature").get().addOnSuccessListener {
            if (it.exists()){
                val temperature:Float = it.value.toString().toFloat()
                Toast.makeText(this,"Successful sensors read", Toast.LENGTH_SHORT).show()
                binding.tvAverageTemperature.setText(temperature.toString())
            } else{
                Toast.makeText(this,"Sensor path does not exist", Toast.LENGTH_SHORT).show()
            }}
            .addOnFailureListener{
                Toast.makeText(this,"FAILED", Toast.LENGTH_SHORT).show()
            }
        database.child("Humidity").get().addOnSuccessListener {
            if (it.exists()){
                val humidity:Float = it.value.toString().toFloat()

                binding.tvAverageHumidity.setText(humidity.toString())
            } else{
                Toast.makeText(this,"Sensor path does not exist", Toast.LENGTH_SHORT).show()
            }}
            .addOnFailureListener{
                Toast.makeText(this,"FAILED", Toast.LENGTH_SHORT).show()
            }
        database.child("Air Quality").get().addOnSuccessListener {
            if (it.exists()){
                val airQuality:Float = it.value.toString().toFloat()
                binding.tvAverageQuality.setText(airQuality.toString())
            } else{
                Toast.makeText(this,"Sensor path does not exist", Toast.LENGTH_SHORT).show()
            }}
            .addOnFailureListener{
                Toast.makeText(this,"FAILED", Toast.LENGTH_SHORT).show()
            }
    }
    private fun subscribeToObservers() {
        viewModel.totalTimeTrack.observe(this, Observer {
            it?.let {
                val totalTimeTrack = TrackingUtility.getFormattedStopWatchTime(it)
                tvTotalTime.text = totalTimeTrack
            }
        })
        viewModel.totalDistance.observe(this, Observer {
            it?.let {
                val km = it / 1000f
                val totalDistance = round(km * 10f) / 10f
                val totalDistanceString = "${totalDistance}km"
                tvTotalDistance.text = totalDistanceString
            }
        })
        viewModel.totalAvgSpeed.observe(this, Observer {
            it?.let {
                val avgSpeed = round(it * 10f) / 10f
                val avgSpeedString = "${avgSpeed}km/h"
                tvAverageSpeed.text = avgSpeedString
            }
        })

    }
    fun clickLogout(view: View){
        prefManager.removeData()
        Firebase.auth.signOut()
        Toast.makeText(this, "You logged out", Toast.LENGTH_LONG).show()
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()

    }

}