package com.example.traveler.UI

import DataClass
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
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.traveler.databinding.ActivityPostBinding
import com.example.traveler.R
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import java.text.DateFormat
import java.util.Calendar

class PostActivity: AppCompatActivity() {
    private lateinit var binding: ActivityPostBinding
    private lateinit var uploadImage: ImageView
    private lateinit var uploadName: EditText
    private lateinit var uploadDesc: EditText
    private lateinit var postButton: Button
    private var uri : Uri? = null
    private var imageURL: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPostBinding.inflate(layoutInflater)
        setContentView(binding.root) // add this line to set the layout
        uploadImage = findViewById(R.id.uploadImage)
        uploadName = findViewById(R.id.uploadName)
        uploadDesc = findViewById(R.id.uploadDesc)
        postButton = findViewById(R.id.postButton)

        val permission = ContextCompat.checkSelfPermission(this, android.Manifest.permission.READ_EXTERNAL_STORAGE)
        if (permission != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, arrayOf(android.Manifest.permission.READ_EXTERNAL_STORAGE), 1)
        }
        val activityResultLauncher = registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                val data: Intent? = result.data
                uri = data?.data
                uploadImage.setImageURI(uri)
            } else {
                Toast.makeText(this, "No Image Selected", Toast.LENGTH_SHORT).show()
            }
        }

        uploadImage.setOnClickListener {
            val photoPicker = Intent(Intent.ACTION_PICK)
            photoPicker.type = "image/*"
            activityResultLauncher.launch(photoPicker)
        }

        postButton.setOnClickListener {
            saveData()
            uploadData()
        }
    }


    private fun saveData() {
        uri?.let {
            val storageReference = FirebaseStorage.getInstance().reference
                .child("Traveler")
                .child(it.lastPathSegment!!)
            val builder = AlertDialog.Builder(this)
            builder.setCancelable(false)
            builder.setView(R.layout.progress_layout)
            val dialog = builder.create()
            if (!isFinishing) {
                dialog.show()
            }
            storageReference.putFile(it).addOnSuccessListener { taskSnapshot ->
                val uriTask = taskSnapshot.storage.downloadUrl
                uriTask.addOnSuccessListener { uri ->
                    imageURL = uri.toString()
                    if (!isFinishing) {
                        dialog.dismiss()
                    }
                }.addOnFailureListener {
                    if (!isFinishing) {
                        dialog.dismiss()
                    }
                    Toast.makeText(this, "Failed to get download URL", Toast.LENGTH_SHORT).show()
                }
            }.addOnFailureListener {
                if (!isFinishing) {
                    dialog.dismiss()
                }
                Toast.makeText(this, "Failed to upload image", Toast.LENGTH_SHORT).show()
            }
        }
    }


    private fun uploadData() {
        val name = uploadName.text.toString()
        val desc = uploadDesc.text.toString()
        val dataClass = DataClass(name, desc, imageURL)

        // We are changing the child from title to currentDate,
        val currentDate = DateFormat.getDateTimeInstance().format(Calendar.getInstance().time)

        FirebaseDatabase.getInstance().getReference("Traveler").child(currentDate)
            .setValue(dataClass).addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Toast.makeText(this, "Saved", Toast.LENGTH_SHORT).show()
                    finish()
                }
            }.addOnFailureListener { e ->
                Toast.makeText(this, e.message.toString(), Toast.LENGTH_SHORT).show()
            }
    }
}