package com.example.packandgo

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.EditText
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import java.util.*
import kotlin.collections.ArrayList



class ToVisit : Fragment(), TasksRecyclerAdapter.ContentListener {
    private val db = Firebase.firestore
    private lateinit var recyclerAdapter: TasksRecyclerAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_to_visit, container, false)
        val recyclerView = view.findViewById<RecyclerView>(R.id.recyclerView_toVisit)
        val addButton = view.findViewById<FloatingActionButton>(R.id.add_task_button_toVisit)
        addButton.setOnClickListener {
            val newTask = Task(UUID.randomUUID().toString(),false, "", "")
            db.collection("toVisitList").add(newTask)
                .addOnSuccessListener { documentReference ->
                    newTask.id = documentReference.id
                    recyclerAdapter.addItem(newTask)
                }
                .addOnFailureListener { e ->
                    Log.w("ToVisitList", "Error adding task", e)
                    Toast.makeText(context, "Error adding task", Toast.LENGTH_SHORT).show()
                }
        }

        db.collection("toVisitList")
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
                recyclerAdapter = TasksRecyclerAdapter(taskList, this@ToVisit, this)
                recyclerView.apply {
                    layoutManager = LinearLayoutManager(this@ToVisit.context)
                    adapter = recyclerAdapter
                }
            }
            .addOnFailureListener { exception ->
                Log.w("ToVisitList", "Error getting documents.", exception)
            }
        return view
    }

    override fun onItemButtonClick(index: Int, task: Task, clickType: ItemClickType) {
        when (clickType) {
            ItemClickType.EDIT -> {
                val nameEditText = view?.findViewById<EditText>(R.id.taskName)
                val descriptionEditText = view?.findViewById<EditText>(R.id.taskDescription)
                val checkbox = view?.findViewById<CheckBox>(R.id.checkbox)
                val isChecked = checkbox?.isChecked ?: false
                val updatedTask = Task(task.id, isChecked,nameEditText?.text.toString(), descriptionEditText?.text.toString())

                db.collection("toVisitList").document(task.id).set(updatedTask)
                    .addOnSuccessListener {
                        recyclerAdapter.updateItem(index, updatedTask)
                    }
                    .addOnFailureListener { e ->
                        Log.w("ToVisitList", "Error updating task", e)
                        Toast.makeText(context, "Error updating task", Toast.LENGTH_SHORT).show()
                    }
            }
            ItemClickType.REMOVE -> {
                recyclerAdapter.removeItem(index)
                db.collection("toVisitList").document(task.id).delete()
            }
        }
    }
}