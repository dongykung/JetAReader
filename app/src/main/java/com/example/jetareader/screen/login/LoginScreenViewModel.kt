package com.example.jetareader.screen.login

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.jetareader.model.Muser
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.launch


class LoginScreenViewModel : ViewModel() {
    private val auth: FirebaseAuth = Firebase.auth

    private val _loading = MutableLiveData(false)
    val loading: LiveData<Boolean> = _loading

    fun createUserWithEmailAndPassword(email: String, password: String, home: () -> Unit) {
        if (_loading.value == false) {
            _loading.value = true
            auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        val displayName = task.result.user?.email?.split('@')?.get(0)
                        createUser(displayName)
                        home()
                    } else {
                        Log.d("FB", "createUserWithEmailAndPassword: ${task.result}")
                    }
                    _loading.value = false
                }
        }
    }

    private fun createUser(displayName: String?) {
        val userId = auth.currentUser?.uid

        val user = Muser(
            userId = userId.toString(),
            displayName = displayName.toString(),
            avaterUrl = "",
            quote = "Life is good",
            profession = "Android Developer",
            id =null
        ).toMap()

        FirebaseFirestore.getInstance().collection("users")
            .add(user)
    }

    fun signInWithEmailAndPassword(email: String, password: String, home: () -> Unit) {
        viewModelScope.launch {
            try {
                auth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            Log.d("FB", "signInWithEmailAndPassword: yaya${task.result}")
                            //TODO "take tem home"
                            home()
                        } else {
                            Log.d("FB", "signInWithEmailAndPassword: ${task.result.toString()}")
                        }
                    }
            } catch (e: Exception) {
                Log.d("FB", "signInWithEmailAndPassword: ${e.message} ")
            }
        }
    }

}