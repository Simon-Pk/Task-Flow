package com.example.taskflow.repositories

import android.util.Log
import com.example.taskflow.Tools.convertToClass
import com.example.taskflow.data.User
import com.example.taskflow.sources.UserSources
import com.google.firebase.auth.FirebaseUser
import javax.inject.Inject
import javax.inject.Singleton
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext

@Singleton
class UserRepository @Inject constructor(private val userSources: UserSources) {
    fun getUserList() =
        flow {
                val response = userSources.userSource().get().await().children
                val userList = response.convertToClass<User>()
                emit(userList)
            }
            .catch {
                withContext(Dispatchers.Main) { Log.e("getUserList", "${it.message}") }
                emit(listOf())
            }

    val currentUser: FirebaseUser?
        get() = userSources.currentUser
}
