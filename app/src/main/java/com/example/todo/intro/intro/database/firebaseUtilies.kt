package com.example.todo.intro.intro.database

import android.util.Log
import com.google.android.gms.tasks.OnFailureListener
import com.google.android.gms.tasks.OnSuccessListener
import com.google.firebase.firestore.DocumentChange
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.EventListener
import com.google.firebase.firestore.QuerySnapshot
import com.google.firebase.firestore.core.UserData
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.installations.remote.TokenResult
import com.google.firebase.ktx.Firebase

fun addUserToFireStore(
    UserData: userData,
    addOnSuccessListener: OnSuccessListener<Void>,
    addOnFailureListener: OnFailureListener
){
    val db = Firebase.firestore
    val collection = db.collection(userData.USER)
    collection
        .document(UserData.id!!)
        .set(UserData)
        .addOnSuccessListener(addOnSuccessListener)
        .addOnFailureListener(addOnFailureListener)
}


fun addTaskToFireStore(
    TaskData: taskData,
    userid: String,
    addOnSuccessListener: OnSuccessListener<Void>,
    addOnFailureListener: OnFailureListener
){
    val db = Firebase.firestore
    val userCollection = db.collection(userData.USER)
    val userDoc = userCollection.document(userid)
    val taskCollection = userDoc.collection(taskData.TASK)
    val taskDoc = taskCollection.document()
    TaskData.id = taskDoc.id
    taskDoc
        .set(TaskData)
        .addOnSuccessListener(addOnSuccessListener)
        .addOnFailureListener(addOnFailureListener)
}


fun getTasksFromFireStore(
    userId: String,
    listener: EventListener<QuerySnapshot>

) {
    val db = Firebase.firestore
    val userCollection = db.collection(userData.USER)
    val userDoc = userCollection.document(userId)
    val taskCollection = userDoc.collection(taskData.TASK)
    taskCollection
        .addSnapshotListener(listener)

}




fun getUserFromFirestore(
    uid: String,
    addOnSuccessListener: OnSuccessListener<DocumentSnapshot>,
    addOnFailureListener: OnFailureListener
){
    val db = Firebase.firestore
    val collection = db.collection(userData.USER)
    collection
        .document(uid)
        .get()
        .addOnSuccessListener(addOnSuccessListener)
        .addOnFailureListener(addOnFailureListener)
}