package com.example.todo.intro.intro.database

import java.io.Serializable

data class taskData(
    var id: String ?= null,
    var title: String?= null,
    var description: String?= null,
    var time: String ?= null,
    var isDone:Boolean = false,
): Serializable {

    companion object{
        const val TASK = "task"
        var tasksList= mutableListOf<taskData>()
    }
}
