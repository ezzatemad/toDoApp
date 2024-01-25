package com.example.todo.intro.intro.adapters


import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.todo.R
import com.example.todo.intro.intro.database.taskData

class tasksAdapter(var list: List<taskData>?= null): RecyclerView.Adapter<tasksAdapter.viewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): viewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.tasks_view, parent, false)
        return viewHolder(view)
    }

    override fun getItemCount(): Int {
        return list?.size ?: 0
    }

    override fun onBindViewHolder(holder: viewHolder, position: Int) {
        val item = list?.get(position)
        holder.item_title.text = item?.title
        holder.item_date.text = item?.time.toString()
        holder.itemView.setOnLongClickListener {
            onTaskItemClickListener?.onItemClick(list!!, position)
            true
        }
        if (item?.isDone == true) {
            holder.item_complete.visibility = View.VISIBLE


        } else {
            holder.item_check.visibility = View.VISIBLE
            holder.item_complete.visibility = View.GONE
        }
        holder.item_check.setOnClickListener {
            onImageClickListener?.onImageClick(list!!, position)
        }
    }

    fun updateAdapter(list: List<taskData>?) {
        this.list = list
        notifyDataSetChanged()
    }

    var onTaskItemClickListener: OnTaskItemClickListener? = null

    interface OnTaskItemClickListener {
        fun onItemClick(taskData: List<taskData>, position: Int)
    }

    var onImageClickListener: OnImageClickListener? = null

    interface OnImageClickListener {
        fun onImageClick(list: List<taskData>, position: Int)
    }


    inner class viewHolder(val view: View) : RecyclerView.ViewHolder(view) {
        val item_title: TextView = view.findViewById(R.id.tv_task_title)
        val item_date: TextView = view.findViewById(R.id.tv_task_date)
        val item_complete: ImageView = view.findViewById(R.id.iv_task_check_completed)
        val item_check: ImageView = view.findViewById(R.id.iv_task_check)

    }
}