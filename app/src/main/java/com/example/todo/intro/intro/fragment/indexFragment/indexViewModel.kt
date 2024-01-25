package com.example.todo.intro.intro.fragment.indexFragment

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.todo.intro.intro.database.getTasksFromFireStore
import com.example.todo.intro.intro.database.taskData
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import com.google.firebase.firestore.DocumentChange
import kotlinx.coroutines.launch

class indexViewModel : ViewModel() {

    private val _tasksList = MutableLiveData<List<taskData>>()

    val tasksList: LiveData<List<taskData>> get() = _tasksList

    private val auth = Firebase.auth

    init {
        // Initialize or load data in the constructor
        getTasksFromFireStoreDB(auth.currentUser?.uid!!)
    }

    fun getTasksFromFireStoreDB(userID: String) {
        viewModelScope.launch {
            getTasksFromFireStore(
                userId = userID,
                listener = { snapshots, e ->
                    if (e != null) {
                        // Handle error appropriately
                        return@getTasksFromFireStore
                    }

                    val modifiedTasks = mutableListOf<taskData>()

                    for (dc in snapshots!!.documentChanges) {
                        when (dc.type) {
                            DocumentChange.Type.ADDED -> {
                                modifiedTasks.add(dc.document.toObject(taskData::class.java))
                            }
                            DocumentChange.Type.MODIFIED -> {
                                val modifiedTask = dc.document.toObject(taskData::class.java)
                                modifiedTasks.add(modifiedTask)
                            }
                            else -> {}
                        }
                    }
                    // Update the LiveData with the modified tasks
                    _tasksList.value = modifiedTasks
                }
            )
        }
    }
}