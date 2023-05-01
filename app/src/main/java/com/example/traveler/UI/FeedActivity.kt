package com.example.traveler.UI

import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.traveler.R
import com.example.traveler.databinding.ActivityFeedBinding
import com.google.android.material.bottomnavigation.BottomNavigationView


@Suppress("DEPRECATION")
class FeedActivity : AppCompatActivity() {
        private var pickedPhoto : Uri? = null
        private var pickedBitMap : Bitmap? = null
        private lateinit var imageView: ImageView
        private lateinit var binding: ActivityFeedBinding
        override fun onCreate(savedInstanceState: Bundle?) {
            super.onCreate(savedInstanceState)
            binding = ActivityFeedBinding.inflate(layoutInflater)
            setContentView(binding.root) // add this line to set the layout
            imageView = findViewById(R.id.imageView)
            // Initialize and assign variable
            val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottomNavigationView)
            // Set Home selected
            bottomNavigationView.selectedItemId = R.id.feed

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
                    R.id.feed -> return@OnNavigationItemSelectedListener true
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

        fun pickedPhoto(view: View){
            if(ContextCompat.checkSelfPermission(this, android.Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(
                    this, arrayOf(android.Manifest.permission.READ_EXTERNAL_STORAGE),
                    1
                )
            }else {
                val galleryIntext = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
                startActivityForResult(galleryIntext,2 )
            }
        }

        override fun onRequestPermissionsResult(
            requestCode: Int,
            permissions: Array<out String>,
            grantResults: IntArray
        ) {
            if(grantResults.size>0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                val galeriIntext = Intent(Intent.ACTION_PICK,MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
                startActivityForResult(galeriIntext, 2)
            }
            super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        }

        @Deprecated("Deprecated in Java")
        override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
            if (requestCode == 2 && resultCode == Activity.RESULT_OK && data != null){
                pickedPhoto = data.data
                if (Build.VERSION.SDK_INT >=28) {
                    val source = ImageDecoder.createSource(this.contentResolver, pickedPhoto!!)
                    pickedBitMap =ImageDecoder.decodeBitmap(source)
                    imageView.setImageBitmap(pickedBitMap)
                }
                else {
                    pickedBitMap = MediaStore.Images.Media.getBitmap(this.contentResolver, pickedPhoto)
                    imageView.setImageBitmap(pickedBitMap)
                }
            }
            super.onActivityResult(requestCode, resultCode, data)
        }

}