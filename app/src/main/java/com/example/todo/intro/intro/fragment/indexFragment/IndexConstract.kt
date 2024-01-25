package com.example.todo.intro.intro.fragment.indexFragment

import com.example.todo.intro.intro.database.taskData

data class TaskViewState(val tasks: List<taskData> = emptyList())

interface IndexView {
    fun render(state: TaskViewState)
}

sealed class IndexIntent {
    object GetTasks : IndexIntent()
    data class EditTask(val position: Int, val task: taskData) : IndexIntent()
    data class DeleteTask(val position: Int, val task: taskData) : IndexIntent()
}

