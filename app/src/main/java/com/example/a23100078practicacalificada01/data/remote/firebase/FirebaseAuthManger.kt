package com.example.a23100078practicacalificada01.data.remote.firebase

import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

object FirebaseAuthManger {
    private val auth = FirebaseAuth.getInstance()
    private val firestore = FirebaseFirestore.getInstance()

    //Register User
    suspend fun registerUser(email: String,
                             name: String,
                             lastName: String,
                             password: String):
    Result<Unit>{
        return try {
            val authResult = auth.createUserWithEmailAndPassword(email, password).await()
            val uid = authResult.user?.uid.toString()

            val user = hashMapOf(
                "uid" to uid,
                "email" to email,
                "name" to name,
                "lastName" to lastName,
            )
            firestore.collection("users").document(uid).set(user).await()
            Result.success(Unit)
        } catch (e: Exception){
            Result.failure(e)
        }
    }

    //Login User
    suspend fun loginUser(email: String, password: String): Result<Unit>{
        return try {
            auth.signInWithEmailAndPassword(email, password).await()
            Result.success(Unit)
        } catch (e: Exception){
            Result.failure(e)
        }
    }
}