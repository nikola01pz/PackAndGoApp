package com.example.packandgo

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class TasksRecyclerAdapter(
    private val items: ArrayList<Task>,
    private val listener: ContentListener,
    fragment: Fragment
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private val db = Firebase.firestore
    private val firebaseCollectionName: String = when (fragment) {
        is PackingList -> {
            "packingList"
        }
        is ToVisit -> {
            "toVisitList"
        }
        else -> {
            throw IllegalArgumentException("Fragment must be either PackingList or ToVisit")
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return TaskViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.recycler_item, parent, false)
        )
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is TaskViewHolder -> {
                holder.bind(position, listener, items[position])
            }
        }

        val checkboxItem = holder.itemView.findViewById<CheckBox>(R.id.checkboxItem)
        checkboxItem.isChecked = items[position].checked
        checkboxItem.setOnClickListener {
            items[position].checked = checkboxItem.isChecked
            db.collection(firebaseCollectionName).document(items[position].id)
                .update("checked", checkboxItem.isChecked)
                .addOnFailureListener { e ->
                    Log.w(firebaseCollectionName, "Error updating checkbox state", e)
                }
        }
    }

    override fun getItemCount(): Int {
        return items.size
    }

    fun removeItem(index: Int) {
        items.removeAt(index)
        notifyItemRemoved(index)
        notifyItemRangeChanged(index, items.size)
    }

    fun updateItem(index: Int, task: Task) {
        items[index] = task
        notifyItemChanged(index)
    }

    class TaskViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val taskName = view.findViewById<TextView>(R.id.taskName)
        private val taskDescription = view.findViewById<TextView>(R.id.taskDescription)
        private val editBtn = view.findViewById<ImageButton>(R.id.editButton)
        private val deleteBtn = view.findViewById<ImageButton>(R.id.deleteButton)
        private val nameEditText = view.findViewById<EditText>(R.id.taskName)
        private val descriptionEditText = view.findViewById<EditText>(R.id.taskDescription)
        fun bind(index: Int, listener: ContentListener, task: Task) {
            taskName.text = task.name
            taskDescription.text = task.description
            editBtn.setOnClickListener {
                task.name = nameEditText.text.toString()
                task.description = descriptionEditText.text.toString()
                listener.onItemButtonClick(index, task, ItemClickType.EDIT)
            }
            deleteBtn.setOnClickListener {
                listener.onItemButtonClick(index, task, ItemClickType.REMOVE)
            }
        }
    }

    interface ContentListener {
        fun onItemButtonClick(index: Int, task: Task, clickType: ItemClickType)
    }
}