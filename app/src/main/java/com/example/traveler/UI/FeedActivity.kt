package com.example.traveler.UI

import DataClass
import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.widget.SearchView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.traveler.R
import com.example.traveler.adaptors.PostAdapter
import com.example.traveler.databinding.ActivityFeedBinding
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import java.util.Locale


@Suppress("DEPRECATION")
class FeedActivity : AppCompatActivity() {
    private lateinit var fab: FloatingActionButton
    private lateinit var databaseReference: DatabaseReference
    private lateinit var binding : ActivityFeedBinding
    private lateinit var eventListener: ValueEventListener
    private lateinit var recyclerView: RecyclerView
    private lateinit var dataList: MutableList<DataClass>
    private lateinit var adapter: PostAdapter
    private lateinit var searchView: SearchView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFeedBinding.inflate(layoutInflater)
        setContentView(binding.root)
        recyclerView = findViewById(R.id.recyclerView)
        fab = findViewById(R.id.fab)
        searchView = findViewById(R.id.search)
        searchView.clearFocus()

        val gridLayoutManager = GridLayoutManager(this, 1)
        recyclerView.layoutManager = gridLayoutManager

        val builder = AlertDialog.Builder(this)
        builder.setCancelable(false)
        builder.setView(R.layout.progress_layout)
        val dialog = builder.create()
        dialog.show()

        dataList = mutableListOf()

        adapter = PostAdapter(this, dataList)
        recyclerView.adapter = adapter

        databaseReference = FirebaseDatabase.getInstance().getReference("Traveler")
        dialog.show()
        eventListener = databaseReference.addValueEventListener(object : ValueEventListener {
            @SuppressLint("NotifyDataSetChanged")
            override fun onDataChange(snapshot: DataSnapshot) {
                dataList.clear()
                for (itemSnapshot in snapshot.children) {
                    val dataClass = itemSnapshot.getValue(DataClass::class.java)
                    dataClass?.let {
                        it.key = itemSnapshot.key.toString()
                        dataList.add(it)
                    }
                }
                adapter.notifyDataSetChanged()
                dialog.dismiss()
            }

            override fun onCancelled(error: DatabaseError) {
                dialog.dismiss()
            }
        })

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String): Boolean {
                searchList(newText)
                return true
            }
        })

        fab.setOnClickListener {
            val intent = Intent(this, PostActivity::class.java)
            startActivity(intent)
        }

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
    private fun searchList(text: String) {
        val searchList = ArrayList<DataClass>()
        for (dataClass in dataList) {
            if (dataClass.dataName?.toLowerCase(Locale.ROOT)?.contains(text.toLowerCase(Locale.ROOT)) == true) {
                searchList.add(dataClass)
            }
        }
        adapter.searchDataList(searchList)
    }

}