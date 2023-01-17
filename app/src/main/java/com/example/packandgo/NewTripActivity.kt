package com.example.packandgo

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.widget.Button
import android.widget.EditText
import android.widget.Toast

class NewTripActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_new_trip)



        val submitButton = findViewById<Button>(R.id.btn_create_new_trip)
        submitButton.setOnClickListener{

            val tripName = findViewById<EditText>(R.id.editText_trip_name).text.toString()

            if (TextUtils.isEmpty(tripName)) {
                Toast.makeText(this, "Empty field not allowed!", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "Proceed..", Toast.LENGTH_SHORT).show()
                val intent = Intent(this, CurrentTripActivity::class.java)
                intent.putExtra(CurrentTripActivity.tripName, tripName)
                startActivity(intent)
            }


        }
    }
}