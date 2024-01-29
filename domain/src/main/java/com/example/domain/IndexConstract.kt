package com.example.domain

data class TaskViewState(val tasks: List<com.example.domain.entities.taskData> = emptyList())

interface IndexView {
    fun render(state: TaskViewState)
}

sealed class IndexIntent {
    object GetTasks : IndexIntent()
    data class EditTask(val position: Int, val task: com.example.domain.entities.taskData) : IndexIntent()
    data class DeleteTask(val position: Int, val task: com.example.domain.entities.taskData) : IndexIntent()
}

