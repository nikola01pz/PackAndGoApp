package com.example.packandgo

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class TasksWithPhotoRecyclerAdapter(
    private val items: ArrayList<TaskWithPhoto>,
    private val listener: ContentListener,
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private val db = Firebase.firestore

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return TaskViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.recycler_itemwithphoto, parent, false)
        )
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is TaskViewHolder -> {
                holder.bind(position, listener, items[position])
            }
        }

        val checkboxItemWithPhoto = holder.itemView.findViewById<CheckBox>(R.id.checkboxItemWithPhoto)
        checkboxItemWithPhoto.isChecked = items[position].isChecked
        checkboxItemWithPhoto.setOnClickListener {
            items[position].isChecked = checkboxItemWithPhoto.isChecked
            db.collection("photoIdeasList").document(items[position].id)
                .update("isChecked", checkboxItemWithPhoto.isChecked)
                .addOnSuccessListener {
                    // Update successful
                }
                .addOnFailureListener { e ->
                    Log.w("photoIdeasList", "Error updating checkbox state", e)
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

    fun updateItem(index: Int, task: TaskWithPhoto) {
        items[index] = task
        notifyItemChanged(index)
    }

    class TaskViewHolder(private val view: View) : RecyclerView.ViewHolder(view) {
        private val taskImage = view.findViewById<ImageView>(R.id.taskWithPhotoImage)
        private val taskName = view.findViewById<TextView>(R.id.taskWithPhotoName)
        private val taskDescription = view.findViewById<TextView>(R.id.taskWithPhotoDescription)
        private val editBtn = view.findViewById<ImageButton>(R.id.editButton)
        private val deleteBtn = view.findViewById<ImageButton>(R.id.deleteButton)
        private val nameEditText = view.findViewById<EditText>(R.id.taskWithPhotoName)
        private val descriptionEditText = view.findViewById<EditText>(R.id.taskWithPhotoDescription)
        fun bind(index: Int, listener: ContentListener, task: TaskWithPhoto) {
            Glide.with(view.context).load(task.imageUrl).into(taskImage)
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
        fun onItemButtonClick(index: Int, task: TaskWithPhoto, clickType: ItemClickType)
    }
}