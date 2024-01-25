package com.example.todo.intro.intro.Repository.User

import com.example.todo.intro.intro.database.userData
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore
import kotlinx.coroutines.tasks.await

class UserRepositoryImpl: UserRepository {
    private val db = Firebase.firestore
    private val userCollection = db.collection(userData.USER)

    override suspend fun getUserById(userId: String): userData? {
        return userCollection.document(userId).get()
            .await().toObject(userData::class.java)

    }

    override suspend fun addUser(user: userData) {
        userCollection.document(user?.id!!).set(user).await()
    }

}