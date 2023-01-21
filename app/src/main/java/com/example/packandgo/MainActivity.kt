package com.example.packandgo

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val currentTripButton = findViewById<Button>(R.id.btn_my_trip)
        currentTripButton.setOnClickListener {
            val intent = Intent(this, MyTripActivity::class.java)
            startActivity(intent)
        }


    }
}