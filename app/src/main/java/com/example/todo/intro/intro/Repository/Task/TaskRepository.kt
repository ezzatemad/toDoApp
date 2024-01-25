package com.example.todo.intro.intro.Repository.Task

import com.example.todo.intro.intro.database.taskData

interface TaskRepository {
    suspend fun getAllTasks(userId: String): List<taskData>
    suspend fun addTask(task: taskData, userId: String)
    suspend fun updateTask(task: taskData, userId: String)
    suspend fun deleteTask(taskId: String, userId: String)
}