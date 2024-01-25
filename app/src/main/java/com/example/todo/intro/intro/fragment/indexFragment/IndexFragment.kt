package com.example.todo.intro.intro.fragment.indexFragment

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.example.todo.databinding.FragmentIndexBinding
import com.example.todo.intro.intro.EditTask.TaskScreenActivity
import com.example.todo.intro.intro.adapters.tasksAdapter

import com.example.todo.intro.intro.database.taskData

import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth

class indexFragment : Fragment() {
    lateinit var adapter: tasksAdapter
    lateinit var dataBinding: FragmentIndexBinding
    lateinit var auth: FirebaseAuth
    private val EDIT_TASK_REQUEST_CODE = 1 // You can use any unique request code
    lateinit var viewModel: indexViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        dataBinding = FragmentIndexBinding.inflate(inflater,container,false)
        return dataBinding.root



    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(this)[indexViewModel::class.java]
        initView()
    }
    private fun initView() {
        auth = Firebase.auth
        adapter = tasksAdapter(taskData.tasksList)
        dataBinding.rvTasks.adapter = adapter
        viewModel.getTasksFromFireStoreDB(auth.currentUser?.uid!!)

        // Observe changes in tasksList LiveData and update the UI accordingly
        viewModel.tasksList.observe(viewLifecycleOwner) { tasks ->
            adapter.updateAdapter(tasks)
        }
        // Inside your click listener or wherever you start TaskScreenActivity
        adapter.onTaskItemClickListener = object : tasksAdapter.OnTaskItemClickListener {
            override fun onItemClick(list: List<taskData>, position: Int) {
                val edit_task = list[position]
                val edit_task_date = list[position].time
                val edit_task_id = list[position].id
                val intent = Intent(activity, TaskScreenActivity::class.java)
                intent.putExtra("position", position)
                intent.putExtra("edit_task", edit_task)
                intent.putExtra("edit_task_date", edit_task_date)
                intent.putExtra("edit_task_id", edit_task_id)
                startActivityForResult(intent, EDIT_TASK_REQUEST_CODE)
            }
        }
        adapter.onImageClickListener = object : tasksAdapter.OnImageClickListener{
            override fun onImageClick(list: List<taskData>, position: Int) {
                var item = list[position]
                item.isDone = true
                adapter.notifyItemChanged(position)
//                updateTaskInFirestore(item)
            }

        }
    }

    override fun onResume() {
        super.onResume()
        viewModel.getTasksFromFireStoreDB(auth.currentUser?.uid!!)
    }

    override fun onStart() {
        super.onStart()
        viewModel.getTasksFromFireStoreDB(auth.currentUser?.uid!!)

    }
}
