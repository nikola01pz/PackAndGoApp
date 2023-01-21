package com.example.packandgo

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class PhotoIdeas : Fragment(), TasksWithPhotoRecyclerAdapter.ContentListener {
    private val db = Firebase.firestore
    private lateinit var recyclerAdapter: TasksWithPhotoRecyclerAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_photo_ideas, container, false)
        val recyclerView = view.findViewById<RecyclerView>(R.id.recyclerView_photoIdeas)
        val addTaskButton =
            view.findViewById<FloatingActionButton>(R.id.add_task_button_photoIdeas)
        addTaskButton.setOnClickListener {
            val intent = Intent(activity, NewTaskWithPhoto::class.java)
            intent.putExtra("collection", "photoIdeasList")
            startActivity(intent)
        }


        db.collection("photoIdeasList")
            .get()
            .addOnSuccessListener { result ->
                val taskList = ArrayList<TaskWithPhoto>()

                for (data in result.documents) {
                    val task = data.toObject(TaskWithPhoto::class.java)
                    if (task != null) {
                        task.id = data.id
                        taskList.add(task)
                    }
                }
                recyclerAdapter = TasksWithPhotoRecyclerAdapter(taskList, this)
                recyclerView.apply {
                    layoutManager = LinearLayoutManager(this@PhotoIdeas.context)
                    adapter = recyclerAdapter
                }
            }
            .addOnFailureListener { exception ->
                Log.w("photoIdeasList", "Error getting documents.", exception)
            }
        return view
    }

    override fun onItemButtonClick(index: Int, task: TaskWithPhoto, clickType: ItemClickType) {
        when (clickType) {
            ItemClickType.EDIT -> {
                db.collection("photoIdeasList").document(task.id).set(task)
                    .addOnSuccessListener {
                        recyclerAdapter.updateItem(index, task)
                    }
                    .addOnFailureListener { e ->
                        Log.w("photoIdeasList", "Error updating task", e)
                        Toast.makeText(context, "Error updating task", Toast.LENGTH_SHORT).show()
                    }
            }
            ItemClickType.REMOVE -> {
                recyclerAdapter.removeItem(index)
                db.collection("photoIdeasList").document(task.id).delete()
            }
        }
    }
}