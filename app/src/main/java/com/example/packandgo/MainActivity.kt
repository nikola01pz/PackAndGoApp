package com.example.packandgo

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button

class MainActivity : AppCompatActivity() {



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val currentTripButton = findViewById<Button>(R.id.btn_current_trip)
        currentTripButton.setOnClickListener{
            val intent = Intent(this, CurrentTripActivity::class.java)
            startActivity(intent)
        }

        val tripHistoryButton = findViewById<Button>(R.id.btn_trip_history)
        tripHistoryButton.setOnClickListener{
            val intent = Intent(this, TripHistoryActivity::class.java)
            startActivity(intent)
        }

        val newTripButton = findViewById<Button>(R.id.btn_new_trip)
        newTripButton.setOnClickListener{
            val intent = Intent(this, NewTripActivity::class.java)
            startActivity(intent)
        }


    }
}