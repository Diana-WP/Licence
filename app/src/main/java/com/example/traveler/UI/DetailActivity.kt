package com.example.traveler.UI

import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.example.traveler.R

class DetailActivity : AppCompatActivity() {
    private lateinit var detailDesc: TextView
    private lateinit var detailTitle: TextView
    private lateinit var detailImage: ImageView

    private var key = ""
    private var imageUrl = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)

        detailDesc = findViewById(R.id.detailDesc)
        detailImage = findViewById(R.id.detailImage)
        detailTitle = findViewById(R.id.detailTitle)


        val bundle = intent.extras
        if (bundle != null) {
            detailDesc.text = bundle.getString("Description")
            detailTitle.text = bundle.getString("Name")
            key = bundle.getString("Key")!!
            imageUrl = bundle.getString("Image")!!
            Glide.with(this).load(bundle.getString("Image")).into(detailImage)
        }
    }
}