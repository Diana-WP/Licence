package com.example.traveler.UI

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.view.WindowManager
import com.example.traveler.R
import androidx.appcompat.app.AppCompatActivity
import com.example.traveler.Constants


@SuppressLint("CustomSplashScreen")
@Suppress("DEPRECATION")
class SplashScreen : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        navigateToTrackingIfNeeded(intent)

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)
        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )

        Handler().postDelayed({
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }, 3000)
    }
    override fun onNewIntent(intent: Intent?){
        super.onNewIntent(intent)
        navigateToTrackingIfNeeded(intent)
    }
    private fun navigateToTrackingIfNeeded(intent:Intent?){
        if(intent?.action == Constants.ACTION_SHOW_TRACKING){
            startActivity(intent)
        }
    }
}