package com.example.traveler

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import com.example.traveler.databinding.ActivityAboutBinding
import com.google.android.material.bottomnavigation.BottomNavigationView

class AboutMeActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAboutBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAboutBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val logoutButton:Button = findViewById(R.id.logout)
        logoutButton.setOnClickListener {
            val intent = Intent(this@AboutMeActivity, RegisterActivity::class.java)
            startActivity(intent)
        }
        // Initialize and assign variable
        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottom_navigation)

        // Set Home selected
        bottomNavigationView.selectedItemId = R.id.about


        fun addNickname(view: View) {
            binding.apply {
                binding.nicknameText.text = binding.nicknameText.text
                binding.nicknameText.visibility = View.GONE
                binding.nameButton.visibility = View.GONE
                binding.nicknameText.visibility = View.VISIBLE
            }

        }
        binding.nicknameText.text = binding.nicknameText.text.toString()
        fun updateNickname (view: View) {
            binding.apply {
                binding.nameButton.visibility = View.GONE
                binding.nameEdit.text = binding.nameEdit.text
                binding.nameEdit.visibility = View.VISIBLE
                view.visibility = View.GONE
            }

        }

        binding.nameButton.setOnClickListener {
            addNickname(it)
        }
        binding.nameButton.setOnClickListener{
            updateNickname(it)
        }



        fun addOccupation(view: View) {
            binding.apply {
                binding.statusText.text = binding.nicknameText.text
                binding.statusText.visibility = View.GONE
                binding.occupationButton.visibility = View.GONE
                binding.statusText.visibility = View.VISIBLE
            }

        }
        fun updateOccupation (view: View) {
            binding.apply {
                binding.occupationButton.visibility = View.GONE
                binding.status.text = binding.nameEdit.text
                binding.status.visibility = View.VISIBLE
                view.visibility = View.GONE
            }

        }

        binding.occupationButton.setOnClickListener {
            addOccupation(it)
        }
        binding.occupationButton.setOnClickListener{
            updateOccupation(it)
        }


        fun addAge(view: View) {
            binding.apply {
                binding.ageText.text = binding.nicknameText.text
                binding.ageText.visibility = View.GONE
                binding.ageButton.visibility = View.GONE
                binding.ageText.visibility = View.VISIBLE
            }

        }
        fun updateAge (view: View) {
            binding.apply {
                binding.ageButton.visibility = View.GONE
                binding.Age.text = binding.nameEdit.text
                binding.Age.visibility = View.VISIBLE
                view.visibility = View.GONE
            }

        }

        binding.ageButton.setOnClickListener {
            addAge(it)
        }
        binding.ageButton.setOnClickListener{
            updateAge(it)
        }
    }
}