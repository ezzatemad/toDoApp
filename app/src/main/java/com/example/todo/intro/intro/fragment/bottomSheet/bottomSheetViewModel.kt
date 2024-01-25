package com.example.todo.intro.intro.fragment.bottomSheet

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.todo.intro.intro.database.addTaskToFireStore
import com.example.todo.intro.intro.database.taskData
import com.google.android.gms.tasks.OnSuccessListener
import kotlinx.coroutines.launch

class bottomSheetViewModel : ViewModel() {

    fun addTaskToFireStoreDB(
        userID: String,
        title: String,
        description: String,
        time: String,
        onSuccess: () -> Unit,
        onFailure: (String) -> Unit
    ) {
        viewModelScope.launch {

            addTaskToFireStore(
                taskData(id = userID, title = title, description = description, time = time),
                userid = userID,
                addOnSuccessListener = {
                    onSuccess.invoke()
                },
                addOnFailureListener = {
                    onFailure.invoke(it.localizedMessage ?: "")
                }
            )
        }
    }
}
