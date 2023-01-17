package com.example.packandgo

import android.content.Intent
import android.content.res.Resources
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator

class CurrentTripActivity : AppCompatActivity() {
    private lateinit var tabLayout: TabLayout
    private lateinit var viewPager: ViewPager2



    companion object{
        const val tripName = "Trip name"
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_current_trip)

        val homeButton = findViewById<Button>(R.id.btn_go_home_from_current)
        homeButton.setOnClickListener{
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }
        tabLayout = findViewById(R.id.tabLayout)
        viewPager = findViewById(R.id.viewPager)
        viewPager.adapter = PageAdapter(this)
        TabLayoutMediator(tabLayout, viewPager){ tab, index ->
            tab.text = when(index){
                0 -> {"Packing list"}
                1 -> {"To visit"}
                2 -> {"Photo ideas"}
                else -> {throw Resources.NotFoundException("Position not found")}
            }
        }.attach()

        val tripName = intent.getStringExtra(tripName)
        findViewById<TextView>(R.id.tV_current_trip_name).text=tripName



    }

}