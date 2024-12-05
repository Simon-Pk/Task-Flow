package com.example.taskflow.repositories

import javax.inject.Inject

class SharedRepository
@Inject
constructor(
    private val userRepository: UserRepository,
) {
    val currentUser = userRepository.currentUser
}
