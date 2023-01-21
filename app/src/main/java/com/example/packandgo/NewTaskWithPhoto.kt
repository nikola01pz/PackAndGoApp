package com.example.packandgo

import android.app.Activity
import android.content.ContentValues.TAG
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.EditText
import android.widget.ImageButton
import android.widget.Toast
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class NewTaskWithPhoto : AppCompatActivity() {
    private val db = Firebase.firestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_new_taskwithphoto)
        val taskImageUrlEditText = findViewById<EditText>(R.id.newTaskPhotoUrl)
        val taskNameEditText = findViewById<EditText>(R.id.newTaskName)
        val taskDescriptionEditText = findViewById<EditText>(R.id.newTaskDescription)
        val saveButton = findViewById<ImageButton>(R.id.saveNewTask)
        saveButton.setOnClickListener {
            val taskImageUrl = taskImageUrlEditText.text.toString()
            val taskName = taskNameEditText.text.toString()
            val taskDescription = taskDescriptionEditText.text.toString()
            if (taskName.isNotEmpty()) {
                val newTask = hashMapOf(
                    "imageUrl" to taskImageUrl,
                    "name" to taskName,
                    "description" to taskDescription,
                    "isChecked" to false
                )
                val collectionName = intent.getStringExtra("collection")
                if (collectionName != null) {
                    db.collection(collectionName)
                        .add(newTask)
                        .addOnSuccessListener { documentReference ->
                            Log.d(TAG, "Task added with ID: ${documentReference.id}")
                            setResult(Activity.RESULT_OK)
                            startMyTripActivity()
                        }
                        .addOnFailureListener { e ->
                            Log.w(TAG, "Error adding task", e)
                            Toast.makeText(this, "Error adding task", Toast.LENGTH_SHORT).show()
                        }
                }
            } else {
                Toast.makeText(this, "Please enter a task name", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun startMyTripActivity() {
        val intent = Intent(this, MyTripActivity::class.java)
        intent.putExtra("openFragment", "photoIdeas")
        startActivity(intent)
        overridePendingTransition(R.anim.popup_enter, R.anim.popup_exit)
    }
}