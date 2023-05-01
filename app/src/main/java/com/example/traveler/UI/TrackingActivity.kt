package com.example.traveler.UI

import android.Manifest
import android.app.PendingIntent
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.traveler.*
import com.example.traveler.Constants.ACTION_PAUSE_SERVICE
import com.example.traveler.Constants.ACTION_START_OR_RESUME_SERVICE
import com.example.traveler.Constants.ACTION_STOP_SERVICE
import com.example.traveler.Constants.MAP_ZOOM
import com.example.traveler.Constants.POLYLINE_COLOR
import com.example.traveler.Constants.POLYLINE_WIDTH
import com.example.traveler.DB.Track
import com.example.traveler.ViewModels.MainViewModel
import com.example.traveler.databinding.ActivityTrackingBinding
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.MapView
import com.google.android.gms.maps.model.LatLngBounds
import com.google.android.gms.maps.model.PolylineOptions
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import dagger.hilt.android.AndroidEntryPoint
import pub.devrel.easypermissions.AppSettingsDialog
import pub.devrel.easypermissions.EasyPermissions
import java.util.Calendar
import kotlin.math.round


@Suppress("DEPRECATION")
@AndroidEntryPoint
class TrackingActivity : AppCompatActivity(), EasyPermissions.PermissionCallbacks {
    private lateinit var viewModel: MainViewModel

    private var map: GoogleMap? = null
    private lateinit var binding: ActivityTrackingBinding
    private lateinit var mapView: MapView
    private lateinit var btnToggleTrack: Button
    private lateinit var btnFinishTrack: Button
    private lateinit var tvTimer: TextView
    private var isTracking = false
    private var pathPoints = mutableListOf<Polyline>()
    private var currentTimeInMillis = 0L
    private lateinit var miCancelTracking: Button


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTrackingBinding.inflate(layoutInflater)
        setContentView(binding.root)
        mapView = binding.mapView
        mapView.onCreate(savedInstanceState)
        btnToggleTrack = findViewById(R.id.btnToggleTrack)
        btnFinishTrack = findViewById(R.id.btnFinishTrack)
        tvTimer = findViewById(R.id.tvTimer)
        miCancelTracking = findViewById(R.id.miCancelTracking)
        miCancelTracking.isVisible = false

        requestPermissions()

        mapView.getMapAsync {
            map = it
            addAllPolylines()
        }

        findViewById<Button>(R.id.miCancelTracking).setOnClickListener {
            showCancelDialog()
        }
        findViewById<Button>(R.id.btnToggleTrack).setOnClickListener {
            toggleTrack()
        }
        findViewById<Button>(R.id.btnFinishTrack).setOnClickListener {
            zoomToSeeWholeTrack()
            endTrackAndSaveToDb()
            val intent = Intent(this, MapActivity::class.java)
            startActivity(intent)
        }


        subscribeToObservers()

            // Initialize and assign variable
            val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottomNavigationView)
            // Set Home selected
            bottomNavigationView.selectedItemId = R.id.track

            // Perform item selected listener
            bottomNavigationView.setOnNavigationItemSelectedListener(BottomNavigationView.OnNavigationItemSelectedListener { item ->
                when (item.itemId) {
                    R.id.statistics -> {
                        val a = Intent(applicationContext, StatisticsActivity::class.java)
                        startActivity(a)
                        finish()
                        overridePendingTransition(0, 0)
                        return@OnNavigationItemSelectedListener true
                    }
                    R.id.activities -> {
                        val a = Intent(applicationContext, MapActivity::class.java)
                        startActivity(a)
                        finish()
                        overridePendingTransition(0, 0)
                        return@OnNavigationItemSelectedListener true
                    }
                    R.id.track -> return@OnNavigationItemSelectedListener true
                    R.id.feed -> {
                        val b = Intent(applicationContext, FeedActivity::class.java)
                        startActivity(b)
                        finish()
                        overridePendingTransition(0, 0)
                        return@OnNavigationItemSelectedListener true
                    }


                }
                false
            })

        }


    private fun showCancelDialog(){
        val dialog = MaterialAlertDialogBuilder(this, com.airbnb.lottie.R.style.AlertDialog_AppCompat)
            .setTitle("Cancel the track?")
            .setMessage("Are you sure you want to cancel the current track?")
            .setIcon(R.drawable.cancel)
            .setPositiveButton("Yes"){
                _,_ ->
                stopTrack()
            }
            .setNegativeButton("No"){
                dialogInterface, _ ->
                dialogInterface.cancel()
            }
            .create()
        dialog.show()
    }

    private fun stopTrack(){
        sendCommandToService(ACTION_STOP_SERVICE)
        miCancelTracking.isVisible = false
        val intent = Intent(this, MapActivity::class.java)
        startActivity(intent)

    }
    private fun subscribeToObservers(){
        TrackingService.isTracking.observe(this, Observer {
            updateTracking(it)
        })
        TrackingService.pathPoints.observe(this, Observer {
            pathPoints = it
            addLatestPolyline()
            moveCameraToUser()
        })

        TrackingService.timeTrackInMillis.observe(this, Observer {
            currentTimeInMillis = it
            val formattedTime = TrackingUtility.getFormattedStopWatchTime(currentTimeInMillis, true)
            tvTimer.text = formattedTime
        })
    }

    private fun toggleTrack(){
        if(isTracking){
           miCancelTracking.isVisible = true
            sendCommandToService(ACTION_PAUSE_SERVICE)
        } else{
            sendCommandToService(ACTION_START_OR_RESUME_SERVICE)
        }
    }
    private fun updateTracking(isTracking: Boolean){
        this.isTracking = isTracking
        if(!isTracking){

            btnToggleTrack.text = buildString {
        append("Start")
    }
            btnFinishTrack.visibility = View.VISIBLE
        }else {
            btnToggleTrack.text = buildString {
        append("Pause")
    }
            btnFinishTrack.visibility = View.GONE
            miCancelTracking.isVisible = true
        }
    }
    private fun moveCameraToUser(){
        if(pathPoints.isNotEmpty() && pathPoints.last().isNotEmpty()){
            map?.animateCamera(
                CameraUpdateFactory.newLatLngZoom(
                    pathPoints.last().last(),
                    MAP_ZOOM)
            )
        }
    }

    private fun zoomToSeeWholeTrack(){
        val bounds = LatLngBounds.Builder()
        for(polyline in pathPoints){
            for(pos in polyline){
                bounds.include(pos)
            }
        }
        map?.moveCamera(
            CameraUpdateFactory.newLatLngBounds(
                bounds.build(),
                mapView.width,
                mapView.height,
                (mapView.height * 0.05f).toInt()
            )
        )
    }

    private fun endTrackAndSaveToDb(){
        map?.snapshot { bmp ->
            var distanceInMeters = 0
            for(polyline in pathPoints){
                distanceInMeters += TrackingUtility.calculatePolyLineLength(polyline).toInt()
            }
            // divided by 1000 to have the number in km/h
            val avgSpeed = round((distanceInMeters / 1000f) / (currentTimeInMillis / 1000f / 60 / 60) * 10) / 10f
            val dateTimestamp = Calendar.getInstance().timeInMillis
            val track = Track(bmp, dateTimestamp, avgSpeed, distanceInMeters, currentTimeInMillis)
            viewModel.insertTrack(track)
            Toast.makeText(
                baseContext,
                "Track saved successfully",
                Toast.LENGTH_SHORT
            ).show()
            stopTrack()
            }
        }


    private fun addAllPolylines(){
        for(polyline in pathPoints){
            val polylineOptions = PolylineOptions()
                .color(POLYLINE_COLOR)
                .width(POLYLINE_WIDTH)
                .addAll(polyline)
            map?.addPolyline(polylineOptions)
        }
    }

    private fun addLatestPolyline(){
        if(pathPoints.isNotEmpty() && pathPoints.last().size > 1){
            val preLastLatLng = pathPoints.last()[pathPoints.last().size - 2]
            val lastLatLng = pathPoints.last().last()
            val polylineOptions = PolylineOptions()
                .color(POLYLINE_COLOR)
                .width(POLYLINE_WIDTH)
                .add(preLastLatLng)
                .add(lastLatLng)
            map?.addPolyline(polylineOptions)
        }
    }

    private fun requestPermissions() {
        if(TrackingUtility.hasLocationPermission(this)) {
            return
        }
        if(Build.VERSION.SDK_INT < Build.VERSION_CODES.Q) {
            EasyPermissions.requestPermissions(
                this,
                "You need to accept location permissions to use this app.",
                Constants.REQUEST_CODE_LOCATION_PERMISSION,
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION
            )
        } else {
            EasyPermissions.requestPermissions(
                this,
                "You need to accept location permissions to use this app.",
                Constants.REQUEST_CODE_LOCATION_PERMISSION,
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_BACKGROUND_LOCATION
            )
        }
    }

    override fun onPermissionsDenied(requestCode: Int, perms: MutableList<String>) {
        if(EasyPermissions.somePermissionPermanentlyDenied(this, perms)) {
            AppSettingsDialog.Builder(this).build().show()
        } else {
            requestPermissions()
        }
    }

    override fun onPermissionsGranted(requestCode: Int, perms: MutableList<String>) {}

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this)
    }
    private fun sendCommandToService(action: String) {
        val intent = Intent(this, TrackingService::class.java).apply {
            this.action = action
            startService(this)

        }
        val pendingIntent = PendingIntent.getService(
            this,
            0,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
        try {
            pendingIntent.send()
        } catch (e: PendingIntent.CanceledException) {
            e.printStackTrace()
        }
    }



    override fun onResume() {
        super.onResume()
        mapView.onResume()
    }

    override fun onStart() {
        super.onStart()
        mapView.onStart()
    }

    override fun onStop() {
        super.onStop()
        mapView.onStop()
    }

    override fun onPause() {
        super.onPause()
        mapView.onPause()
    }

    override fun onLowMemory() {
        super.onLowMemory()
        mapView.onLowMemory()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        mapView.onSaveInstanceState(outState)
    }

}


