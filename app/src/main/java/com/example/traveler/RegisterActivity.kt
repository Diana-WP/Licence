package com.example.traveler

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.content.res.AppCompatResources
import com.example.traveler.databinding.ActivityRegisterBinding

class RegisterActivity : AppCompatActivity() {
    private lateinit var username: EditText
    private lateinit var password: EditText
    private lateinit var confirm_password: EditText
    private lateinit var phone: EditText
    private lateinit var binding: ActivityRegisterBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        username = findViewById(R.id.username)
        password = findViewById(R.id.password)
        confirm_password = findViewById(R.id.confirm_password)
        phone = findViewById(R.id.phone)

        val loginButton: Button = findViewById(R.id.login)
        loginButton.setOnClickListener {
            val intent = Intent(this@RegisterActivity, AboutMeActivity::class.java)
            startActivity(intent)
        }
        val signButton: Button = findViewById(R.id.loginButton)
        signButton.setOnClickListener {
            val intent = Intent(this@RegisterActivity, MainActivity::class.java)
            startActivity(intent)
        }

        findViewById<Button>(R.id.register).setOnClickListener {
            validateEmptyForm()
        }
    }



    private fun validateEmptyForm() {

        val icon = AppCompatResources.getDrawable(baseContext, R.drawable.ic_attention)
        icon?.setBounds(0, 0, icon.intrinsicWidth, icon.intrinsicHeight)
        when {
            TextUtils.isEmpty(username.text.toString().trim()) -> {
                username.setError("Please Enter Username", icon)
            }
            TextUtils.isEmpty(password.text.toString().trim()) -> {
                password.setError("Please Enter Password", icon)
            }
            TextUtils.isEmpty(confirm_password.text.toString().trim()) -> {
                confirm_password.setError("Please Confirm Password", icon)
            }
            TextUtils.isEmpty(phone.text.toString().trim()) -> {
                phone.setError("Please Enter Phone", icon)
            }
            username.text.toString().isNotEmpty() &&
                    password.text.toString().isNotEmpty() &&
                    confirm_password.toString().isNotEmpty() &&
                    phone.toString().isNotEmpty() -> {
                if (username.text.toString().matches(Regex(pattern = "@"))) {
                    if (password.text.toString().length >= 5) {
                        if (password.text.toString() == confirm_password.text.toString()) {
                            if (phone.text.toString().matches(Regex("[0-9]"))) {
                                Toast.makeText(
                                    baseContext,
                                    "Register Successful",
                                    Toast.LENGTH_SHORT
                                ).show()
                            } else {
                                phone.setError("Phone is not correct")
                            }
                        } else {
                            confirm_password.setError("Password didn't match")
                        }

                    } else {
                        password.setError("Please enter at least 5 characters!")
                    }
                } else {
                    username.setError("Please enter valid id")
                }
            }
        }
    }

    fun EditText.afterTextChanged(afterTextChanged: (String) -> Unit) {
        this.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(editable: Editable?) {
                afterTextChanged.invoke(editable.toString())
            }

            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {}
        })
    }
}
