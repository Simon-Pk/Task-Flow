package com.example.taskflow.viewmodels

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.taskflow.data.Notifications
import com.example.taskflow.repositories.NotificationsRepository
import com.example.taskflow.repositories.SharedRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@HiltViewModel
class IncomingNotificationsViewModel
@Inject
constructor(
    private val notificationsRepository: NotificationsRepository,
    private val sharedRepository: SharedRepository,
) : ViewModel() {
    private val _notificationsList = MutableStateFlow(listOf(Notifications()))
    val notificationsList = _notificationsList.asStateFlow()

    init {
        viewModelScope.launch {
            notificationsRepository
                .getNotificationsList(sharedRepository.currentUser?.uid)
                .collect { notificationsList ->
                    _notificationsList.update { notificationsList }
                    Log.d("notificationsList", notificationsList.toString())
                }
        }
    }

    suspend fun updateNotificationsList() {
        notificationsRepository.getNotificationsList(sharedRepository.currentUser?.uid).collect {
            notificationsList ->
            _notificationsList.update { notificationsList }
        }
    }
}
