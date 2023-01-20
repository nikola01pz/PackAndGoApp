package com.example.packandgo

import android.content.Intent
import android.content.res.Resources
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.Button
import android.widget.Toast
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator

class MyTripActivity : AppCompatActivity() {
    private lateinit var tabLayout: TabLayout
    private lateinit var viewPager: ViewPager2

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_my_trip)

        val homeButton = findViewById<Button>(R.id.btn_go_home_from_current)
        homeButton.setOnClickListener{
            val intent = Intent(this, MainActivity::class.java)
            intent.putExtra("collection", "packingList")
            startActivity(intent)
        }
        val helpButton = findViewById<Button>(R.id.helpButton)
        helpButton.setOnClickListener {
            val toast = Toast.makeText(this, "If you want to mark your note as done, you need to check the checkbox and press EDIT button on the right", Toast.LENGTH_LONG)
            val handler = Handler(Looper.getMainLooper())
            val runnable = Runnable { toast.cancel() }
            handler.postDelayed(runnable, 4000)
            toast.show()
        }

        tabLayout = findViewById(R.id.tabLayout)
        viewPager = findViewById(R.id.viewPager)
        viewPager.adapter = PageAdapter(this)
        when (intent.getStringExtra("startPage")) {
            "packingList" -> viewPager.currentItem = 0
            "toVisitList" -> viewPager.currentItem = 1
        }
        TabLayoutMediator(tabLayout, viewPager){ tab, index ->
            tab.text = when(index){
                0 -> {"Packing list"}
                1 -> {"To visit"}
                2 -> {"Photo ideas"}
                else -> {throw Resources.NotFoundException("Position not found")}
            }
        }.attach()

        val collection = intent.getStringExtra("collection")
        if (collection == "packingList") {
            viewPager.currentItem = 0
        } else if (collection == "toVisitList") {
            viewPager.currentItem = 1
        }
    }

}