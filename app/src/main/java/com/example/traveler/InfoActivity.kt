package com.example.traveler
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.example.traveler.databinding.ActivityInfoBinding
import com.google.android.material.bottomnavigation.BottomNavigationView
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
@Suppress("DEPRECATION")

class InfoActivity : AppCompatActivity() {
    private lateinit var binding: ActivityInfoBinding
    private lateinit var prefManager: PrefManager
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityInfoBinding.inflate(layoutInflater)
        setContentView(binding.root)
        prefManager = PrefManager(this)
        // Initialize and assign variable
        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottom_navigation)

        // Set Home selected
        bottomNavigationView.selectedItemId = R.id.info

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
                R.id.info -> return@OnNavigationItemSelectedListener true
                R.id.about -> {
                    val b = Intent(applicationContext, MainActivity::class.java)
                    startActivity(b)
                    finish()
                    overridePendingTransition(0, 0)
                    return@OnNavigationItemSelectedListener true
                }
            }
            false
        })


    }
    fun clickLogout(view: View){
        prefManager.removeData()
        Toast.makeText(this, "You logged out", Toast.LENGTH_LONG).show()
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()

    }
}
