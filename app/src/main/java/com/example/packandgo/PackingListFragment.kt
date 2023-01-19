package com.example.packandgo

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import java.util.*
import kotlin.collections.ArrayList

class PackingList : Fragment(), TasksRecyclerAdapter.ContentListener {
    private val db = Firebase.firestore
    private lateinit var recyclerAdapter: TasksRecyclerAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_packing_list, container, false)
        val recyclerView = view.findViewById<RecyclerView>(R.id.recyclerView_packingList)

        val addButton = view.findViewById<FloatingActionButton>(R.id.add_task_button)

        addButton.setOnClickListener {
            val newTask = Task(UUID.randomUUID().toString(), "New task", "")
            db.collection("tasks").add(newTask)
                .addOnSuccessListener { documentReference ->
                    newTask.id = documentReference.id
                    recyclerAdapter.addItem(newTask)
                }
                .addOnFailureListener { e ->
                    Log.w("PackingList", "Error adding task", e)
                    Toast.makeText(context, "Error adding task", Toast.LENGTH_SHORT).show()
                }
        }

        db.collection("tasks")
            .get()
            .addOnSuccessListener { result ->
                val taskList = ArrayList<Task>()

                for (data in result.documents) {
                    val task = data.toObject(Task::class.java)
                    if (task != null) {
                        task.id = data.id
                        taskList.add(task)
                    }
                }
                recyclerAdapter = TasksRecyclerAdapter(taskList, this@PackingList)
                recyclerView.apply {
                    layoutManager = LinearLayoutManager(this@PackingList.context)
                    adapter = recyclerAdapter
                }
            }
            .addOnFailureListener { exception ->
                Log.w("PackingList", "Error getting documents.", exception)
            }
        return view
    }

    override fun onItemButtonClick(index: Int, task: Task, clickType: ItemClickType) {
        when (clickType) {
            ItemClickType.EDIT -> {
                val nameEditText = view?.findViewById<EditText>(R.id.taskName)
                val descriptionEditText = view?.findViewById<EditText>(R.id.taskDescription)
                val updatedTask = Task(task.id, nameEditText?.text.toString(), descriptionEditText?.text.toString())

                db.collection("tasks").document(task.id).set(updatedTask)
                    .addOnSuccessListener {
                        recyclerAdapter.updateItem(index, updatedTask)
                    }
                    .addOnFailureListener { e ->
                        Log.w("PackingList", "Error updating task", e)
                        Toast.makeText(context, "Error updating task", Toast.LENGTH_SHORT).show()
                    }
            }
            ItemClickType.REMOVE -> {
                recyclerAdapter.removeItem(index)
                db.collection("tasks").document(task.id).delete()
            }
        }
    }
}