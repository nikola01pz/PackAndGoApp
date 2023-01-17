package com.example.packandgo

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
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

        db.collection("tasks")
            .get()
            .addOnSuccessListener { result ->
                val taskList = ArrayList<Task>()

                taskList.add(Task(UUID.randomUUID().toString(),"Peri zube", "jako"))
                taskList.add(Task(UUID.randomUUID().toString(),"Obuci se", "lijepo"))
                taskList.add(Task(UUID.randomUUID().toString(),"Nasmij se", " "))
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
                db.collection("tasks").document(task.id).set(task)
            }
            ItemClickType.REMOVE -> {
                recyclerAdapter.removeItem(index)
                db.collection("tasks").document(task.id).delete()
            }
        }
    }
}