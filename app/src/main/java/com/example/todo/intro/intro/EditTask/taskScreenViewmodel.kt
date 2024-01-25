package com.example.todo.intro.intro.EditTask

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.todo.intro.intro.database.deleteTask
import com.example.todo.intro.intro.database.taskData
import com.example.todo.intro.intro.database.updateTask
import com.example.todo.intro.intro.database.userData
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.launch

class taskScreenViewmodel : ViewModel() {

    val taskUpdated = MutableLiveData<Boolean>()
    val taskDeleted = MutableLiveData<Boolean>()


    fun updateTaskDB(
        userid: String, taskId: String,
        updatedTitle: String, updatedDescription: String,
        updatedTime: String, updatedDone: Boolean
    ) {
        viewModelScope.launch {
            updateTask(userid, taskId, updatedTitle, updatedDescription, updatedTime, updatedDone,
                addOnSuccessListener = {
                    // Document successfully updated
                    Log.e("TAG", "DocumentSnapshot successfully updated!")
                    taskUpdated.postValue(true)
                },
                addOnFailureListener = {
                    // Handle errors here
                    Log.e("TAG", "Error updating document")
                })
        }

    }

    fun deleteTaskDB(userid: String, taskId: String) {
        viewModelScope.launch {
            deleteTask(userid,
                taskId,
                addOnSuccessListener = {
                    // Document successfully deleted
                    Log.e("TAG", "DocumentSnapshot successfully deleted!")
//                finish()
                    taskDeleted.postValue(true)
                },
                addOnFailureListener = {
                    // Handle errors here
                    Log.e("TAG", "Error deleting document${it.localizedMessage}")
                })
        }
    }


}