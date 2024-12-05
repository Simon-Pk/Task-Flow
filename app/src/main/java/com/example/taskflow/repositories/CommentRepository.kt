package com.example.taskflow.repositories

import android.util.Log
import com.example.taskflow.Tools.convertToClass
import com.example.taskflow.data.Comment
import com.example.taskflow.sources.CommentSources
import com.google.firebase.database.FirebaseDatabase
import javax.inject.Inject
import javax.inject.Singleton
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext

@Singleton
class CommentRepository @Inject constructor(private val commentSources: CommentSources) {
    fun getCommentsList() =
        flow {
                val response = commentSources.commentsSource().get().await().children
                val priorityList = response.convertToClass<Comment>()
                emit(priorityList)
            }
            .catch {
                withContext(Dispatchers.Main) { Log.e("getCommentsList", "${it.message}") }
                emit(listOf())
            }

    suspend fun createCommentsData(comment: Comment) {
        val firebase = FirebaseDatabase.getInstance()
        val commentUID = firebase.reference.push().key.toString()
        commentSources
            .commentsSource()
            .child(commentUID)
            .setValue(comment.copy(uid = commentUID))
            .await()
    }
}
