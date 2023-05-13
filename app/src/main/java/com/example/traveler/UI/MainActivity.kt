package com.example.traveler.UI

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
import com.android.volley.NetworkError
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.traveler.PrefManager
import com.example.traveler.R
import com.example.traveler.databinding.ActivityMainBinding
import com.google.android.material.bottomnavigation.BottomNavigationView

@Suppress("DEPRECATION")
class MainActivity : AppCompatActivity() {
    private lateinit var emailText: EditText
    private lateinit var ageText: EditText
    private lateinit var passwordText: EditText
    private lateinit var confirm_password: EditText
    private lateinit var phoneText: EditText
    private val url: String = "http://192.168.168.120/traveler/register.php"
    private lateinit var requestQueue: RequestQueue
    private lateinit var prefManager: PrefManager
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
        prefManager = PrefManager(this)

        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottomNavigationView)
        // Set Home selected
        bottomNavigationView.selectedItemId = R.id.about


        findViewById<Button>(R.id.buttonSave).setOnClickListener {
            validateEmptyForm()
        }
        init()
        checkLogin()
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

    private fun init(){
        prefManager = PrefManager(this)
        emailText = findViewById(R.id.email)
        phoneText = findViewById(R.id.phone)
        ageText = findViewById(R.id.age)
        passwordText = findViewById(R.id.password)
    }

    private fun checkLogin() {
        if (prefManager.isLogin()!!) {
            val intent = Intent(this, FeedActivity::class.java)
            startActivity(intent)
            finish()
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
                if (emailText.text.toString().matches(Regex(pattern = "^([\\w.-]+)@(gmail|yahoo|hotmail|outlook|gmx)\\.com\$"))) {
                    if (phoneText.text.toString().contains(Regex(pattern = "\\d{10}\$"))) {
                        if (ageText.text.toString().contains(Regex(pattern = "\\d{2,3}\$"))) {
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

        // Generate unique key using email and phone
        val key = "${email}_${phone}"
        if (TextUtils.isEmpty(email) || TextUtils.isEmpty(phone) || TextUtils.isEmpty(age) || TextUtils.isEmpty(password) ) {
            Toast.makeText(this, "Please fill all the fields.", Toast.LENGTH_SHORT).show()
        } else {
            val stringRequest = object : StringRequest(
                Method.POST, url,
                Response.Listener { response ->
                    // If response from the php is success
                    if (response.trim().equals("Registration successful")) {
                        Toast.makeText(this, "Registration success", Toast.LENGTH_LONG).show()
                        prefManager.setLogin(true)
                        //It will open the next activity
                        val intent = Intent(this, FeedActivity::class.java)
                        startActivity(intent)
                        finish()
                    } else if(response.trim().equals("Error: User is already registered")) {
                        Toast.makeText(this, "User already registered", Toast.LENGTH_LONG).show()
                        prefManager.setLogin(true)
                        //It will open the next activity
                        val intent = Intent(this, FeedActivity::class.java)
                        startActivity(intent)
                        finish()
                    } else{
                        Toast.makeText(this, "Registration failed", Toast.LENGTH_LONG).show()
                    }
                },
                Response.ErrorListener { error ->
                    if (error is NetworkError) {
                        Toast.makeText(this, "Cannot connect to internet!", Toast.LENGTH_LONG).show()
                    } else {
                        Toast.makeText(this, error.toString(), Toast.LENGTH_LONG).show()
                    }
                }) {
                @Throws(AuthFailureError::class)
                override fun getParams(): Map<String, String> {
                    val params = HashMap<String, String>()
                    params["email"] = email
                    params["phone"] = phone
                    params["age"] = age
                    params["password"] = password
                    params["key"] = key // Add the generated key to the request parameters
                    return params
                }
            }
            requestQueue.add(stringRequest)
        }
    }
}