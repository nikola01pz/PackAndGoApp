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
import kotlin.collections.ArrayList

class ToVisit : Fragment(), TasksRecyclerAdapter.ContentListener {
    private val db = Firebase.firestore
    private lateinit var recyclerAdapter: TasksRecyclerAdapter

    private fun startNewTaskActivity() {
        val intent = Intent(context, NewTaskActivity::class.java)
        intent.putExtra("collection", "toVisitList")
        startActivity(intent)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_to_visit, container, false)
        val recyclerView = view.findViewById<RecyclerView>(R.id.recyclerView_toVisit)
        val addTaskButton =
            view.findViewById<FloatingActionButton>(R.id.add_task_button_toVisit)
        addTaskButton.setOnClickListener {
            startNewTaskActivity()
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
                recyclerAdapter = TasksRecyclerAdapter(taskList, this, this)
                recyclerView.apply {
                    layoutManager = LinearLayoutManager(this@ToVisit.context)
                    adapter = recyclerAdapter
                }
            }
            .addOnFailureListener { exception ->
                Log.w("ToVisit", "Error getting documents.", exception)
            }
        return view
    }

    override fun onItemButtonClick(index: Int, task: Task, clickType: ItemClickType) {
        when (clickType) {
            ItemClickType.EDIT -> {

                db.collection("toVisitList").document(task.id).set(task)
                    .addOnSuccessListener {
                        recyclerAdapter.updateItem(index, task)
                    }
                    .addOnFailureListener { e ->
                        Log.w("ToVisit", "Error updating task", e)
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