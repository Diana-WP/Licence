package com.example.traveler

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import android.util.Log.d
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.content.res.AppCompatResources
import com.example.traveler.databinding.ActivityMainBinding
import com.google.android.material.bottomnavigation.BottomNavigationView

@Suppress("DEPRECATION")
class MainActivity : AppCompatActivity() {
    private lateinit var email: EditText
    private lateinit var age: EditText
    private lateinit var password: EditText
    private lateinit var confirm_password: EditText
    private lateinit var phone: EditText

    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        email = findViewById(R.id.email)
        phone = findViewById(R.id.phone)
        age = findViewById(R.id.age)
        password = findViewById(R.id.password)
        confirm_password = findViewById(R.id.confirm_password)
        binding.buttonReg.isEnabled = false

        // Initialize and assign variable
        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottom_navigation)
        // Set Home selected
        bottomNavigationView.selectedItemId = R.id.about

        findViewById<Button>(R.id.buttonSave).setOnClickListener {
            validateEmptyForm()

        }

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
                R.id.about -> return@OnNavigationItemSelectedListener true
                R.id.info -> {
                    val b = Intent(applicationContext, InfoActivity::class.java)
                    startActivity(b)
                    finish()
                    overridePendingTransition(0, 0)
                    return@OnNavigationItemSelectedListener true
                }
            }
            false
        })

        fun addEmail(view: View) {
            binding.apply {
                binding.emailText.text = binding.email.text.toString()
                binding.email.visibility = View.VISIBLE
                binding.emailText.visibility = View.GONE
                view.visibility = View.GONE
            }
        }

        fun updateEmail(view: View) {
            binding.apply {
                binding.emailText.text = binding.email.text.toString()
                binding.email.visibility = View.VISIBLE
                binding.emailText.visibility = View.GONE
                binding.buttonReg.visibility = View.VISIBLE
                view.visibility = View.GONE
            }
        }

        fun addPhone(view: View) {
            binding.apply {
                binding.phoneText.text = binding.phone.text.toString()
                binding.phone.visibility = View.VISIBLE
                binding.phoneText.visibility = View.GONE
                view.visibility = View.GONE
            }
        }

        fun updatePhone(view: View) {
            binding.apply {
                binding.phoneText.text = binding.phone.text.toString()
                binding.phone.visibility = View.VISIBLE
                binding.phoneText.visibility = View.GONE
                binding.buttonReg.visibility = View.VISIBLE
                view.visibility = View.GONE
            }
        }

        fun addAge(view: View) {
            binding.apply {
                binding.ageText.text = binding.age.text.toString()
                binding.ageText.visibility = View.VISIBLE
                binding.age.visibility = View.GONE
                view.visibility = View.GONE
            }
        }

        fun updateAge(view: View) {
            binding.apply {
                binding.ageText.text = binding.age.text.toString()
                binding.age.visibility = View.VISIBLE
                binding.ageText.visibility = View.GONE
                binding.buttonReg.visibility = View.VISIBLE
                view.visibility = View.GONE
            }
        }

        fun addPassword(view: View) {
            binding.apply {
                binding.passwordText.text = binding.password.text.toString()
                binding.passwordText.visibility = View.VISIBLE
                binding.password.visibility = View.GONE
                view.visibility = View.GONE
            }
        }

        fun updatePassword(view: View) {
            binding.apply {
                binding.passwordText.text = binding.password.text.toString()
                binding.password.visibility = View.VISIBLE
                binding.passwordText.visibility = View.GONE
                binding.buttonReg.visibility = View.VISIBLE
                view.visibility = View.GONE
            }
        }

        fun addConfirmedPassword(view: View) {
            binding.apply {
                binding.confirmText.text = binding.confirmPassword.text.toString()
                binding.confirmText.visibility = View.VISIBLE
                binding.confirmPassword.visibility = View.GONE
                view.visibility = View.GONE
            }
        }

        fun updateConfirmedPassword(view: View) {
            binding.apply {
                binding.confirmText.text = binding.confirmPassword.text.toString()
                binding.confirmPassword.visibility = View.VISIBLE
                binding.confirmText.visibility = View.GONE
                binding.buttonReg.visibility = View.VISIBLE
                view.visibility = View.GONE
            }
        }

        binding.buttonReg.setOnClickListener {
            addEmail(it)
            addPhone(it)
            addAge(it)
            addPassword(it)
            addConfirmedPassword(it)
            updateEmail(it)
            updatePhone(it)
            updateAge(it)
            updatePassword(it)
            updateConfirmedPassword(it)
            binding.buttonReg.visibility = View.VISIBLE

        }

    }


    private fun validateEmptyForm() {

        val icon = AppCompatResources.getDrawable(baseContext, R.drawable.ic_attention)
        icon?.setBounds(0, 0, icon.intrinsicWidth, icon.intrinsicHeight)
        when {
            TextUtils.isEmpty(email.text.toString().trim()) -> {
                email.setError("Please Enter Username", icon)
            }
            TextUtils.isEmpty(phone.text.toString().trim()) -> {
                phone.setError("Please Enter a Phone Number", icon)
            }
            TextUtils.isEmpty(age.text.toString().trim()) -> {
                age.setError("Please Enter Your Age", icon)
            }
            TextUtils.isEmpty(password.text.toString().trim()) -> {
                password.setError("Please Enter Password", icon)
            }
            TextUtils.isEmpty(confirm_password.text.toString().trim()) -> {
                confirm_password.setError("Please Enter Password Again", icon)
            }
            email.text.toString().isNotEmpty() &&
                    phone.text.toString().isNotEmpty() &&
                    age.text.toString().isNotEmpty() &&
                    password.text.toString().isNotEmpty() &&
                    confirm_password.text.toString().isNotEmpty() -> {
                if (email.text.toString().contains(Regex(pattern ="@"))) {
                    if (phone.text.toString().contains(Regex(pattern="[0-9]"))) {
                        if (age.text.toString().contains(Regex(pattern="[0-9]"))) {
                            if (password.text.toString().length >= 5) {
                                if (confirm_password.text.toString() == password.text.toString()) {
                                    Toast.makeText(
                                        baseContext,
                                        "Your data seems fine",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                    binding.buttonReg.isEnabled = true
                                } else {
                                    confirm_password.setError("Passwords don't match")
                                }
                            } else {
                                password.setError("Please enter at least 5 characters!")
                            }
                        } else {
                            age.setError("Please enter a valid age")
                        }
                    } else {
                        phone.setError("Please enter a valid phone number")
                    }
                } else {
                    email.setError("Please enter valid id")
                }
            }
        }
        }

}
