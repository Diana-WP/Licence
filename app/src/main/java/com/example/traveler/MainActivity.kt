package com.example.traveler

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.content.res.AppCompatResources
import com.android.volley.AuthFailureError
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.traveler.databinding.ActivityMainBinding
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.android.synthetic.main.activity_main.*


@Suppress("DEPRECATION")
class MainActivity : AppCompatActivity() {
    private lateinit var emailText: EditText
    private lateinit var ageText: EditText
    private lateinit var passwordText: EditText
    private lateinit var confirm_password: EditText
    private lateinit var phoneText: EditText
    private val url: String = "http://192.168.0.105/traveler/register.php"
    private lateinit var requestQueue: RequestQueue
    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        emailText = findViewById(R.id.email)
        phoneText = findViewById(R.id.phone)
        ageText = findViewById(R.id.age)
        passwordText = findViewById(R.id.password)
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
        // Instantiate the RequestQueue.
        requestQueue = Volley.newRequestQueue(this)

        findViewById<Button>(R.id.buttonReg).setOnClickListener {
            registerUser()
        }
    }

    private fun validateEmptyForm() {

        val icon = AppCompatResources.getDrawable(baseContext, R.drawable.ic_attention)
        icon?.setBounds(0, 0, icon.intrinsicWidth, icon.intrinsicHeight)
        when {
            TextUtils.isEmpty(emailText.text.toString().trim()) -> {
                emailText.setError("Please Enter Username", icon)
            }
            TextUtils.isEmpty(phoneText.text.toString().trim()) -> {
                phoneText.setError("Please Enter a Phone Number", icon)
            }
            TextUtils.isEmpty(ageText.text.toString().trim()) -> {
                ageText.setError("Please Enter Your Age", icon)
            }
            TextUtils.isEmpty(passwordText.text.toString().trim()) -> {
                passwordText.setError("Please Enter Password", icon)
            }
            TextUtils.isEmpty(confirm_password.text.toString().trim()) -> {
                confirm_password.setError("Please Enter Password Again", icon)
            }
            emailText.text.toString().isNotEmpty() &&
                    phoneText.text.toString().isNotEmpty() &&
                    ageText.text.toString().isNotEmpty() &&
                    passwordText.text.toString().isNotEmpty() &&
                    confirm_password.text.toString().isNotEmpty() -> {
                if (emailText.text.toString().contains(Regex(pattern = "@"))) {
                    if (phoneText.text.toString().contains(Regex(pattern = "[0-9]"))) {
                        if (ageText.text.toString().contains(Regex(pattern = "[0-9]"))) {
                            if (passwordText.text.toString().length >= 5) {
                                if (confirm_password.text.toString() == passwordText.text.toString()) {
                                    Toast.makeText(
                                        baseContext,
                                        "Your data seems fine",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                    binding.buttonReg.isEnabled = true
                                } else {
                                    confirm_password.error = "Passwords don't match"
                                }
                            } else {
                                passwordText.error = "Please enter at least 5 characters!"
                            }
                        } else {
                            ageText.error = "Please enter a valid age"
                        }
                    } else {
                        phoneText.setError("Please enter a valid phone number")
                    }
                } else {
                    emailText.setError("Please enter valid id")
                }
            }
        }
    }

    private fun registerUser() {
        val email = emailText.text.toString().trim()
        val phone = phoneText.text.toString().trim()
        val age = ageText.text.toString().trim()
        val password = passwordText.text.toString().trim()

        if (TextUtils.isEmpty(email) || TextUtils.isEmpty(phone) || TextUtils.isEmpty(age) || TextUtils.isEmpty(password) ) {
            Toast.makeText(this, "Please fill all the fields.", Toast.LENGTH_SHORT).show()
        } else {
            val stringRequest = object : StringRequest(
                Method.POST, url,
                Response.Listener { response ->
                    // registration successful
                    Toast.makeText(this, "Registration successful", Toast.LENGTH_SHORT).show()
                    // start the feed activity
                    val intent = Intent(this, FeedActivity::class.java)
                    startActivity(intent)
                    // finish the current activity
                    finish()
                },
                Response.ErrorListener { error ->
                    Toast.makeText(this, "Registration failed: ${error.message}", Toast.LENGTH_SHORT).show()
                }) {
                @Throws(AuthFailureError::class)
                override fun getParams(): Map<String, String> {
                    val params = HashMap<String, String>()
                    params["email"] = email
                    params["phone"] = phone
                    params["age"] = age
                    params["password"] = password
                    return params
                }
            }
            requestQueue.add(stringRequest)
        }
    }
}