package com.example.traveler.UI

import android.Manifest
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.Spinner
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.traveler.*
import com.example.traveler.DB.Track
import dagger.hilt.android.AndroidEntryPoint
import com.example.traveler.ViewModels.MainViewModel
import com.example.traveler.adaptors.TrackAdapter
import com.example.traveler.databinding.ActivityMapBinding
import com.google.android.material.bottomnavigation.BottomNavigationView
import pub.devrel.easypermissions.AppSettingsDialog
import pub.devrel.easypermissions.EasyPermissions

@Suppress("DEPRECATION")
@AndroidEntryPoint
class MapActivity : AppCompatActivity(), EasyPermissions.PermissionCallbacks  {
    private val viewModel: MainViewModel by viewModels()

    private lateinit var binding: ActivityMapBinding
    private lateinit var prefManager: PrefManager
    private lateinit var rvTracks: RecyclerView
    private lateinit var trackAdapter: TrackAdapter
    private lateinit var spFilter: Spinner
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMapBinding.inflate(layoutInflater)
        setContentView(binding.root)
        prefManager = PrefManager(this)

        rvTracks = findViewById(R.id.rvTracks)
        setupRecyclerView()

        when(viewModel.sortType){
            SortType.DATE -> spFilter.setSelection(0)
            SortType.TRACKING_TIME -> spFilter.setSelection(1)
            SortType.DISTANCE -> spFilter.setSelection(2)
            SortType.AVG_SPEED -> spFilter.setSelection(3)
        }

        spFilter.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onNothingSelected(parent: AdapterView<*>?) {}

            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                when(position){
                    0 -> viewModel.sortTracks(SortType.DATE)
                    1 -> viewModel.sortTracks(SortType.TRACKING_TIME)
                    2 -> viewModel.sortTracks(SortType.DISTANCE)
                    3 -> viewModel.sortTracks(SortType.AVG_SPEED)
                }
            }
        }
        viewModel.tracks.observe(this, Observer {
            trackAdapter.submitList(it)
        })
        // Initialize and assign variable
        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottomNavigationView)
        // Set Home selected
        bottomNavigationView.selectedItemId = R.id.activities

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
                R.id.track -> {
                    val a = Intent(applicationContext, TrackingActivity::class.java)
                    startActivity(a)
                    finish()
                    overridePendingTransition(0, 0)
                    return@OnNavigationItemSelectedListener true
                }
                R.id.activities -> return@OnNavigationItemSelectedListener true
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


    private fun setupRecyclerView() {
        trackAdapter = TrackAdapter()
        rvTracks.adapter = trackAdapter
        rvTracks.layoutManager = LinearLayoutManager(this)
    }
}
