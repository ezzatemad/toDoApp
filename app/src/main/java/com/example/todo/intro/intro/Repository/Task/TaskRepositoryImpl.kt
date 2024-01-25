package com.example.todo.intro.intro.Repository.Task

import com.example.todo.intro.intro.database.taskData
import com.example.todo.intro.intro.database.userData
import com.google.firebase.Firebase
import com.google.firebase.firestore.QuerySnapshot
import com.google.firebase.firestore.firestore
import com.google.firebase.firestore.toObject
import kotlinx.coroutines.tasks.await

class TaskRepositoryImpl: TaskRepository {
    private val db = Firebase.firestore
    private val taskCollection = db.collection(taskData.TASK)
    override suspend fun getAllTasks(userId: String): List<taskData> {
        // Use collectionGroup to query tasks across all users
        val querySnapshot: QuerySnapshot =
            db.collectionGroup(taskData.TASK)
                .whereEqualTo("userId", userId)
                .get().await()

        // Convert each document to taskData
        return querySnapshot.documents.mapNotNull { documentSnapshot ->
            documentSnapshot.toObject<taskData>()
        }
    }

    override suspend fun addTask(task: taskData, userId: String) {
        taskCollection.document(taskData.TASK).set(task).await()
    }

    override suspend fun updateTask(task: taskData, userId: String) {

    }

    override suspend fun deleteTask(taskId: String, userId: String) {
    }
}